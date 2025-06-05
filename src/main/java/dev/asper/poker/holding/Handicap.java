package dev.asper.poker.holding;

public enum Handicap {
    NO_CARDS,
    ONE_CARD,
    TWO_CARDS;

    public static Handicap fromIntersect(int value) {
        return switch (value) {
            case 0 -> NO_CARDS;
            case 1 -> ONE_CARD;
            case 2 -> TWO_CARDS;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }
}
