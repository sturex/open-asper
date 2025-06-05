package dev.asper.poker.ai;


import dev.asper.advice.Advice;
import dev.asper.poker.engine.PokerGame;

public record AdvisedGame(PokerGame pokerGame, Advice advice) {
}
