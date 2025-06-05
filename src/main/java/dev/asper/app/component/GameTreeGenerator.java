package dev.asper.app.component;

import dev.asper.advice.AdviceCorrector;
import dev.asper.advice.Decision;
import dev.asper.poker.engine.*;
import dev.asper.poker.engine.spot.PreflopSpot;
import dev.asper.poker.enums.Action;
import dev.asper.poker.enums.PokerGameEventType;
import dev.asper.poker.enums.PreflopPosition;
import dev.asper.poker.enums.Street;
import dev.asper.poker.generate.PokerGameEdge;
import dev.asper.poker.generate.PokerGameNode;
import dev.asper.poker.holding.BoardFlush;
import dev.asper.poker.holding.BoardPair;
import dev.asper.poker.holding.BoardStatus;
import dev.asper.poker.holding.BoardStraight;
import dev.asper.stat.*;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameTreeGenerator {
    private final PokerEngine pokerEngine;

    @Autowired
    public GameTreeGenerator(PokerEngine pokerEngine) {
        this.pokerEngine = pokerEngine;
    }

    public Map<PokerGameEdge, PokerGameNode> generate(PokerGame pokerGame, Map<String, HoldemStatistics> statisticsMap) {
        PreflopPosition heroPosition = pokerGame.getNextPlayerOrThrow().getPreflopPosition();
        if (pokerGame.getState().getStreet() == Street.PF) {
            return generatePf(pokerGame, 1., statisticsMap, heroPosition);
        } else {
            BoardStatus boardStatus = getBoardStatus(pokerGame);
            BoardFlush boardFlush = boardStatus.boardFlush();
            BoardStraight boardStraight = boardStatus.boardStraight();
            BoardPair boardPair = boardStatus.boardPair();
            return generatePrecisePostflop(pokerGame, 1., boardFlush, boardStraight, boardPair, statisticsMap, heroPosition);
        }
    }

    private BoardStatus getBoardStatus(PokerGame pokerGame) {
        return switch (pokerGame.getState().getStreet()) {
            case FL -> pokerGame.getFlBoardStatus();
            case TN -> pokerGame.getTnBoardStatus();
            case RV -> pokerGame.getRvBoardStatus();
            default -> throw new IllegalStateException("Unexpected value: " + pokerGame.getState().getStreet());
        };
    }

    private Map<PokerGameEdge, PokerGameNode> generatePf(PokerGame pokerGame,
                                                         double srcBranchProbability,
                                                         Map<String, HoldemStatistics> statisticsMap,
                                                         PreflopPosition srcHeroPosition) {
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        HoldemStatistics statistics = statisticsMap.get(hero.getPlayerName());
        PreflopPosition heroPosition = hero.getPreflopPosition();
        PokerSituation pokerSituation = pokerGame.getState();
        Street street = pokerSituation.getStreet();
        List<MoveStatistics> moveStats = pokerGame.getMoveStatistics();
        PreflopSpot preflopSpot = PreflopSpot.from(pokerGame);
        List<Branch> preflopSpotInBranches = statistics.getPreflopSpotInBranches(preflopSpot);
        Branch inBranch = moveStats.isEmpty() ? Branch.NONE : moveStats.get(moveStats.size() - 1).outBranch().findClosest(preflopSpotInBranches);
        PreflopStatisticsKey preflopStatisticsKey = new PreflopStatisticsKey(preflopSpot, inBranch);
        int potSize = pokerGame.getPotSize();
        int callAmount = pokerGame.computeCallAmount(heroPosition);

        Map<Branch, PreflopBranchStatistics> preflopStatistics = statistics.getPreflopStatistics(preflopStatisticsKey);

        List<Map.Entry<Branch, PreflopBranchStatistics>> filteredBranches;
        if (heroPosition == srcHeroPosition) {
            filteredBranches = preflopStatistics.entrySet().stream().toList();
        } else {
            Optional<Map.Entry<Branch, PreflopBranchStatistics>> maxProbabilityRaiseBranch = preflopStatistics.entrySet().stream()
                    .filter(e -> e.getKey().getAction() == Action.R)
                    .max(Comparator.comparingDouble(e -> e.getValue().branchProbability()));
            filteredBranches = preflopStatistics.entrySet().stream()
                    .filter(e -> e.getKey().getAction() != Action.R)
                    .collect(Collectors.toList());
            maxProbabilityRaiseBranch.ifPresent(filteredBranches::add);
        }

        double sum = filteredBranches.stream().mapToDouble(v -> v.getValue().branchProbability()).sum();
        return filteredBranches.parallelStream()
                .map(e -> {
                    PreflopBranchStatistics preflopBranchStatistics = e.getValue();
                    Branch outBranch = e.getKey();
                    //TODO remove either Decision or Advice
                    Decision correctedDecision = AdviceCorrector.correct(outBranch.toDecision(potSize, callAmount, hero.getStackStub()).toAdvice(), pokerGame).toDecision();
                    double branchProbabilityMultiplied = preflopBranchStatistics.branchProbability() / sum * srcBranchProbability;
                    return new PokerGameEdge(correctedDecision, branchProbabilityMultiplied);
                })
                .collect(Collectors.groupingBy(PokerGameEdge::decision,
                        Collectors.collectingAndThen(Collectors.toList(), edges -> {
                            PokerGameEdge pokerGameEdge = edges.get(0);
                            double totalWeight = edges.size() == 1 ? pokerGameEdge.weight() : edges.stream().mapToDouble(PokerGameEdge::weight).sum();
                            return new PokerGameEdge(pokerGameEdge.decision(), totalWeight);
                        })))
                .values().parallelStream()
                .map(pokerGameEdge -> {
                    List<Move> moves = new ArrayList<>();
                    moves.add(new Move(heroPosition, pokerGameEdge.decision()));
                    PokerGameEventType moveType = street.getPokerGameEventType();
                    PokerHand ph = PokerHand.createForMove(moveType, moves);
                    PokerGame copy = pokerGame.copy();
                    pokerEngine.apply(copy, moveType, 0, ph);
                    PokerSituation copyPokerSituation = copy.getState();
                    double branchProbability = pokerGameEdge.weight();
                    if (copyPokerSituation == PokerSituation.PF_COMPLETED) {
                        if (copy.isCompleted()) {
                            return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                        } else {
                            PokerGame next = copy.copy();
                            PokerHand completed = PokerHand.createForCards(PokerGameEventType.FL_CARDS, null, null, null);
                            pokerEngine.apply(next, PokerGameEventType.FL_CARDS, 0, completed);
                            PokerGameNode pokerGameNode = new PokerGameNode(next, Collections.emptyMap());
//                            PokerGameNode pokerGameNode = new PokerGameNode(next, generateRoughPostflop(next, branchProbability, statisticsMap, srcHeroPosition));
                            return Pair.of(pokerGameEdge, pokerGameNode);
                        }
                    } else {
                        PokerGameNode pokerGameNode = new PokerGameNode(copy, generatePf(copy, branchProbability, statisticsMap, srcHeroPosition));
                        return Pair.of(pokerGameEdge, pokerGameNode);
                    }
                }).toList().stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    private Map<PokerGameEdge, PokerGameNode> generatePrecisePostflop(PokerGame pokerGame,
                                                                      double srcBranchProbability,
                                                                      BoardFlush boardFlush,
                                                                      BoardStraight boardStraight,
                                                                      BoardPair boardPair,
                                                                      Map<String, HoldemStatistics> statisticsMap,
                                                                      PreflopPosition srcHeroPosition) {
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        HoldemStatistics statistics = statisticsMap.get(hero.getPlayerName());
        PreflopPosition heroPosition = hero.getPreflopPosition();
        PokerSituation pokerSituation = pokerGame.getState();
        Street street = pokerGame.getState().getStreet();
        List<MoveStatistics> moveStats = pokerGame.getMoveStatistics();
        Branch inBranch = moveStats.isEmpty() ? Branch.NONE :
                moveStats.get(moveStats.size() - 1).outBranch()
                        .findClosest(statistics.getPostflopPokerSituationBoardInBranches(new PostflopPokerSituationBoardKey(pokerSituation, boardFlush, boardStraight, boardPair)));
        PostflopBoardStatisticsKey postflopBoardStatisticsKey = new PostflopBoardStatisticsKey(pokerSituation, inBranch, boardFlush, boardStraight, boardPair);
        EnumMap<Branch, Double> branchWeights = statistics.findBranchWeights(postflopBoardStatisticsKey);
        if (branchWeights.isEmpty()) {
            return generateRoughPostflop(pokerGame, srcBranchProbability, statisticsMap, srcHeroPosition);
        } else {
            List<Map.Entry<Branch, Double>> filteredBranches = branchWeights.entrySet().stream().toList();
//            if (heroPosition == srcHeroPosition) {
//                filteredBranches = branchWeights.entrySet().stream().toList();
//            } else {
//                Optional<Map.Entry<Branch, Double>> maxProbabilityRaiseBranch = branchWeights.entrySet().stream()
//                        .filter(e -> e.getKey().getAction() == Action.R)
//                        .max(Comparator.comparingDouble(Map.Entry::getValue));
//                filteredBranches = branchWeights.entrySet().stream()
//                        .filter(e -> e.getKey().getAction() != Action.R)
//                        .collect(Collectors.toList());
//                maxProbabilityRaiseBranch.ifPresent(filteredBranches::add);
//            }
            double sum = filteredBranches.stream().mapToDouble(Map.Entry::getValue).sum();
            return filteredBranches.parallelStream()
                    .map(e -> {
                        double branchProbability = e.getValue();
                        Branch decisionBranch = e.getKey();
                        //TODO remove either Decision or Advice
                        Decision correctedDecision = AdviceCorrector.correct(decisionBranch.toDecision(pokerGame.getPotSize(), pokerGame.computeCallAmount(heroPosition), hero.getStackStub()).toAdvice(), pokerGame).toDecision();
                        double branchProbabilityMultiplied = branchProbability / sum * srcBranchProbability;
                        return new PokerGameEdge(correctedDecision, branchProbabilityMultiplied);
                    })
                    .collect(Collectors.groupingBy(PokerGameEdge::decision,
                            Collectors.collectingAndThen(Collectors.toList(), edges -> {
                                PokerGameEdge pokerGameEdge = edges.get(0);
                                double totalWeight = edges.size() == 1 ? pokerGameEdge.weight() : edges.stream().mapToDouble(PokerGameEdge::weight).sum();
                                return new PokerGameEdge(pokerGameEdge.decision(), totalWeight);
                            })))
                    .values().parallelStream()
                    .map(pokerGameEdge -> {
                        double branchProbability = pokerGameEdge.weight();
                        List<Move> moves = new ArrayList<>();
                        moves.add(new Move(heroPosition, pokerGameEdge.decision()));
                        PokerGameEventType moveType = street.getPokerGameEventType();
                        PokerHand ph = PokerHand.createForMove(moveType, moves);
                        PokerGame copy = pokerGame.copy();
                        pokerEngine.apply(copy, moveType, 0, ph);
                        PokerSituation pokerSituation1 = copy.getState();
                        if (pokerSituation1 == PokerSituation.RV_COMPLETED) {
                            return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                        } else if (pokerSituation1 == PokerSituation.FL_COMPLETED) {
                            if (copy.isCompleted()) {
                                return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                            } else {
                                PokerGame next = copy.copy();
                                PokerHand completed = PokerHand.createForCards(PokerGameEventType.TN_CARD, null, null, null);
                                pokerEngine.apply(next, PokerGameEventType.TN_CARD, 0, completed);
                                PokerGameNode pokerGameNode = new PokerGameNode(next, generateRoughPostflop(next, branchProbability, statisticsMap, srcHeroPosition));
                                return Pair.of(pokerGameEdge, pokerGameNode);
                            }
                        } else if (pokerSituation1 == PokerSituation.TN_COMPLETED) {
                            if (copy.isCompleted()) {
                                return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                            } else {
                                PokerGame next = copy.copy();
                                PokerHand completed = PokerHand.createForCards(PokerGameEventType.RV_CARD, null, null, null);
                                pokerEngine.apply(next, PokerGameEventType.RV_CARD, 0, completed);
                                PokerGameNode pokerGameNode = new PokerGameNode(next, generateRoughPostflop(next, branchProbability, statisticsMap, srcHeroPosition));
                                return Pair.of(pokerGameEdge, pokerGameNode);
                            }
                        } else {
                            PokerGameNode pokerGameNode = new PokerGameNode(copy, generatePrecisePostflop(copy,
                                    branchProbability,
                                    boardFlush,
                                    boardStraight,
                                    boardPair,
                                    statisticsMap,
                                    srcHeroPosition));
                            return Pair.of(pokerGameEdge, pokerGameNode);
                        }
                    }).toList().stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
        }
    }

    private Map<PokerGameEdge, PokerGameNode> generateRoughPostflop(PokerGame pokerGame,
                                                                    double srcBranchProbability,
                                                                    Map<String, HoldemStatistics> statisticsMap,
                                                                    PreflopPosition srcHeroPosition) {
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        HoldemStatistics statistics = statisticsMap.get(hero.getPlayerName());
        PreflopPosition heroPosition = hero.getPreflopPosition();
        PokerSituation pokerSituation = pokerGame.getState();
        Street street = pokerGame.getState().getStreet();
        List<MoveStatistics> moveStats = pokerGame.getMoveStatistics();
        Branch inBranch = moveStats.isEmpty() ? Branch.NONE : moveStats.get(moveStats.size() - 1).outBranch().findClosest(statistics.getPostflopPokerSituationInBranches(pokerSituation));
        PostflopStatisticsKey preflopStatisticsKey = new PostflopStatisticsKey(pokerSituation, inBranch);
        EnumMap<Branch, Double> branchWeights = statistics.findBranchWeights(preflopStatisticsKey);

        List<Map.Entry<Branch, Double>> filteredBranches = branchWeights.entrySet().stream().toList();
//        if (heroPosition == srcHeroPosition) {
//            filteredBranches = branchWeights.entrySet().stream().toList();
//        } else {
//            Optional<Map.Entry<Branch, Double>> maxProbabilityRaiseBranch = branchWeights.entrySet().stream()
//                    .filter(e -> e.getKey().getAction() == Action.R)
//                    .max(Comparator.comparingDouble(Map.Entry::getValue));
//            filteredBranches = branchWeights.entrySet().stream()
//                    .filter(e -> e.getKey().getAction() != Action.R)
//                    .collect(Collectors.toList());
//            maxProbabilityRaiseBranch.ifPresent(filteredBranches::add);
//        }

        double sum = filteredBranches.stream().mapToDouble(Map.Entry::getValue).sum();

        return filteredBranches.parallelStream()
                .map(e -> {
                    double branchProbability = e.getValue();
                    Branch decisionBranch = e.getKey();
                    //TODO remove either Decision or Advice
                    Decision correctedDecision = AdviceCorrector.correct(decisionBranch.toDecision(pokerGame.getPotSize(), pokerGame.computeCallAmount(heroPosition), hero.getStackStub()).toAdvice(), pokerGame).toDecision();
                    double branchProbabilityMultiplied = branchProbability / sum * srcBranchProbability;
                    return new PokerGameEdge(correctedDecision, branchProbabilityMultiplied);
                })
                .collect(Collectors.groupingBy(PokerGameEdge::decision,
                        Collectors.collectingAndThen(Collectors.toList(), edges -> {
                            PokerGameEdge pokerGameEdge = edges.get(0);
                            double totalWeight = edges.size() == 1 ? pokerGameEdge.weight() : edges.stream().mapToDouble(PokerGameEdge::weight).sum();
                            return new PokerGameEdge(pokerGameEdge.decision(), totalWeight);
                        })))
                .values().parallelStream()
                .map(pokerGameEdge -> {
                    double branchProbability = pokerGameEdge.weight();
                    List<Move> moves = new ArrayList<>();
                    moves.add(new Move(heroPosition, pokerGameEdge.decision()));
                    PokerGameEventType moveType = street.getPokerGameEventType();
                    PokerHand ph = PokerHand.createForMove(moveType, moves);
                    PokerGame copy = pokerGame.copy();
                    pokerEngine.apply(copy, moveType, 0, ph);
                    PokerSituation pokerSituation1 = copy.getState();
                    if (pokerSituation1 == PokerSituation.RV_COMPLETED) {
                        return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                    } else if (pokerSituation1 == PokerSituation.FL_COMPLETED) {
                        if (copy.isCompleted()) {
                            return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                        } else {
                            PokerGame next = copy.copy();
                            PokerHand completed = PokerHand.createForCards(PokerGameEventType.TN_CARD, null, null, null);
                            pokerEngine.apply(next, PokerGameEventType.TN_CARD, 0, completed);
                            PokerGameNode pokerGameNode = new PokerGameNode(next, generateRoughPostflop(next, branchProbability, statisticsMap, srcHeroPosition));
                            return Pair.of(pokerGameEdge, pokerGameNode);
                        }
                    } else if (pokerSituation1 == PokerSituation.TN_COMPLETED) {
                        if (copy.isCompleted()) {
                            return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                        } else {
                            PokerGame next = copy.copy();
                            PokerHand completed = PokerHand.createForCards(PokerGameEventType.RV_CARD, null, null, null);
                            pokerEngine.apply(next, PokerGameEventType.RV_CARD, 0, completed);
                            PokerGameNode pokerGameNode = new PokerGameNode(next, generateRoughPostflop(next, branchProbability, statisticsMap, srcHeroPosition));
                            return Pair.of(pokerGameEdge, pokerGameNode);
                        }
                    } else {
                        PokerGameNode pokerGameNode = new PokerGameNode(copy, generateRoughPostflop(copy, branchProbability, statisticsMap, srcHeroPosition));
                        return Pair.of(pokerGameEdge, pokerGameNode);
                    }
                }).toList().stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    private Map<PokerGameEdge, PokerGameNode> generateSingleBranchPostflop(PokerGame pokerGame,
                                                                           double srcBranchProbability,
                                                                           Map<String, HoldemStatistics> statisticsMap,
                                                                           PreflopPosition srcHeroPosition) {
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        HoldemStatistics statistics = statisticsMap.get(hero.getPlayerName());
        PreflopPosition heroPosition = hero.getPreflopPosition();
        PokerSituation pokerSituation = pokerGame.getState();
        Street street = pokerGame.getState().getStreet();
        List<MoveStatistics> moveStats = pokerGame.getMoveStatistics();
        Branch inBranch = moveStats.isEmpty() ? Branch.NONE : moveStats.get(moveStats.size() - 1).outBranch().findClosest(statistics.getPostflopPokerSituationInBranches(pokerSituation));
        PostflopStatisticsKey preflopStatisticsKey = new PostflopStatisticsKey(pokerSituation, inBranch);
        EnumMap<Branch, Double> branchWeights = statistics.findBranchWeights(preflopStatisticsKey);
        List<Map.Entry<Branch, Double>> filteredBranches = branchWeights.entrySet().stream().toList();
//        if (heroPosition == srcHeroPosition) {
//            filteredBranches = branchWeights.entrySet().stream().toList();
//        } else {
//            Optional<Map.Entry<Branch, Double>> maxProbabilityRaiseBranch = branchWeights.entrySet().stream()
//                    .filter(e -> e.getKey().getAction() == Action.R)
//                    .max(Comparator.comparingDouble(Map.Entry::getValue));
//            filteredBranches = branchWeights.entrySet().stream()
//                    .filter(e -> e.getKey().getAction() != Action.R)
//                    .collect(Collectors.toList());
//            maxProbabilityRaiseBranch.ifPresent(filteredBranches::add);
//        }
        double sum = filteredBranches.stream().mapToDouble(Map.Entry::getValue).sum();
        return filteredBranches.parallelStream()
                .map(e -> {
                    double branchProbability = e.getValue();
                    Branch decisionBranch = e.getKey();
                    //TODO remove either Decision or Advice
                    Decision correctedDecision = AdviceCorrector.correct(decisionBranch.toDecision(pokerGame.getPotSize(), pokerGame.computeCallAmount(heroPosition), hero.getStackStub()).toAdvice(), pokerGame).toDecision();
                    double branchProbabilityMultiplied = branchProbability / sum * srcBranchProbability;
                    return new PokerGameEdge(correctedDecision, branchProbabilityMultiplied);
                })
                .collect(Collectors.groupingBy(PokerGameEdge::decision,
                        Collectors.collectingAndThen(Collectors.toList(), edges -> {
                            PokerGameEdge pokerGameEdge = edges.get(0);
                            double totalWeight = edges.size() == 1 ? pokerGameEdge.weight() : edges.stream().mapToDouble(PokerGameEdge::weight).sum();
                            return new PokerGameEdge(pokerGameEdge.decision(), totalWeight);
                        })))
                .values().parallelStream()
                .map(pokerGameEdge -> {
                    double branchProbability = pokerGameEdge.weight();
                    List<Move> moves = new ArrayList<>();
                    moves.add(new Move(heroPosition, pokerGameEdge.decision()));
                    PokerGameEventType moveType = street.getPokerGameEventType();
                    PokerHand ph = PokerHand.createForMove(moveType, moves);
                    PokerGame copy = pokerGame.copy();
                    pokerEngine.apply(copy, moveType, 0, ph);
                    PokerSituation pokerSituation1 = copy.getState();
                    if (pokerSituation1 == PokerSituation.RV_COMPLETED) {
                        return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                    } else if (pokerSituation1 == PokerSituation.FL_COMPLETED) {
                        if (copy.isCompleted()) {
                            return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                        } else {
                            PokerGame next = copy.copy();
                            PokerHand completed = PokerHand.createForCards(PokerGameEventType.TN_CARD, null, null, null);
                            pokerEngine.apply(next, PokerGameEventType.TN_CARD, 0, completed);
                            PokerGameNode pokerGameNode = new PokerGameNode(next, generateSingleBranchPostflop(next, branchProbability, statisticsMap, srcHeroPosition));
                            return Pair.of(pokerGameEdge, pokerGameNode);
                        }
                    } else if (pokerSituation1 == PokerSituation.TN_COMPLETED) {
                        if (copy.isCompleted()) {
                            return Pair.of(pokerGameEdge, new PokerGameNode(copy, Collections.emptyMap()));
                        } else {
                            PokerGame next = copy.copy();
                            PokerHand completed = PokerHand.createForCards(PokerGameEventType.RV_CARD, null, null, null);
                            pokerEngine.apply(next, PokerGameEventType.RV_CARD, 0, completed);
                            PokerGameNode pokerGameNode = new PokerGameNode(next, generateSingleBranchPostflop(next, branchProbability, statisticsMap, srcHeroPosition));
                            return Pair.of(pokerGameEdge, pokerGameNode);
                        }
                    } else {
                        PokerGameNode pokerGameNode = new PokerGameNode(copy, generateSingleBranchPostflop(copy, branchProbability, statisticsMap, srcHeroPosition));
                        return Pair.of(pokerGameEdge, pokerGameNode);
                    }
                }).toList().stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

}

