package dev.asper.poker.engine.spot;

import dev.asper.advice.ActionMap;
import dev.asper.advice.ActionTweak;
import dev.asper.advice.ActionVector;
import dev.asper.advice.AmountTweak;
import dev.asper.app.graphql.SpotInfo;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.enums.Street;
import dev.asper.spark.FeatureSchema;

public interface Spot {
    PokerSituation pokerSituation();

    FeatureSchema actionFeatureSchema();

    FeatureSchema amountFeatureSchema();

    String name();

    ActionMap defaultActionMap();

    ActionVector defaultActionVector();

    double defaultDiffAmountByPot();

    ActionTweak actionTweak();

    AmountTweak amountTweak();

    default Street street() {
        return pokerSituation().getStreet();
    }

    double defaultAmount();

    default int compareTo(Spot spot) {
        int psCompared = pokerSituation().getStreet().compareTo(spot.pokerSituation().getStreet());
        return psCompared == 0 ? name().compareTo(spot.name()) : psCompared;
    }

    default SpotInfo toSpotInfo() {
        return new SpotInfo(this, name(), street(), pokerSituation(), actionFeatureSchema(), amountFeatureSchema());
    }
}
