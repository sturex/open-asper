package dev.asper.poker.engine;


import dev.asper.poker.card.CardPair;

import java.io.Serializable;

public record PlayerInitialState(
        String playerName,
        CardPair pocketCards,
        int initialStack,
        double vpip) implements Serializable {

    public PlayerInitialState(String playerName, CardPair pocketCards, int initialStack, double vpip) {
        this.playerName = playerName;
        this.pocketCards = pocketCards;
        this.initialStack = initialStack;
        this.vpip = vpip;
    }

    public PlayerInitialState(String playerName, int initialStack, double vpip) {
        this(playerName, null, initialStack, vpip);
    }

    public boolean isPocketCardsOpen() {
        return pocketCards != null;
    }

}
