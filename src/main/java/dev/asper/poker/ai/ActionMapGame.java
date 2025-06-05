package dev.asper.poker.ai;


import dev.asper.advice.ActionMap;
import dev.asper.advice.Advice;
import dev.asper.poker.engine.PokerGame;

public record ActionMapGame(PokerGame pokerGame, ActionMap actionMap) {
}
