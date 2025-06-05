package dev.asper.app.graphql;

import dev.asper.advice.CardCellWeightedRandomAmount;
import dev.asper.advice.WeightedAmount;
import dev.asper.advice.WeightedRandomAmount;
import dev.asper.poker.card.CardCell;

import java.util.List;

public record CardCellWeightedRandomAmountOutput(CardCell cardCell, List<WeightedAmount> weightedAmounts) {
    public static CardCellWeightedRandomAmountOutput from(CardCellWeightedRandomAmount cardCellWeightedRandomAmount) {
        WeightedRandomAmount weightedRandomAmount = cardCellWeightedRandomAmount.weightedRandomAmount();
        List<WeightedAmount> weightedAmounts = WeightedRandomAmountOutput.from(weightedRandomAmount.getWeightedAmounts());
        return new CardCellWeightedRandomAmountOutput(cardCellWeightedRandomAmount.cardCell(), weightedAmounts);
    }
}
