package dev.asper.advice;

import com.google.common.util.concurrent.AtomicDouble;
import dev.asper.common.util.ExceptionHelper;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class WeightedRandomAmount implements Serializable {
    private final static Random random = new Random();
    @Serial
    private static final long serialVersionUID = 7891212884514147996L;
    private final List<WeightedAmount> weightedAmounts;

    public WeightedRandomAmount(List<WeightedAmount> weightedAmounts) {
        ExceptionHelper.throwIf(weightedAmounts.isEmpty(), "%s cannot be consructed with empty weighted amounts".formatted(WeightedRandomAmount.class.getName()));
        double totalWeight = weightedAmounts.stream().mapToDouble(WeightedAmount::weight).sum();
        AtomicDouble shift = new AtomicDouble();
        this.weightedAmounts = weightedAmounts.stream()
                .sorted(Comparator.comparingDouble(WeightedAmount::weight))
                .map(v -> {
                    double weight = v.weight() / totalWeight;
                    return new WeightedAmount(shift.addAndGet(weight), v.amount());
                })
                .toList();
    }

    public WeightedRandomAmount() {
        weightedAmounts = Collections.emptyList();
    }

    public double next() {
        double threshold = random.nextDouble();
        return weightedAmounts.stream()
                .filter(v -> v.weight() > threshold)
                .findFirst()
                .map(WeightedAmount::amount)
                .orElseThrow();
    }

    public List<WeightedAmount> getWeightedAmounts() {
        return weightedAmounts;
    }

}
