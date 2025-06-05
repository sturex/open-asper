package dev.asper.spark;


public record ModelParams(
        ModelType modelType,
        FeatureSchema featureSchema,
        String labelFeatureName,
        String[] categoricalFeatureNames,
        String[] quantitativeFeatureNames,
        int maxDepth,
        int minInstancesPerNode,
        double minInfoGain,
        double minWeightFractionPerNode,
        String datasetQuery,
        String fallbackQuery) {
}