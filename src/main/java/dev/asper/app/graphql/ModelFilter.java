package dev.asper.app.graphql;

import dev.asper.app.repository.spec.TimestampInterval;
import dev.asper.spark.FeatureSchema;
import dev.asper.spark.ModelStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ModelFilter {
    private String chars;
    private TimestampInterval createdInterval;
    private List<FeatureSchema> featureSchemas;
    private List<ModelStatus> modelStatuses;
}
