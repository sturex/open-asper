package dev.asper.stat;


import dev.asper.poker.engine.Branch;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.engine.spot.PreflopSpot;
import dev.asper.poker.enums.Street;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public record HoldemStatistics(
        Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> preflopStatistics,
        Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> postflopStatistics,
        Map<PostflopStatisticsKey, EnumMap<Branch, Double>> postflopRoughStatistics,
        List<PreflopStatisticsKey> preflopStatisticsKeys,
        EnumMap<Street, List<PostflopBoardStatisticsKey>> postflopStatisticsKeyMap,
        EnumMap<Street, List<PostflopStatisticsKey>> postflopRoughStatisticsKeyMap,
        EnumMap<PreflopSpot, List<Branch>> preflopInBranchesMap,
        EnumMap<PokerSituation, List<Branch>> postflopInBranchesMap,
        Map<PostflopPokerSituationBoardKey, List<Branch>> postflopPokerSituationBoardInBranchesMap,
        Map<PreflopStatisticsKey, List<Branch>> preflopOutBranchesMap) implements Serializable {
    private final static EnumMap<Branch, PreflopBranchStatistics> emptyMap = new EnumMap<>(Branch.class);
    private final static EnumMap<Branch, Double> emptyMap2 = new EnumMap<>(Branch.class);


    public HoldemStatistics(Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> preflopStatistics,
                            Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> postflopStatistics,
                            Map<PostflopStatisticsKey, EnumMap<Branch, Double>> postflopRoughStatistics,
                            List<PreflopStatisticsKey> preflopStatisticsKeys,
                            EnumMap<Street, List<PostflopBoardStatisticsKey>> postflopStatisticsKeyMap,
                            EnumMap<Street, List<PostflopStatisticsKey>> postflopRoughStatisticsKeyMap,
                            EnumMap<PreflopSpot, List<Branch>> preflopInBranchesMap,
                            EnumMap<PokerSituation, List<Branch>> postflopInBranchesMap,
                            Map<PostflopPokerSituationBoardKey, List<Branch>> postflopPokerSituationBoardInBranchesMap,
                            Map<PreflopStatisticsKey, List<Branch>> preflopOutBranchesMap) {
        this.preflopStatistics = preflopStatistics;
        this.postflopStatistics = postflopStatistics;
        this.postflopRoughStatistics = postflopRoughStatistics;
        this.preflopStatisticsKeys = preflopStatisticsKeys;
        this.postflopStatisticsKeyMap = postflopStatisticsKeyMap;
        this.postflopRoughStatisticsKeyMap = postflopRoughStatisticsKeyMap;
        this.preflopInBranchesMap = preflopInBranchesMap;
        this.postflopInBranchesMap = postflopInBranchesMap;
        this.postflopPokerSituationBoardInBranchesMap = postflopPokerSituationBoardInBranchesMap;
        this.preflopOutBranchesMap = preflopOutBranchesMap;
    }

    public HoldemStatistics(Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> preflopStatistics,
                            Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> postflopStatistics,
                            Map<PostflopStatisticsKey, EnumMap<Branch, Double>> postflopRoughStatistics) {
        this(preflopStatistics, postflopStatistics, postflopRoughStatistics,
                new ArrayList<>(preflopStatistics.keySet()),
                postflopStatistics.entrySet().stream()
                        .collect(Collectors.groupingBy(e -> e.getKey().pokerSituation().getStreet(),
                                () -> new EnumMap<>(Street.class),
                                Collectors.mapping(Map.Entry::getKey, Collectors.toList()))),
                postflopRoughStatistics.entrySet().stream()
                        .collect(Collectors.groupingBy(e -> e.getKey().pokerSituation().getStreet(),
                                () -> new EnumMap<>(Street.class),
                                Collectors.mapping(Map.Entry::getKey, Collectors.toList()))),
                createPreflopSpotInBranchesMap(preflopStatistics),
                createPostflopSpotInBranchesMap(postflopRoughStatistics),
                createPostflopPokerSituationBoardInBranchesMap(postflopStatistics),
                createPreflopOutBranchesMap(preflopStatistics)
        );
    }

    private static Map<PreflopStatisticsKey, List<Branch>> createPreflopOutBranchesMap(Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> postflopStatistics) {
        return postflopStatistics.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.flatMapping(entry -> entry.getValue().keySet().stream(), Collectors.toList())));
    }

    private static Map<PostflopPokerSituationBoardKey, List<Branch>> createPostflopPokerSituationBoardInBranchesMap(Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> postflopStatistics) {
        return postflopStatistics.keySet().stream().collect(Collectors.groupingBy(k -> new PostflopPokerSituationBoardKey(k.pokerSituation(), k.boardFlush(), k.boardStraight(), k.boardPair()),
                Collectors.mapping(PostflopBoardStatisticsKey::inBranch, Collectors.toList())));
    }

    private static EnumMap<PokerSituation, List<Branch>> createPostflopSpotInBranchesMap(Map<PostflopStatisticsKey, EnumMap<Branch, Double>> postflopRoughStatistics) {
        return postflopRoughStatistics.keySet().stream().collect(Collectors.groupingBy(PostflopStatisticsKey::pokerSituation, () -> new EnumMap<>(PokerSituation.class),
                Collectors.mapping(PostflopStatisticsKey::inBranch, Collectors.toList())));
    }

    private static EnumMap<PreflopSpot, List<Branch>> createPreflopSpotInBranchesMap(Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> preflopStatistics) {
        return preflopStatistics.keySet().stream().collect(Collectors.groupingBy(PreflopStatisticsKey::preflopSpot, () -> new EnumMap<>(PreflopSpot.class),
                Collectors.mapping(PreflopStatisticsKey::inBranch, Collectors.toList())));
    }

    public Map<Branch, PreflopBranchStatistics> getPreflopStatistics(PreflopStatisticsKey preflopStatisticsKey) {
        return preflopStatistics.getOrDefault(preflopStatisticsKey, emptyMap);
    }

    public List<Branch> getPreflopSpotInBranches(PreflopSpot preflopSpot) {
        return preflopInBranchesMap.getOrDefault(preflopSpot, Collections.emptyList());
    }

    public List<Branch> getPreflopSpotOutBranches(PreflopStatisticsKey preflopStatisticsKey) {
        return preflopOutBranchesMap.getOrDefault(preflopStatisticsKey, Collections.emptyList());
    }

    public List<Branch> getPostflopPokerSituationInBranches(PokerSituation pokerSituation) {
        return postflopInBranchesMap.getOrDefault(pokerSituation, Collections.emptyList());
    }

    public List<Branch> getPostflopPokerSituationBoardInBranches(PostflopPokerSituationBoardKey postflopPokerSituationBoardKey) {
        return postflopPokerSituationBoardInBranchesMap.getOrDefault(postflopPokerSituationBoardKey, Collections.emptyList());
    }

    public EnumMap<Branch, Double> findBranchWeights(PostflopBoardStatisticsKey postflopBoardStatisticsKey) {
        return postflopStatistics.getOrDefault(postflopBoardStatisticsKey, emptyMap2);
    }

    public EnumMap<Branch, Double> findBranchWeights(PostflopStatisticsKey preflopStatisticsKey) {
        return postflopRoughStatistics.getOrDefault(preflopStatisticsKey, emptyMap2);
    }
}
