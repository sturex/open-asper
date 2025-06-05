package dev.asper.app.graphql;

import dev.asper.advice.WeightedAmount;
import dev.asper.poker.card.CardCell;

import java.util.List;

public record CardCellWeightedAmount(CardCell cardCell, List<WeightedAmount> weightedAmounts) {
}
