package dev.asper.advice;

import dev.asper.poker.card.CardCell;

import java.io.Serializable;

public record CardCellWeightedRandomAmount(CardCell cardCell, WeightedRandomAmount weightedRandomAmount) implements Serializable {
}
