package dev.asper.poker.hand;



import dev.asper.poker.card.Card;
import dev.asper.poker.card.CardPair;

import java.util.EnumSet;

public class PreflopHand extends Hand {

    public PreflopHand(EnumSet<Card> cards) {
        super(cards);
    }
    public PreflopHand(CardPair cardPair) {
        super(cardPair.cards());
    }

    private PreflopHand(Card card1, Card card2) {
        super(EnumSet.of(card1, card2));
    }

    public static PreflopHand of(Card card1, Card card2) {
        return new PreflopHand(card1, card2);
    }

    public FlopHand addFlopCards(EnumSet<Card> cards) {
        EnumSet<Card> flCards = EnumSet.copyOf(this.cards());
        flCards.addAll(cards);
        return new FlopHand(flCards);
    }

}
