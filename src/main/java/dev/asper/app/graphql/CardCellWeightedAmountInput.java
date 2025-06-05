package dev.asper.app.graphql;

import dev.asper.poker.card.CardCell;

import java.util.List;

public record CardCellWeightedAmountInput(CardCell cardCell, List<WeightedAmountInput> weightedAmountInputs) {
}
