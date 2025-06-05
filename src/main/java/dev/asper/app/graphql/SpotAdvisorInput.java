package dev.asper.app.graphql;

import dev.asper.poker.engine.spot.Spot;

public record SpotAdvisorInput(String spot,
                               ActionAdvisorInput actionAdvisorInput,
                               ActionTweakInput actionTweakInput,
                               AmountAdvisorInput amountAdvisorInput,
                               AmountTweakInput amountTweakInput) {
}
