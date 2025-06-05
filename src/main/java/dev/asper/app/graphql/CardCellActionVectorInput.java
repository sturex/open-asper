package dev.asper.app.graphql;

import dev.asper.poker.chart.CardCellActionMap;
import dev.asper.poker.card.CardCell;

public record CardCellActionVectorInput(CardCell cardCell, ActionVectorInput actionVectorInput) {
    public CardCellActionMap toCardCellActionMap() {
        return new CardCellActionMap(cardCell, actionVectorInput.toActionMap());
    }
}
