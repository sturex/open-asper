package dev.asper.spark;


public enum ModelType {
    REGRESSION(MetricName.RMSE), CLASSIFICATION(MetricName.ACCURACY_ERROR);

    private final MetricName metricName;

    ModelType(MetricName metricName) {
        this.metricName = metricName;
    }

    public MetricName metricName(){
        return metricName;
    }
}
