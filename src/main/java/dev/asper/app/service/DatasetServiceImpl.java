package dev.asper.app.service;

import dev.asper.app.component.ClickhouseCollector;
import dev.asper.app.component.PlayersRepository;
import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.app.component.ClickhouseQueries;
import dev.asper.clickhouse.OpponentType;
import dev.asper.clickhouse.PlayerStat;
import dev.asper.clickhouse.PokerHandWithMiningType;
import dev.asper.common.feature.Descriptor;
import dev.asper.common.feature.Feature;
import dev.asper.poker.engine.*;
import dev.asper.poker.engine.spot.Spots;
import dev.asper.poker.enums.BoardSize;
import dev.asper.poker.enums.CompetitionType;
import dev.asper.poker.enums.MiningType;
import dev.asper.poker.enums.Street;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DatasetServiceImpl implements DatasetService {
    public static final int THREAD_COUNT = 30;
    public static final int BATCH_SIZE = 3000000;
    private final ClickhouseQueries clickhouseQueries;
    private final ClickhouseCollector clickhouseCollector;
    private final LogService logService;
    private final PokerEngine pokerEngine;
    private final PlayersRepository playersRepository;

    @Autowired
    public DatasetServiceImpl(ClickhouseQueries clickhouseQueries,
                              ClickhouseCollector clickhouseCollector,
                              LogService logService,
                              PokerEngine pokerEngine,
                              PlayersRepository playersRepository) {
        this.clickhouseQueries = clickhouseQueries;
        this.clickhouseCollector = clickhouseCollector;
        this.logService = logService;
        this.pokerEngine = pokerEngine;
        this.playersRepository = playersRepository;
    }

    @Override
    public int createDatasetsAsync(String pokerGamesSchemaName, String pokerGamesTableName, String datasetsSchemaName) {
        playersRepository.init();
        new Thread(() -> createDatasets(pokerGamesSchemaName, pokerGamesTableName, datasetsSchemaName)).start();
        return 0;
    }

    @Override
    public int createHoldemStatDatasetsAsync(String pokerGamesSchemaName, String pokerGamesTableName, String datasetsSchemaName) {
        playersRepository.init();
        new Thread(() -> createHoldemStatDatasets(pokerGamesSchemaName, pokerGamesTableName, datasetsSchemaName)).start();
        return 0;
    }

    private final Map<Street, List<Feature<?, PokerGame>>> holdemStatFeatures = Map.of(
            Street.PF, List.of(PokerFeatures.heroName, PokerFeatures.heroEffStackType, PokerFeatures.prevBranch),
            Street.FL, List.of(PokerFeatures.heroName, PokerFeatures.heroEffStackType, PokerFeatures.prevBranch, PokerFeatures.flHoldingBoardFlush, PokerFeatures.flHoldingBoardStraight, PokerFeatures.flHoldingBoardPair),
            Street.TN, List.of(PokerFeatures.heroName, PokerFeatures.heroEffStackType, PokerFeatures.prevBranch, PokerFeatures.tnHoldingBoardFlush, PokerFeatures.tnHoldingBoardStraight, PokerFeatures.tnHoldingBoardPair),
            Street.RV, List.of(PokerFeatures.heroName, PokerFeatures.heroEffStackType, PokerFeatures.prevBranch, PokerFeatures.rvHoldingBoardFlush, PokerFeatures.rvHoldingBoardStraight, PokerFeatures.rvHoldingBoardPair)
    );

    private void createHoldemStatDatasets(String pokerGamesSchemaName, String pokerGamesTableName, String datasetsSchemaName) {
        List<PokerHandWithMiningType> pokerHandWithMiningTypes;
        int offset = 0;
        while (!(pokerHandWithMiningTypes = clickhouseQueries.fetchPokerHands(pokerGamesSchemaName, pokerGamesTableName, offset += BATCH_SIZE, BATCH_SIZE)).isEmpty()) {
            logService.log(LogRecordCode.FETCH_POKER_GAMES_CLOCK, Map.of("fetched", String.valueOf(pokerHandWithMiningTypes.size())));
            int batchSize = pokerHandWithMiningTypes.size() > 100000 ? pokerHandWithMiningTypes.size() / THREAD_COUNT : pokerHandWithMiningTypes.size();
            ListUtils.partition(pokerHandWithMiningTypes, batchSize).parallelStream().forEach(phmts -> {
                Map<PokerSituation, List<List<Descriptor<?>>>> batch = collectHoldemStatsBatch(phmts);
                clickhouseCollector.save(datasetsSchemaName, holdemStatFeatures, batch);
            });
        }
    }

    private void createDatasets(String pokerGamesSchemaName, String pokerGamesTableName, String datasetsSchemaName) {
        List<PokerHandWithMiningType> pokerHandWithMiningTypes;
        int offset = 0;
        while (!(pokerHandWithMiningTypes = clickhouseQueries.fetchPokerHands(pokerGamesSchemaName, pokerGamesTableName, offset += BATCH_SIZE, BATCH_SIZE)).isEmpty()) {
            logService.log(LogRecordCode.FETCH_POKER_GAMES_CLOCK, Map.of("fetched", String.valueOf(pokerHandWithMiningTypes.size())));
            int batchSize = pokerHandWithMiningTypes.size() > 100000 ? pokerHandWithMiningTypes.size() / THREAD_COUNT : pokerHandWithMiningTypes.size();
            List<Map<Pair<PokerSituation, String>, List<List<Descriptor<?>>>>> maps = ListUtils.partition(pokerHandWithMiningTypes, batchSize)
                    .parallelStream().map(this::collectDatasetsBatch).toList();
            Map<Pair<PokerSituation, String>, List<List<Descriptor<?>>>> total = maps.stream()
                    .flatMap(m -> m.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue,
                            (lists, lists2) -> {
                                lists.addAll(lists2);
                                return lists;
                            }));
            clickhouseCollector.save(datasetsSchemaName, total);
        }
    }


    private Map<Pair<PokerSituation, String>, List<List<Descriptor<?>>>> collectDatasetsBatch(List<PokerHandWithMiningType> pokerHandWithMiningTypes) {
        Map<Pair<PokerSituation, String>, List<List<Descriptor<?>>>> datasetMap = new HashMap<>();
        pokerHandWithMiningTypes.forEach(pokerHandWithMiningType -> {
            PokerHand pokerHand = pokerHandWithMiningType.pokerHand();
            MiningType miningType = pokerHandWithMiningType.miningType();
            if (pokerHand.containsOpenCards()) {
                replayDatasetCollect(datasetMap, pokerHand, miningType);
            }
        });
        return datasetMap;
    }

    private Map<PokerSituation, List<List<Descriptor<?>>>> collectHoldemStatsBatch(List<PokerHandWithMiningType> pokerHandWithMiningTypes) {
        Map<PokerSituation, List<List<Descriptor<?>>>> datasetMap = new HashMap<>();
        pokerHandWithMiningTypes.forEach(pokerHandWithMiningType -> {
            replayHoldemStatsCollect(datasetMap, pokerHandWithMiningType.pokerHand());
        });
        return datasetMap;
    }

    private void replayDatasetCollect(Map<Pair<PokerSituation, String>, List<List<Descriptor<?>>>> datasetMap, PokerHand pokerHand, MiningType miningType) {
        BoardSize boardSize = BoardSize.of(pokerHand.playerInitialStates().size());
        Optional<PokerGame> pokerGame = PokerGame.create(pokerHand, playerName -> playersRepository.resolveNarrowOpponentType(playerName, boardSize));
        CompetitionType competitionType = pokerHand.competitionType();
        PokerGameScenario scenario = PokerGameScenario.from(pokerHand);
        try {
            pokerGame.ifPresent(pg -> {
                scenario.onBegin((step, ph) -> {
                    pokerEngine.apply(pg, step.event(), step.eventIdx(), ph);
                });
                scenario.onEachStep((step, ph) -> {
                    PokerSituation prevState = pg.getState();
                    if (!PokerSituation.intermediateSituations.contains(prevState) && pg.getNextPlayerOrThrow().isPocketCardsOpen()) {
                        PokerPlayer hero = pg.getNextPlayerOrThrow();
                        String spotStr = Spots.strFrom(pg);
                        List<Descriptor<?>> descriptors = new ArrayList<>();
                        prevState.getServiceFeatures().forEach(feature -> descriptors.add(feature.descriptor(pg)));
                        prevState.getFeatures().forEach(feature -> descriptors.add(feature.descriptor(pg)));
                        pokerEngine.apply(pg, step.event(), step.eventIdx(), ph);
                        PokerSituation nextState = pg.getState();
                        nextState.getLabels().forEach(feature -> descriptors.add(feature.descriptor(pg)));
                        double heroProfitRaw = pokerHand.profitByPosition().get(pg.getCurrentPlayerOrThrow().getPreflopPosition());
                        double profit = heroProfitRaw / (double) pg.getBbAmount();
                        descriptors.add(new Descriptor<>(PokerFeatureName.MINING_TYPE, miningType));
                        descriptors.add(new Descriptor<>(PokerFeatureName.PROFIT_BB, profit));
                        descriptors.add(new Descriptor<>(PokerFeatureName.COMPETITION_TYPE, competitionType));
                        PlayerStat playerStat = playersRepository.getPlayerStat(hero.getPlayerName(), pg.getBoardSize());
                        List<OpponentType> oppTypes = Arrays.stream(OpponentType.values())
                                .filter(opponentType -> opponentType.test(playerStat))
                                .toList();
                        descriptors.add(new Descriptor<>(PokerFeatureName.OPP_TYPES, oppTypes));
                        if (descriptors.stream().allMatch(Descriptor::isValid)) {
                            datasetMap.computeIfAbsent(Pair.of(prevState, spotStr), s -> new ArrayList<>()).add(descriptors);
                        }
                    } else {
                        pokerEngine.apply(pg, step.event(), step.eventIdx(), ph);
                    }
                });
            });
        } catch (Exception e) {
            logService.log(LogRecordCode.REPLAY_ERROR, Map.of("replay error", e.getMessage(), "pokerHand", pokerHand.toString()));
        }
    }

    private void replayHoldemStatsCollect(Map<PokerSituation, List<List<Descriptor<?>>>> datasetMap, PokerHand pokerHand) {
        BoardSize boardSize = BoardSize.of(pokerHand.playerInitialStates().size());
        Optional<PokerGame> pokerGame = PokerGame.create(pokerHand, playerName -> playersRepository.resolveNarrowOpponentType(playerName, boardSize));
        CompetitionType competitionType = pokerHand.competitionType();
        PokerGameScenario scenario = PokerGameScenario.from(pokerHand);
        try {
            pokerGame.ifPresent(pg -> {
                scenario.onBegin((step, ph) -> {
                    pokerEngine.apply(pg, step.event(), step.eventIdx(), ph);
                });
                scenario.onEachStep((step, ph) -> {
                    PokerSituation prevState = pg.getState();
                    if (!PokerSituation.intermediateSituations.contains(prevState)) {
                        PokerPlayer hero = pg.getNextPlayerOrThrow();
                        Street street = prevState.getStreet();
                        List<Feature<?, PokerGame>> features = holdemStatFeatures.get(street);
                        List<Descriptor<?>> descriptors = new ArrayList<>();
                        features.forEach(feature -> descriptors.add(feature.descriptor(pg)));
                        pokerEngine.apply(pg, step.event(), step.eventIdx(), ph);
                        descriptors.add(PokerFeatures.heroBranch.descriptor(pg));
                        double heroProfitRaw = pokerHand.profitByPosition().get(pg.getCurrentPlayerOrThrow().getPreflopPosition());
                        double profit = heroProfitRaw / (double) pg.getBbAmount();
                        descriptors.add(new Descriptor<>(PokerFeatureName.PROFIT_BB, profit));
                        descriptors.add(new Descriptor<>(PokerFeatureName.COMPETITION_TYPE, competitionType));
                        PlayerStat playerStat = playersRepository.getPlayerStat(hero.getPlayerName(), pg.getBoardSize());
                        List<OpponentType> oppTypes = Arrays.stream(OpponentType.values())
                                .filter(opponentType -> opponentType.test(playerStat))
                                .toList();
                        descriptors.add(new Descriptor<>(PokerFeatureName.OPP_TYPES, oppTypes));
                        if (descriptors.stream().allMatch(Descriptor::isValid)) {
                            datasetMap.computeIfAbsent(prevState, s -> new ArrayList<>()).add(descriptors);
                        }
                    } else {
                        pokerEngine.apply(pg, step.event(), step.eventIdx(), ph);
                    }
                });
            });
        } catch (Exception e) {
            logService.log(LogRecordCode.REPLAY_ERROR, Map.of("replay error", e.getMessage(), "pokerHand", pokerHand.toString()));
        }
    }

}
