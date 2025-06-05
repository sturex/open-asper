package dev.asper.app.graphql;

import dev.asper.advice.WeightedAmount;

public record WeightedAmountInput(double weight, double amount) {
    public WeightedAmount toWeightedAmount() {
        return new WeightedAmount(weight, amount);
    }
}
