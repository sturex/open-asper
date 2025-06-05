package dev.asper.spark;


import dev.asper.common.util.ExceptionHelper;

import java.util.stream.IntStream;


public record FeatureImportance(String featureName,
                                double importance) {

    public static FeatureImportance[] combine(String[] features, double[] importances) {
        ExceptionHelper.throwIf(features.length != importances.length, "Couldn't combine two arrays since they have different lengths. featureNamesLength=" + features.length + ", importancesLength=" + importances.length);
        return IntStream.range(0, features.length)
                .mapToObj(idx -> new FeatureImportance(features[idx], importances[idx]))
                .toArray(FeatureImportance[]::new);
    }
}
