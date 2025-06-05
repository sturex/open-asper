package dev.asper.poker.hand;


import dev.asper.poker.card.Card;

import java.util.EnumSet;
import java.util.Objects;
import java.util.stream.Collectors;

abstract class Hand {
    private final String name;
    private final EnumSet<Card> cards;

    protected Hand(EnumSet<Card> cards) {
        this.cards = cards;
        name = this.cards.stream().sorted().map(Card::stdName).map(Objects::toString).collect(Collectors.joining());
    }

    public EnumSet<Card> cards() {
        return cards;
    }

    public String name() {
        return name;
    }

    public boolean contains(Card card) {
        return cards.contains(card);
    }

}
