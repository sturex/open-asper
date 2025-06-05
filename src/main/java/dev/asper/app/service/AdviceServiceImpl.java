package dev.asper.app.service;

import dev.asper.advice.*;
import dev.asper.app.component.PipelineModelRecoverException;
import dev.asper.app.config.AdviceServiceConfig;
import dev.asper.app.entity.Model;
import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.clickhouse.OpponentType;
import dev.asper.metric.hs.HoldemHandStrengthCalculator;
import dev.asper.poker.ai.AdvisorInfo;
import dev.asper.app.entity.SolutionInfo;
import dev.asper.poker.ai.*;
import dev.asper.poker.card.Card;
import dev.asper.poker.card.CardPair;
import dev.asper.poker.card.PreflopRange;
import dev.asper.poker.chart.PostflopActionChart;
import dev.asper.poker.chart.PostflopAmountChart;
import dev.asper.poker.chart.PreflopActionChart;
import dev.asper.poker.chart.PreflopAmountChart;
import dev.asper.poker.engine.*;
import dev.asper.poker.engine.spot.PreflopSpot;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.enums.*;
import dev.asper.app.component.GameTreeGenerator;
import dev.asper.poker.generate.PokerGameEdge;
import dev.asper.poker.generate.PokerGameNode;
import dev.asper.spark.FeatureSchema;
import dev.asper.spark.PipelineInfo;
import dev.asper.spark.PipelineUtils;
import dev.asper.stat.HoldemStatistics;
import dev.asper.stat.PreflopBranchStatistics;
import dev.asper.stat.PreflopStatisticsKey;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AdviceServiceImpl implements AdviceService {

    private final PokerHandReplayer pokerHandReplayer = new PokerHandReplayer(new PokerEngine());
    private final Map<String, Solution> solutionPool = new HashMap<>();
    private final SolutionService solutionService;
    private final PreflopActionChartService preflopActionChartService;
    private final PreflopAmountChartService preflopAmountChartService;
    private final PostflopAmountChartService postflopAmountChartService;
    private final PostflopActionChartService postflopActionChartService;
    private final SpecialSpotTweakService specialSpotTweakService;
    private final StatisticsService statisticsService;
    private final SparkSession sparkSession;
    private final ModelService modelService;
    private final LogService logService;
    private final OpponentService opponentService;
    private final GameTreeGenerator gameTreeGenerator;

    @Autowired
    public AdviceServiceImpl(AdviceServiceConfig adviceServiceConfig,
                             SolutionService solutionService,
                             PreflopActionChartService preflopActionChartService,
                             PreflopAmountChartService preflopAmountChartService,
                             PostflopAmountChartService postflopAmountChartService,
                             PostflopActionChartService postflopActionChartService,
                             SpecialSpotTweakService specialSpotTweakService,
                             StatisticsService statisticsService,
                             SparkSession sparkSession,
                             ModelService modelService,
                             LogService logService,
                             OpponentService opponentService,
                             GameTreeGenerator gameTreeGenerator) {
        this.solutionService = solutionService;
        this.preflopActionChartService = preflopActionChartService;
        this.preflopAmountChartService = preflopAmountChartService;
        this.postflopAmountChartService = postflopAmountChartService;
        this.postflopActionChartService = postflopActionChartService;
        this.specialSpotTweakService = specialSpotTweakService;
        this.statisticsService = statisticsService;
        this.sparkSession = sparkSession;
        this.modelService = modelService;
        this.logService = logService;
        this.opponentService = opponentService;
        this.gameTreeGenerator = gameTreeGenerator;
        createSolutionPool(adviceServiceConfig.getSolutionNames());
    }

    record ProfitLoss(double profit, double loss) {
        public double diff() {
            return profit - loss;
        }

        public boolean isPositive() {
            return profit > loss;
        }
    }

    @Override
    public void createSolutionPool(List<String> solutionNames) {
        solutionPool.clear();
        Set<SolutionInfo> solutionInfos = solutionService.findByNameIn(solutionNames);
        Instant started = Instant.now();
        logService.log(started, LogService.Units.SEC, LogRecordCode.SOLUTION_POOL_RECOVER_STARTED, Map.of("solution names", String.join(", ", solutionNames)));
        solutionInfos.forEach(solution -> {
            logService.log(started, LogService.Units.SEC, LogRecordCode.SOLUTION_RECOVER_STARTED, Map.of("solution name", solution.getName()));
            solutionPool.put(solution.getName(), recoverPokerSolution(started, solution));
        });
    }

    private Solution recoverPokerSolution(Instant started, SolutionInfo solutionInfo) {
        AtomicInteger cnt = new AtomicInteger();
        String solutionName = solutionInfo.getName();
        List<AdvisorInfo> advisorInfos = solutionService.recoverAdvisorInfos(solutionInfo);
        Map<Spot, AdvisorInfo> advisorInfoMap = advisorInfos.stream()
                .collect(Collectors.toMap((AdvisorInfo advisorInfo) -> advisorInfo.spotInfo().spot(), Function.identity()));
        Set<Map.Entry<Spot, AdvisorInfo>> entries = advisorInfoMap.entrySet();
        int size = entries.size();
        Map<Spot, Advisor> map = entries
                .parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    AdvisorInfo advisorInfo = e.getValue();
                    AmountAdvisorType amountAdvisorType = advisorInfo.amountAdvisorType();
                    ActionAdvisorType actionAdvisorType = advisorInfo.actionAdvisorType();
                    if (actionAdvisorType == ActionAdvisorType.ML_ACTION || amountAdvisorType == AmountAdvisorType.ML_AMOUNT) {
                        int i = cnt.incrementAndGet();
                        if (i % 10 == 0) {
                            logService.log(started, LogService.Units.SEC, LogRecordCode.SOLUTION_RECOVER_CLOCK, Map.of("solutionName", solutionName, "counter", String.valueOf(i), "total", String.valueOf(size * 2)));
                        }
                    }
                    return recoverAdvisor(e.getKey(), advisorInfo);
                }));
        return new Solution(solutionName, map);
    }

    @Override
    public Advisor recoverAdvisor(Spot spot, AdvisorInfo advisorInfo) {
        ActionAdvisorType actionAdvisorType = advisorInfo.actionAdvisorType();
        AmountAdvisorType amountAdvisorType = advisorInfo.amountAdvisorType();
        ActionTweak actionTweak = advisorInfo.actionTweak();
        AmountTweak amountTweak = advisorInfo.amountTweak();
        String actionAdvisorName = advisorInfo.actionAdvisorName();
        String amountAdvisorName = advisorInfo.amountAdvisorName();
        if (actionAdvisorType == ActionAdvisorType.ML_ACTION && amountAdvisorType == AmountAdvisorType.ML_AMOUNT) {
            return tryRecoverMlAdvisor(spot, advisorInfo);
        } else {
            ActionAdvisor actionAdvisor = recoverActionAdvisor(spot, actionAdvisorType, actionTweak, actionAdvisorName);
            AmountAdvisor amountAdvisor = recoverAmountAdvisor(spot, amountAdvisorType, amountTweak, amountAdvisorName);
            return new SimpleAdvisor(actionAdvisor, amountAdvisor);
        }
    }

    @Override
    public AmountAdvisor recoverAmountAdvisor(Spot spot, AmountAdvisorType amountAdvisorType, AmountTweak amountTweak, String amountAdvisorName) {
        return switch (amountAdvisorType) {
            case ML_AMOUNT -> tryRecoverMlAmountAdvisor(spot, amountAdvisorName, amountTweak);
            case CHART_PREFLOP_AMOUNT -> {
                PreflopAmountChart preflopAmountChart = preflopAmountChartService.findByName(amountAdvisorName);
                preflopAmountChart.setAmountTweak(amountTweak);
                yield preflopAmountChart;
            }
            case CHART_POSTFLOP_AMOUNT -> {
                PostflopAmountChart postflopAmountChart = postflopAmountChartService.findByName(amountAdvisorName);
                postflopAmountChart.setAmountTweak(amountTweak);
                yield postflopAmountChart;
            }
            case COUNTER_STRATEGY_AMOUNT -> throw new RuntimeException("Not implemented");
        };
    }

    @Override
    public ActionAdvisor recoverActionAdvisor(Spot spot, ActionAdvisorType actionAdvisorType, ActionTweak actionTweak, String actionAdvisorName) {
        return switch (actionAdvisorType) {
            case ML_ACTION -> tryRecoverMlActionAdvisor(spot, actionTweak, actionAdvisorName);
            case CHART_PREFLOP_ACTION -> {
                PreflopActionChart preflopActionChart = preflopActionChartService.findByName(actionAdvisorName);
                preflopActionChart.setActionTweak(actionTweak);
                yield preflopActionChart;
            }
            case CHART_POSTFLOP_ACTION -> {
                PostflopActionChart postflopActionChart = postflopActionChartService.findByName(actionAdvisorName);
                postflopActionChart.setActionTweak(actionTweak);
                yield postflopActionChart;
            }
            case COUNTER_STRATEGY_ACTION -> throw new RuntimeException("Not implemented");
        };
    }

    private ActionAdvisor tryRecoverMlActionAdvisor(Spot troubleSpot, ActionTweak actionTweak, String actionAdvisorName) {
        try {
            return recoverMlActionAdvisor(troubleSpot, actionAdvisorName, actionTweak);
        } catch (PipelineModelRecoverException e) {
            return createActionAdvisorOnTroubleSpot(troubleSpot);
        }
    }

    private AmountAdvisor tryRecoverMlAmountAdvisor(Spot troubleSpot, String amountAdvisorName, AmountTweak amountTweak) {
        try {
            return recoverMlAmountAdvisor(troubleSpot, amountAdvisorName, amountTweak);
        } catch (PipelineModelRecoverException e) {
            return createActionAmountOnTroubleSpot(troubleSpot);
        }
    }

    private ActionAdvisor createActionAdvisorOnTroubleSpot(Spot troubleSpot) {
        return new ActionAdvisor() {
            @Override
            public ActionMap adviceActionMap(Spot spot, PokerGame pokerGame) {
                return troubleSpot.defaultActionMap();
            }
        };
    }

    private AmountAdvisor createActionAmountOnTroubleSpot(Spot troubleSpot) {
        return new AmountAdvisor() {
            @Override
            public int adviceAmount(Spot spot, PokerGame pokerGame) {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                int callAmount = pokerGame.computeCallAmount(hero.getPreflopPosition());
                int potSize = pokerGame.getPotSize();
                return callAmount + (int) (potSize * troubleSpot.defaultDiffAmountByPot());
            }
        };
    }

    private Advisor tryRecoverMlAdvisor(Spot spot, AdvisorInfo advisorInfo) {
        MlActionAdvisor mlActionAdvisor;
        try {
            mlActionAdvisor = recoverMlActionAdvisor(spot, advisorInfo.actionAdvisorName(), advisorInfo.actionTweak());
            MlAmountAdvisor mlAmountAdvisor = recoverMlAmountAdvisor(spot, advisorInfo.amountAdvisorName(), advisorInfo.amountTweak());
            return new MlAdvisor(mlActionAdvisor, mlAmountAdvisor);
        } catch (PipelineModelRecoverException e) {
            logService.log(LogRecordCode.ML_ACTION_PIPELINE_RECOVER_ERROR, Map.of("spot", spot.name(), "exception", e.getMessage()));
            MlAmountAdvisor mlAmountAdvisor;
            try {
                mlAmountAdvisor = recoverMlAmountAdvisor(spot, advisorInfo.amountAdvisorName(), advisorInfo.amountTweak());
                return new SimpleAdvisor(createActionAdvisorOnTroubleSpot(spot), mlAmountAdvisor);
            } catch (PipelineModelRecoverException ex) {
                logService.log(LogRecordCode.ML_AMOUNT_PIPELINE_RECOVER_ERROR, Map.of("spot", spot.name(), "exception", e.getMessage()));
                return new SimpleAdvisor(createActionAdvisorOnTroubleSpot(spot), createActionAmountOnTroubleSpot(spot));
            }
        }
    }

    @Override
    public MlActionAdvisor recoverMlActionAdvisor(Spot spot, String name, ActionTweak actionTweak) throws PipelineModelRecoverException {
        PipelineInfo pipelineInfo = modelService.recoverPipelineModel(name);
        PipelineModel pipelineModel = pipelineInfo.pipelineModel();
        Model model = pipelineInfo.model();
        FeatureSchema featureSchema = model.getFeatureSchema();
        StructType structType = PipelineUtils.createStructType(spot);
        return new MlActionAdvisor(featureSchema, pipelineModel, structType, sparkSession, actionTweak);
    }

    @Override
    public MlAmountAdvisor recoverMlAmountAdvisor(Spot spot, String name, AmountTweak amountTweak) throws PipelineModelRecoverException {
        PipelineInfo pipelineInfo = modelService.recoverPipelineModel(name);
        PipelineModel pipelineModel = pipelineInfo.pipelineModel();
        Model model = pipelineInfo.model();
        FeatureSchema featureSchema = model.getFeatureSchema();
        StructType structType = PipelineUtils.createStructType(spot);
        return new MlAmountAdvisor(featureSchema, pipelineModel, structType, sparkSession, amountTweak);
    }

    @Override
    public Advice advice(PokerHand pokerHand, String solutionName, SpecialSpotTweakCollection specialSpotTweakCollection) {
        BoardSize boardSize = BoardSize.of(pokerHand.playerInitialStates().size());
        PokerGame pokerGame = PokerGame.create(pokerHand, playerName -> OpponentType.OVERALL).orElseThrow();
        pokerHandReplayer.replayBestEffort(pokerGame, pokerHand);
        return advice(pokerGame, solutionName, specialSpotTweakCollection);
    }

    @Override
    public Advice advice(PokerGame pokerGame, String solutionName, SpecialSpotTweakCollection specialSpotTweakCollection) {
        return Optional.ofNullable(getSolution(solutionName))
                .map(solution -> {
                    Advice advice = solution.advice(pokerGame, specialSpotTweakCollection);
                    return AdviceCorrector.correct(advice, pokerGame);
                })
                .orElseThrow(() -> new RuntimeException("Solution " + solutionName + " is not found in pool. Pool contains: " + String.join(", ", solutionPool.keySet())));
    }

    @Override
    public List<AdvisedGame> advices(List<PokerGame> pokerGames, String solutionName, String specialSpotCollectionName) {
        SpecialSpotTweakCollection specialSpotTweakCollection = specialSpotTweakService.getByName(specialSpotCollectionName);
        return Optional.ofNullable(getSolution(solutionName))
                .map(solution -> solution.advices(pokerGames, specialSpotTweakCollection)
                        .stream().map(advisedGame -> {
                            Advice advice = advisedGame.advice();
                            PokerGame pokerGame = advisedGame.pokerGame();
                            Advice corrected = AdviceCorrector.correct(advice, pokerGame);
                            return new AdvisedGame(pokerGame, corrected);
                        }).collect(Collectors.toList()))
                .orElseThrow(() -> new RuntimeException("Solution " + solutionName + " is not found in pool. Pool contains: " + String.join(", ", solutionPool.keySet())));
    }

    @Override
    public Solution getSolution(String solutionName) {
        return solutionPool.get(solutionName);
    }

    @Override
    public Advice adviseCS(PokerHand pokerHand) {
        BoardSize boardSize = BoardSize.of(pokerHand.playerInitialStates().size());
        Map<String, Double> vpips = pokerHand.playerInitialStates().entrySet().stream().collect(Collectors.toMap(e -> e.getValue().playerName(), e -> e.getValue().vpip()));
        PokerGame pokerGame = PokerGame.create(pokerHand, playerName -> opponentService.resolveOpponentType(playerName, boardSize, vpips.get(playerName))).orElseThrow();
        pokerHandReplayer.replayBestEffort(pokerGame, pokerHand);
        return adviseCS(pokerGame, CounterStrategyType.NO_ALLIN_REDUCING_RAISE_SIZE);
    }


    private record RangeInvestments(PreflopRange preflopRange, double investments, double pot) {
    }

    @Override
    public Advice adviseCS(PokerGame pokerGame, CounterStrategyType counterStrategyType) {

        logService.log(LogRecordCode.GENERAL, Map.of("pg", pokerGame.toString()));

        long started = System.currentTimeMillis();
        try {
            Map<String, HoldemStatistics> statisticsMap = pokerGame.getPlayersByPosition().values().stream()
                    .collect(Collectors.toMap(PokerPlayer::getPlayerName, pokerPlayer -> statisticsService.getStatistics(pokerPlayer.getOpponentType())));
            Map<PokerGameEdge, PokerGameNode> treeMap = gameTreeGenerator.generate(pokerGame, statisticsMap);

            Optional<Map.Entry<PokerGameEdge, PokerGameNode>> opt = treeMap.entrySet().stream()
                    .filter(e -> e.getKey().decision().action() == Action.F)
                    .findFirst();
            treeMap.entrySet().removeIf(e -> e.getKey().decision().action() == Action.F);

            int bbAmount = pokerGame.getBbAmount();
            Map<PokerGameEdge, EnumMap<PreflopPosition, RangeInvestments>> combinedMap = treeMap.entrySet().parallelStream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            e -> {
                                Map<PokerGameEdge, PokerGameNode> edgeTreeMap = e.getValue().treeMap();
                                if (edgeTreeMap.isEmpty()) {
                                    double weight = e.getKey().weight();
                                    PokerGame completedPokerGame = e.getValue().pokerGame();
                                    return completedPokerGame.getAllPlayersFilteringFolded()
                                            .stream()
                                            .collect(Collectors.toMap(PokerPlayer::getPreflopPosition, pokerPlayer -> {
                                                double investments = pokerPlayer.getTotalInvestedAmount() * weight;
                                                PreflopRange range = /*pokerPlayer.isFolded() ? PreflopRange.EMPTY : */getPreflopRange(bbAmount, statisticsMap, pokerPlayer, weight);
                                                return new RangeInvestments(range, investments, completedPokerGame.getPotSize() * weight);
                                            }, (o1, o2) -> {
                                                throw new RuntimeException();
                                            }, () -> new EnumMap<>(PreflopPosition.class)));
                                } else {
                                    List<WeightedGame> weightedGames = findCompletedWeightedGames(edgeTreeMap).toList();
                                    return weightedGames
                                            .parallelStream()
                                            .flatMap(weightedGame -> {
                                                double weight = weightedGame.weight;
                                                PokerGame pg = weightedGame.pokerGame;
                                                return pg.getAllPlayersFilteringFolded()
                                                        .stream()
                                                        .map(pokerPlayer -> {
                                                            PreflopPosition position = pokerPlayer.getPreflopPosition();
                                                            double investments = pokerPlayer.getTotalInvestedAmount() * weight;
                                                            PreflopRange range = /*pokerPlayer.isFolded() ? PreflopRange.EMPTY :*/ getPreflopRange(bbAmount, statisticsMap, pokerPlayer, weight);
                                                            return new AbstractMap.SimpleEntry<>(position, new RangeInvestments(range, investments, pg.getPotSize() * weight));
                                                        });
                                            })
                                            .collect(Collectors.toMap(Map.Entry::getKey,
                                                    Map.Entry::getValue,
                                                    (a, b) -> new RangeInvestments(a.preflopRange().plus(b.preflopRange()), a.investments() + b.investments(), a.pot() + b.pot()),
                                                    () -> new EnumMap<>(PreflopPosition.class)));
                                }
                            },
                            (m1, m2) -> {
                                throw new RuntimeException("Duplicate keys not allowed");
                            },
                            HashMap::new
                    ));

            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
            PreflopPosition heroPosition = hero.getPreflopPosition();

            EnumSet<PreflopPosition> playerPositions = pokerGame.getAllPlayersFilteringFolded().stream().map(PokerPlayer::getPreflopPosition).collect(Collectors.toCollection(() -> EnumSet.noneOf(PreflopPosition.class)));
            combinedMap.forEach((k, v) -> v.entrySet().removeIf(e -> !playerPositions.contains(e.getKey())));

            Street street = pokerGame.getState().getStreet();

            Map<PokerGameEdge, EnumMap<PreflopPosition, Double>> profits = combinedMap.entrySet().parallelStream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                        PokerGameEdge edge = e.getKey();
                        double weight = edge.weight();
                        EnumMap<PreflopPosition, RangeInvestments> ri = e.getValue();
                        EnumMap<PreflopPosition, PreflopRange> oppRangeMap = ri.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().preflopRange, (o, o2) -> o, () -> new EnumMap<>(PreflopPosition.class)));
                        EnumMap<PreflopPosition, Double> investmentsMap = ri.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().investments / weight, (o, o2) -> o, () -> new EnumMap<>(PreflopPosition.class)));
                        EnumMap<PreflopPosition, Double> potMap = ri.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().pot / weight, (o, o2) -> o, () -> new EnumMap<>(PreflopPosition.class)));
                        double totalPot = investmentsMap.values().stream().mapToDouble(d -> d).sum();
                        oppRangeMap.remove(heroPosition);
                        EnumMap<PreflopPosition, Double> winProbabilitiesMap = street == Street.PF ? computeWinProbabilities(heroPosition, hero.getCardPair(), oppRangeMap) :
                                computePostflopWinProbabilities(heroPosition, hero.getCardPair(), oppRangeMap, pokerGame.getBoardCards());

                        Map<PreflopPosition, ProfitLoss> profitLossMap = winProbabilitiesMap.entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, v -> {
                                    double winrate = v.getValue();
                                    double investments = investmentsMap.get(v.getKey());
                                    double pot = potMap.get(v.getKey());
                                    return new ProfitLoss(totalPot * winrate, investments/* * (1 - winrate)*/);
                                }));

