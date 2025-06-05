package dev.asper.poker.hand;


import dev.asper.poker.card.Card;

import java.util.EnumSet;

public class TurnHand extends Hand {

    protected TurnHand(EnumSet<Card> cards) {
        super(cards);
    }

    public RiverHand addRiverCard(Card card) {
        EnumSet<Card> cards = this.cards();
        EnumSet<Card> rvCards = EnumSet.copyOf(cards);
        rvCards.add(card);
        return new RiverHand(rvCards);
    }
}
