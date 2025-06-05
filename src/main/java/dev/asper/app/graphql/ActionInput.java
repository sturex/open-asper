package dev.asper.app.graphql;

import dev.asper.advice.Decision;
import dev.asper.poker.enums.Action;

public record ActionInput(Action action, int amount) {
    public Decision toDecision() {
        return new Decision(action, amount);
    }
}
