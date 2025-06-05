package dev.asper.poker.holding;

import dev.asper.poker.card.Rank;

public record ComboStatus(
        ComboExType comboExType,
        ComboDescription comboDescription,
        Handicap handicap,
        Rank comboRank,
        Nuts nuts
) {
}
