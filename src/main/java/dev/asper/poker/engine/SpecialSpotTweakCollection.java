package dev.asper.poker.engine;

import dev.asper.advice.ActionMap;
import dev.asper.poker.ai.ActionMapGame;
import dev.asper.poker.enums.SpecialSpot;

import java.util.EnumMap;

public record SpecialSpotTweakCollection(EnumMap<SpecialSpot, ActionMap> collection) {
    public SpecialSpotTweakCollection() {
        this(new EnumMap<>(SpecialSpot.class));
    }

    public ActionMapGame apply(ActionMapGame actionMapGame) {
        if (!collection.isEmpty()) {
            ActionMap actionMap = actionMapGame.actionMap();
            PokerGame pokerGame = actionMapGame.pokerGame();
            SpecialSpot specialSpot = SpecialSpot.from(pokerGame);
            ActionMap tweak = collection.getOrDefault(specialSpot, ActionMap.identity);
            return new ActionMapGame(pokerGame, actionMap.multiply(tweak));
        }
        return actionMapGame;
    }

    public static SpecialSpotTweakCollection identity = new SpecialSpotTweakCollection();
}
