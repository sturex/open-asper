package dev.asper.app.graphql;

import dev.asper.advice.ActionVector;
import dev.asper.poker.enums.SpecialSpot;

import java.util.List;

public record SpecialSpotTweak(
        String name,
        List<SpecialSpotTweakOutput> specialSpotTweakOutputs
) {
}
