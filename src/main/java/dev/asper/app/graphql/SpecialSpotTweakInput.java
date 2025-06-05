package dev.asper.app.graphql;

import dev.asper.poker.enums.SpecialSpot;

public record SpecialSpotTweakInput(SpecialSpot specialSpot,
                                    ActionVectorInput actionVectorInput) {
}
