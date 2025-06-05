package dev.asper.app.graphql;

import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.spark.FeatureSchema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ModelInput {
    private PokerSituation pokerSituation;
    private Spot spot;
    private FeatureSchema featureSchema;
    private final String name;
    private final String description;
    private DecisionTreeParamsInput decisionTreeParamsInput;
    private final String datasetQuery;
    private final String fallbackQuery;
}
