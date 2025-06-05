package dev.asper.poker.card;

import java.util.*;
import java.util.stream.Collectors;

public class Deck {
    public static final int NUM_RANKS = 12;
    public static final int NUM_SUITS = 4;
    public static final int NUM_CARDS = 52;
    private final List<Card> cards;
    private int idx = 0;

    private Deck(EnumSet<Card> deadCards) {
        cards = Arrays.stream(Card.values())
                .filter(card -> !deadCards.contains(card))
                .collect(Collectors.toCollection(ArrayList::new));
        shuffle();
    }

    private Deck() {
        cards = Arrays.stream(Card.values())
                .collect(Collectors.toCollection(ArrayList::new));
        shuffle();
    }

    private Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public static Deck shuffled() {
        return new Deck();
    }

    public static Deck shuffledWithout(EnumSet<Card> deadCards) {
        return new Deck(deadCards);
    }

    public Card pick() {
        if (!isEmpty()) {
            return cards.get(idx++);
        } else {
            throw new IllegalStateException("Deck is empty");
        }
    }

    public boolean isEmpty() {
        return idx >= cards.size();
    }

    public Deck shuffledCopy() {
        return new Deck(cards);
    }
}
