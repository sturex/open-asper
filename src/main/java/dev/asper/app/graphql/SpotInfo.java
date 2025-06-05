package dev.asper.app.graphql;

import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.enums.Street;
import dev.asper.spark.FeatureSchema;

import java.io.Serializable;

public record SpotInfo(
        Spot spot,
        String name,
        Street street,
        PokerSituation pokerSituation,
        FeatureSchema actionFeatureSchema,
        FeatureSchema amountFeatureSchema
) implements Serializable {

    public SpotInfo(Spot spot) {
        this(spot, spot.name(), spot.street(), spot.pokerSituation(), spot.actionFeatureSchema(), spot.amountFeatureSchema());
    }
}
