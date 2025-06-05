package dev.asper.poker.holding;

import dev.asper.poker.card.Rank;

public record StraightStatus(Straight straight, Handicap handicap, Nuts nuts, Rank rank) {
}
