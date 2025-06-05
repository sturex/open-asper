package dev.asper.app.graphql;

import dev.asper.poker.card.CardPair;
import dev.asper.poker.engine.PlayerInitialState;
import dev.asper.poker.enums.PreflopPosition;

public record PlayerInitialStateInput(
        PreflopPosition preflopPosition,
        String playerName,
        String pocketCards,
        int initialStack,
        double vpip) {
    public PlayerInitialState toPlayerInitialState() {
        return pocketCards == null || pocketCards.isEmpty() ? new PlayerInitialState(playerName, initialStack, vpip()) : new PlayerInitialState(playerName, CardPair.from(pocketCards), initialStack, vpip);
    }
}
