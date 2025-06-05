package dev.asper.poker.card;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public record Cards(EnumSet<Card> cards) {
    public Cards(Card... cards) {
        this(Arrays.stream(cards).collect(Collectors.toCollection(() -> EnumSet.noneOf(Card.class))));
    }

    public final static Cards empty = new Cards();

    public static Cards from(String cards) {
        if (cards.isEmpty()) {
            return empty;
        } else {
            String[] split = cards.split("(?<=\\G.{2})");
            return new Cards(Arrays.stream(split)
                    .map(Card::from)
                    .collect(Collectors.toCollection(() -> EnumSet.noneOf(Card.class))));
        }
    }

    public Cards plus(Card card) {
        EnumSet<Card> clone = cards.clone();
        clone.add(card);
        return new Cards(clone);
    }

    public Cards plus(Card... cards) {
        EnumSet<Card> clone = this.cards.clone();
        for (Card card : cards) {
            clone.add(card);
        }
        return new Cards(clone);
    }

    public boolean isEmpty() {
        return cards().isEmpty();
    }

    public String asString() {
        return cards.stream().map(Card::stdName).collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return isEmpty() ? "" : "[" + asString() + "]";
    }

    public List<Card> asList() {
        return cards.stream().sorted(Comparator.comparing(Card::rank).reversed()).toList();
    }
}
