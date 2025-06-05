package dev.asper.metric.cactus;


import dev.asper.poker.card.Rank;
import dev.asper.poker.holding.NormalizedNumeric;
import dev.asper.poker.holding.ComboType;

import java.util.EnumSet;

public record CactusCombo(ComboType comboType, String description, double value, Rank rank, EnumSet<Rank> ranks) implements NormalizedNumeric {
    @Override
    public double normalizedNumeric() {
        return value;
    }
}
