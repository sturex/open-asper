package dev.asper.poker.ai;

import dev.asper.advice.Advice;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.SpecialSpotTweakCollection;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.engine.spot.Spots;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Solution {
    private final String name;
    private final Map<Spot, Advisor> advisors;

    public Solution(String name, Map<Spot, Advisor> advisors) {
        this.name = name;
        this.advisors = advisors;
    }

    public List<AdvisedGame> advices(List<PokerGame> pokerGames, SpecialSpotTweakCollection specialSpotTweakCollection) {
        Map<Spot, List<PokerGame>> bySpot = pokerGames.stream().collect(Collectors.groupingBy(Spots::from));
        return bySpot.entrySet().parallelStream()
                .flatMap(e -> {
                    Spot spot = e.getKey();
                    List<PokerGame> pgs = e.getValue();
                    return advisors.get(spot).advices(spot, pgs, specialSpotTweakCollection).stream();
                })
                .toList();
    }

    public Advice advice(PokerGame pokerGame, SpecialSpotTweakCollection specialSpotTweakCollection) {
        Spot spot = Spots.from(pokerGame);
        return advisors.get(spot).advice(spot, pokerGame, specialSpotTweakCollection);
    }
}
