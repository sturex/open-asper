package dev.asper.app.graphql;

import dev.asper.advice.AmountTweak;
import dev.asper.advice.AmountTweakStrategy;

public record AmountTweakInput(AmountTweakStrategy amountTweakStrategy,
                               double value) {
    public AmountTweak toAmountTweak() {
        return new AmountTweak(amountTweakStrategy, value);
    }
}
