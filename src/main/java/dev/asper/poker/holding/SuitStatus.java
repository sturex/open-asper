package dev.asper.poker.holding;

import java.util.*;
import java.util.stream.Collectors;

public enum SuitStatus {
    SUITED("s"), OFF_SUITED("o"), EQUAL_RANKS("");

    private final String letter;

    SuitStatus(String letter) {
        this.letter = letter;
    }

    public String letter() {
        return letter;
    }
}
