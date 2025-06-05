package dev.asper.poker.engine;


import dev.asper.advice.Decision;
import dev.asper.poker.enums.Action;
import dev.asper.poker.enums.PreflopPosition;

import java.io.Serializable;

public record Move(PreflopPosition preflopPosition, Decision decision) implements Serializable {
    @Override
    public String toString() {
        return preflopPosition + ":" + decision;
    }
}
