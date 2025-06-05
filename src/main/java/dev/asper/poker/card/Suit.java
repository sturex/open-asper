package dev.asper.poker.card;

public enum Suit {
    CLUBS("c"),
    DIAMONDS("d"),
    HEARTS("h"),
    SPADES("s");


    private final String letter;

    Suit(String letter) {
        this.letter = letter;
    }

    public static Suit from(int value) {
        return values()[value];
    }


    public String letter() {
        return letter;
    }
}
