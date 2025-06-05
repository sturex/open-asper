package dev.asper.advice;


import dev.asper.poker.enums.Action;
import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;

public record Decision(Action action, int amount) implements Serializable {

    @Override
    public String toString() {
        return action != Action.F ? action + "(" + amount + ")" : action.letter();
    }

    public Advice toAdvice() {
        return new Advice(action, amount);
    }

}