//                        Map<PreflopPosition, ProfitLoss> pl = profitLossMap.entrySet().stream()
//                                .collect(Collectors.toMap(Map.Entry::getKey, v -> {
//                                    PreflopPosition preflopPosition = v.getKey();
//
//                                    double profit =
//                                            profitLossMap.entrySet().stream()
//                                                    .mapToDouble(val -> {
//                                                        if (val.getKey() == preflopPosition) {
//                                                            return val.getValue().profit;
//                                                        } else {
//                                                            return val.getValue().loss;
//                                                        }
//                                                    })
//                                                    .sum();
//
//                                    double loss =
//                                            profitLossMap.entrySet().stream()
//                                                    .mapToDouble(val -> {
//                                                        if (val.getKey() == preflopPosition) {
//                                                            return val.getValue().loss;
//                                                        } else {
//                                                            return val.getValue().profit;
//                                                        }
//                                                    })
//                                                    .sum();
//
//                                    return new ProfitLoss(profit, loss);
//                                }));

                        return profitLossMap.entrySet().stream()
                                .collect(Collectors.toMap(Map.Entry::getKey,
                                        ee -> ee.getValue().diff(),
                                        (o, o2) -> o,
                                        () -> new EnumMap<>(PreflopPosition.class)));
                    }));
            opt.ifPresent(e -> {
                EnumMap<PreflopPosition, Double> m = new EnumMap<>(PreflopPosition.class);
                m.put(heroPosition, -(double) hero.getTotalInvestedAmount());
                profits.put(e.getKey(), m);
            });

            Decision decision = makeDecision(heroPosition, profits, counterStrategyType);
            if (pokerGame.getAllPlayersFilteringFolded().size() > 2) {
                logService.log(LogRecordCode.CS_TIME_TAKEN_MULTIWAY, Map.of("time_taken", String.valueOf(System.currentTimeMillis() - started), "pg", pokerGame.toString()));
            } else {
                logService.log(LogRecordCode.CS_TIME_TAKEN, Map.of("time_taken", String.valueOf(System.currentTimeMillis() - started)));
            }
            return new Advice(decision.action(), decision.amount());
        } catch (Exception e) {
            logService.log(LogRecordCode.CS_ERROR, e);
            throw new RuntimeException(e);
        }
    }

    private Decision makeDecision(PreflopPosition heroPosition, Map<PokerGameEdge, EnumMap<PreflopPosition, Double>> profits, CounterStrategyType counterStrategyType) {
        return switch (counterStrategyType) {
            case NO_TWEAK -> makeInitialDecision(heroPosition, profits);
            case NO_ALLIN_REDUCING_RAISE_SIZE -> makeDecisionRemovingAllinAndGetSmallerRaiseSize(heroPosition, profits);
            case NO_ALLIN_MAX_RAISE_SIZE -> makeDecisionRemovingAllinAndGetMaxRaiseSize(heroPosition, profits);
        };
    }

    private Decision makeInitialDecision(PreflopPosition heroPosition, Map<PokerGameEdge, EnumMap<PreflopPosition, Double>> profits) {
        return profits.entrySet().stream()
                .max(Comparator.comparingDouble(value -> value.getValue().get(heroPosition)))
                .map(v -> v.getKey().decision())
                .orElseThrow();
    }

    private Decision makeDecisionRemovingAllinAndGetSmallerRaiseSize(PreflopPosition heroPosition, Map<PokerGameEdge, EnumMap<PreflopPosition, Double>> profits) {
        Decision initialDecision = makeInitialDecision(heroPosition, profits);
        if (initialDecision.action() == Action.A) {
            List<Decision> positiveProfitRaisingDecisions = profits.entrySet().stream()
                    .filter(e -> e.getValue().get(heroPosition) > 0)
                    .map(e -> e.getKey().decision())
                    .filter(decision -> decision.action() == Action.R)
                    .sorted(Comparator.comparingDouble(Decision::amount))
                    .toList();
            int size = positiveProfitRaisingDecisions.size();
            if (size >= 2) {
                return positiveProfitRaisingDecisions.get(1);
            } else if (size == 1) {
                return positiveProfitRaisingDecisions.get(0);
            } else {
                return initialDecision;
            }
        } else {
            return initialDecision;
        }
    }

    private Decision makeDecisionRemovingAllinAndGetMaxRaiseSize(PreflopPosition heroPosition, Map<PokerGameEdge, EnumMap<PreflopPosition, Double>> profits) {
        Decision initialDecision = makeInitialDecision(heroPosition, profits);
        if (initialDecision.action() == Action.A) {
            List<Decision> positiveProfitRaisingDecisions = profits.entrySet().stream()
                    .filter(e -> e.getValue().get(heroPosition) > 0)
                    .map(e -> e.getKey().decision())
                    .filter(decision -> decision.action() == Action.R)
                    .sorted(Comparator.comparingDouble(Decision::amount))
                    .toList();
            if (!positiveProfitRaisingDecisions.isEmpty()) {
                return positiveProfitRaisingDecisions.get(positiveProfitRaisingDecisions.size() - 1);
            } else {
                return initialDecision;
            }
        } else {
            return initialDecision;
        }
    }

    private PreflopRange getPreflopRange(int bbAmount, Map<String, HoldemStatistics> statisticsMap, PokerPlayer pokerPlayer, double weight) {
        List<MoveStatistics> moveStatistics = pokerPlayer.getMoveStatistics(Street.PF);
        MoveStatistics lastPfMove = moveStatistics.get(moveStatistics.size() - 1);
        Branch inBranch = lastPfMove.inBranch();
        Branch outBranch = lastPfMove.outBranch();
        PokerSituation pokerSituation = lastPfMove.prevPokerSituation();
        HoldemStatistics statistics = statisticsMap.get(pokerPlayer.getPlayerName());
        PreflopSpot preflopSpot = PreflopSpot.from(pokerSituation, bbAmount, lastPfMove.stackStub());
        List<Branch> preflopSpotInBranches = statistics.getPreflopSpotInBranches(preflopSpot);
        Branch closestInBranch = inBranch.findClosest(preflopSpotInBranches);
        PreflopStatisticsKey preflopStatisticsKey = new PreflopStatisticsKey(preflopSpot, closestInBranch);
        List<Branch> preflopSpotOutBranches = statistics.getPreflopSpotOutBranches(preflopStatisticsKey);
        Branch closestOutBranch = outBranch.findClosest(preflopSpotOutBranches);
        PreflopBranchStatistics preflopBranchStatistics = statistics.preflopStatistics().get(preflopStatisticsKey).get(closestOutBranch);
        return preflopBranchStatistics.preflopRange()
                .copy()
                .multiply(weight);
    }

    private Stream<PokerGame> findCompletedGames(Map<PokerGameEdge, PokerGameNode> treeMap) {
        return treeMap.entrySet().parallelStream().flatMap(e -> {
            PokerGameNode pokerGameNode = e.getValue();
            if (pokerGameNode.isEmpty()) {
                return Stream.of(pokerGameNode.pokerGame());
            } else {
                return findCompletedGames(pokerGameNode.treeMap());
            }
        });
    }


    private record WeightedGame(PokerGame pokerGame, double weight) {
    }

    private Stream<WeightedGame> findCompletedWeightedGames(Map<PokerGameEdge, PokerGameNode> treeMap) {
        return treeMap.entrySet().parallelStream().flatMap(e -> {
            PokerGameNode pokerGameNode = e.getValue();
            if (pokerGameNode.isEmpty()) {
                double weight = e.getKey().weight();
                return Stream.of(new WeightedGame(pokerGameNode.pokerGame(), weight));
            } else {
                return findCompletedWeightedGames(pokerGameNode.treeMap());
            }
        });
    }

    private EnumMap<PreflopPosition, Double> computeWinProbabilities(PreflopPosition heroPosition, CardPair cardPair, Map<PreflopPosition, PreflopRange> oppRangeMap) {
        return HoldemHandStrengthCalculator.winRate(heroPosition, cardPair, oppRangeMap, 1000);
    }

    private EnumMap<PreflopPosition, Double> computePostflopWinProbabilities(PreflopPosition heroPosition, CardPair cardPair, Map<PreflopPosition, PreflopRange> oppRangeMap, EnumSet<Card> boardCards) {
        return HoldemHandStrengthCalculator.winRate(heroPosition, cardPair, oppRangeMap, boardCards, 1000);
    }

}
