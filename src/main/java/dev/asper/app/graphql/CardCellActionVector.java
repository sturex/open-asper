package dev.asper.app.graphql;

import dev.asper.advice.ActionVector;
import dev.asper.poker.card.CardCell;

public record CardCellActionVector(CardCell cardCell, ActionVector actionVector) {
}
