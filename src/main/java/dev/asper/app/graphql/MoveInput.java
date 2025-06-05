package dev.asper.app.graphql;

import dev.asper.poker.engine.Move;
import dev.asper.poker.enums.PreflopPosition;

public record MoveInput(PreflopPosition preflopPosition, ActionInput actionInput) {
    public Move toMove() {
        return new Move(preflopPosition, actionInput.toDecision());
    }
}
