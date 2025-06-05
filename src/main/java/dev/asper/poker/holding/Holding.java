package dev.asper.poker.holding;

import dev.asper.poker.card.CardCell;

public record Holding(
        CardCell cardCell,
        ComboStatus comboStatus,
        FlushStatus flushStatus,
        StraightStatus straightStatus
) {
}
