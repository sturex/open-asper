package dev.asper.app.graphql;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Builder
@Data
public class DecisionTreeParamsInput {
    private int maxDepth;
    private double minInfoGain;
    private double minWeightFractionPerNode;
    private int minInstancesPerNode;

    public static DecisionTreeParamsInput[] defaultPreconfigured = new DecisionTreeParamsInput[]{
            new DecisionTreeParamsInput(20, 0.0, 0.0, 30)
    };


    @Override
    public String toString() {
        return StringUtils.remove(maxDepth + "_" + minInstancesPerNode + "_" + String.format("%f", minInfoGain) + "_" + String.format("%f", minWeightFractionPerNode), ".");
    }
}
