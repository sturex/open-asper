package dev.asper.poker.holding;

public record BoardStatus(
        BoardPair boardPair,
        BoardStraight boardStraight,
        BoardFlush boardFlush
) {
}
