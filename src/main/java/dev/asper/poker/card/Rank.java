package dev.asper.poker.card;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Rank {
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("T"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");

    private final String letter;
    private final static Map<String, Rank> mapping = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(v -> mapping.put(v.letter, v));
    }

    Rank(String letter) {
        this.letter = letter;
    }

    public static Rank of(Character character) {
        return of(character.toString());
    }

    public static Rank of(String value) {
        return mapping.get(value);
    }

    public static Rank of(int value) {
        return values()[value];
    }

    public String letter() {
        return letter;
    }

    public boolean isGreater(Rank other) {
        return this.compareTo(other) > 0;
    }

    public boolean isGreaterOrEquals(Rank other) {
        return this == other || this.compareTo(other) > 0;
    }

    public boolean isLess(Rank other) {
        return this.compareTo(other) < 0;
    }

}
