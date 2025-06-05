package dev.asper.poker.chart;

import dev.asper.advice.ActionMap;
import dev.asper.poker.card.CardCell;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public record CardCellActionMap(CardCell cardCell, ActionMap actionMap) implements Serializable {
    public static CardCellActionMap from(CardCell cardCell, double f, double c, double a, double r) {
        return new CardCellActionMap(cardCell, ActionMap.from(f, c, a, r));
    }

    public static List<CardCellActionMap> from(double f, double c, double a, double r, CardCell... cardCells) {
        return Arrays.stream(cardCells).map(cc -> from(cc, f, c, a, r)).toList();
    }
}
