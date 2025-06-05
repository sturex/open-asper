package dev.asper.poker.chart;

import dev.asper.advice.AmountTweak;
import dev.asper.advice.WeightedAmount;
import dev.asper.advice.WeightedRandomAmount;
import dev.asper.poker.ai.AmountAdvisor;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerPlayer;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.spark.FeatureSchema;

import java.util.List;

public class PostflopAmountChart implements AmountAdvisor {
    private final WeightedRandomAmount weightedRandomAmount;

    private AmountTweak amountTweak = AmountTweak.identity;

    public PostflopAmountChart(WeightedRandomAmount weightedRandomAmount) {
        this.weightedRandomAmount = weightedRandomAmount;
    }

    @Override
    public int adviceAmount(Spot spot, PokerGame pokerGame) {
        FeatureSchema amountFeatureSchema = spot.amountFeatureSchema();
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        int heroCallAmount = pokerGame.computeCallAmount(hero.getPreflopPosition());
        return switch (amountFeatureSchema) {
            case AMOUNT_BB -> (int) weightedRandomAmount.next();
            case DIFF_AMOUNT_BB -> heroCallAmount + (int) weightedRandomAmount.next();
            case AMOUNT_BY_POT -> (int) (pokerGame.getPotSize() * weightedRandomAmount.next());
            case DIFF_AMOUNT_BY_POT -> heroCallAmount + (int) (pokerGame.getPotSize() * weightedRandomAmount.next());
            default -> throw new IllegalStateException("Unexpected value: " + amountFeatureSchema);
        };
    }

    public void setAmountTweak(AmountTweak amountTweak) {
        this.amountTweak = amountTweak;
    }

    public WeightedRandomAmount getWeightedRandomAmount() {
        return weightedRandomAmount;
    }

    @Override
    public AmountTweak getAmountTweak() {
        return amountTweak;
    }

    public List<WeightedAmount> getWeightedAmounts() {
        return weightedRandomAmount.getWeightedAmounts();
    }
}
