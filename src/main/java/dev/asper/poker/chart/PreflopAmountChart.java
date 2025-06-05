package dev.asper.poker.chart;

import dev.asper.advice.*;
import dev.asper.poker.ai.AmountAdvisor;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerPlayer;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.card.CardCell;
import dev.asper.spark.FeatureSchema;

import java.io.Serial;
import java.util.*;

public class PreflopAmountChart extends EnumMap<CardCell, WeightedRandomAmount> implements AmountAdvisor {
    @Serial
    private static final long serialVersionUID = -8686757074250290920L;
    private final List<CardCellWeightedRandomAmount> cardCellWeightedRandomAmounts;
    private AmountTweak amountTweak = AmountTweak.identity;

    public PreflopAmountChart(Spot spot, List<CardCellWeightedRandomAmount> cardCellWeightedRandomAmounts) {
        super(CardCell.class);
        this.cardCellWeightedRandomAmounts = new ArrayList<>(cardCellWeightedRandomAmounts);
        this.cardCellWeightedRandomAmounts.forEach(cardCellWeightedRandomAmount -> {
            CardCell cardCell = cardCellWeightedRandomAmount.cardCell();
            this.put(cardCell, cardCellWeightedRandomAmount.weightedRandomAmount());
        });
        EnumSet<CardCell> cardCells = EnumSet.allOf(CardCell.class);
        cardCells.removeAll(keySet());
        List<WeightedAmount> weightedAmounts = List.of(new WeightedAmount(1., spot.defaultAmount()));
        WeightedRandomAmount defaultWeightedRandomAmount = new WeightedRandomAmount(weightedAmounts);
        cardCells.forEach(cardCell -> {
            this.cardCellWeightedRandomAmounts.add(new CardCellWeightedRandomAmount(cardCell, defaultWeightedRandomAmount));
            this.put(cardCell, defaultWeightedRandomAmount);
        });
    }

    public void setAmountTweak(AmountTweak amountTweak) {
        this.amountTweak = amountTweak;
    }

    @Override
    public int adviceAmount(Spot spot, PokerGame pokerGame) {
        CardCell cardCell = pokerGame.getNextPlayerOrThrow().getCardCell();
        WeightedRandomAmount weightedRandomAmount = Optional.ofNullable(get(cardCell))
                .orElseThrow(() -> new RuntimeException("%s doesn't contain %s key".formatted(PreflopAmountChart.class.getPackageName(), cardCell.name())));
        FeatureSchema amountFeatureSchema = spot.amountFeatureSchema();
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        int heroCallAmount = pokerGame.computeCallAmount(hero.getPreflopPosition());
        return switch (amountFeatureSchema) {
            case AMOUNT_BB -> (int) (weightedRandomAmount.next() * pokerGame.getBbAmount());
            case DIFF_AMOUNT_BB -> heroCallAmount + (int) (weightedRandomAmount.next() * pokerGame.getBbAmount());
            case AMOUNT_BY_POT -> (int) (pokerGame.getPotSize() * weightedRandomAmount.next());
            case DIFF_AMOUNT_BY_POT -> heroCallAmount + (int) (pokerGame.getPotSize() * weightedRandomAmount.next());
            default -> throw new IllegalStateException("Unexpected value: " + amountFeatureSchema);
        };
    }

    public List<CardCellWeightedRandomAmount> getCardCellWeightedRandomAmounts() {
        return cardCellWeightedRandomAmounts;
    }

    @Override
    public AmountTweak getAmountTweak() {
        return amountTweak;
    }
}
