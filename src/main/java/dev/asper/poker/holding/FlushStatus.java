package dev.asper.poker.holding;

import dev.asper.poker.card.Rank;

public record FlushStatus(Flush flush, Handicap handicap, Nuts nuts, Rank rank) {
}
