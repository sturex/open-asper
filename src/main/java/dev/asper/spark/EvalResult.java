package dev.asper.spark;



public record EvalResult(MetricName metricName, double seenDataValue, double unknownDataValue) {
}
