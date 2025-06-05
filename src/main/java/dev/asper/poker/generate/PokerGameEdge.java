package dev.asper.poker.generate;


import dev.asper.advice.Decision;

public record PokerGameEdge(Decision decision, double weight) {
}
