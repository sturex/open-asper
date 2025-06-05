package dev.asper.app.graphql;

import dev.asper.advice.WeightedAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public record WeightedRandomAmountOutput(List<WeightedAmount> weightedAmounts) {
    public static List<WeightedAmount> from(List<WeightedAmount> weightedAmounts) {
        List<WeightedAmount> diffWeightedAmounts = new ArrayList<>();
        diffWeightedAmounts.add(weightedAmounts.get(0));
        IntStream.range(1, weightedAmounts.size()).forEach(idx -> {
            WeightedAmount prev = weightedAmounts.get(idx - 1);
            WeightedAmount next = weightedAmounts.get(idx);
            WeightedAmount weightedAmount = new WeightedAmount(next.weight() - prev.weight(), next.amount());
            diffWeightedAmounts.add(weightedAmount);
        });
        return diffWeightedAmounts;
    }
}