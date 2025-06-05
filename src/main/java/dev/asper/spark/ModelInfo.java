package dev.asper.spark;

import org.apache.spark.ml.PipelineModel;

public record ModelInfo(
        PipelineModel pipelineModel,
        FeatureInfo featureInfo,
        EvalResult evalResult,
        int numNodes,
        int depth,
        int datasetRowCount) {
}