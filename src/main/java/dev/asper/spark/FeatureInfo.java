package dev.asper.spark;

import java.util.Arrays;
import java.util.Comparator;

public class FeatureInfo {
    public static final FeatureInfo EMPTY = new FeatureInfo(new FeatureImportance[]{});
    private FeatureImportance[] featureImportances;
    private FeatureImportance[] top5FeatureImportances;

    public FeatureInfo() {
    }

    public FeatureInfo(FeatureImportance[] featureImportances) {
        this.featureImportances = featureImportances;
        this.top5FeatureImportances = Arrays.stream(featureImportances).sorted(Comparator.comparingDouble(FeatureImportance::importance).reversed()).limit(5).toArray(FeatureImportance[]::new);
    }

    public FeatureImportance[] getFeatureImportances() {
        return featureImportances;
    }

    public FeatureImportance[] getTop5FeatureImportances() {
        //TODO
        return top5FeatureImportances != null ? top5FeatureImportances : Arrays.stream(featureImportances).sorted(Comparator.comparingDouble(FeatureImportance::importance).reversed()).limit(5).toArray(FeatureImportance[]::new);
    }

    public static FeatureInfo empty() {
        return EMPTY;
    }
}
