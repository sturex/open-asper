package dev.asper.advice;

import java.io.Serializable;

public record AmountTweak(AmountTweakStrategy amountTweakStrategy,
                          double value) implements Serializable {
    public static AmountTweak identity = new AmountTweak(AmountTweakStrategy.IDENTITY, 1.0);
    public static AmountTweak multiply = new AmountTweak(AmountTweakStrategy.MULTIPLY, 1.0);

    public static AmountTweak replace(double value) {
        return new AmountTweak(AmountTweakStrategy.REPLACE, value);
    }

    @Override
    public String toString() {
        return amountTweakStrategy + ", " + value;
    }

    public double apply(double amount) {
        return amountTweakStrategy.apply(amount, value);
    }

}
