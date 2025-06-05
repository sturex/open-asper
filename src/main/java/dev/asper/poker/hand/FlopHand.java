package dev.asper.poker.hand;



import dev.asper.poker.card.Card;

import java.util.EnumSet;

public class FlopHand extends Hand {

    public FlopHand(EnumSet<Card> flCards) {
        super(flCards);
    }

    public TurnHand addTurnCard(Card card) {
        EnumSet<Card> cards = this.cards();
        EnumSet<Card> tnCards = EnumSet.copyOf(cards);
        tnCards.add(card);
        return new TurnHand(tnCards);
    }

}
