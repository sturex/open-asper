package dev.asper.app.graphql;

import dev.asper.advice.ActionTweak;
import dev.asper.advice.ActionVector;
import dev.asper.poker.enums.SpecialSpot;

public record SpecialSpotTweakOutput(
        SpecialSpot specialSpot,
        ActionVector actionVector
) {
}
