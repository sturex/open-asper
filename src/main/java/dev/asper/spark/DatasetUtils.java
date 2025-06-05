package dev.asper.spark;

import dev.asper.common.feature.Descriptor;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public enum DatasetUtils {
    ;

    public static org.apache.spark.sql.Dataset<?> toDataset(SparkSession sparkSession, Collection<? extends List<? extends Descriptor<?>>> descriptors, StructType structType) {
        List<Row> rows = descriptors.stream()
                .map(d -> RowFactory.create(d.stream()
                        .map(Descriptor::transformedValue)
                        .toArray()))
                .collect(Collectors.toList());
        return sparkSession.createDataFrame(rows, structType);
    }

    public static double[] selectAsDoubleArray(Dataset<Row> dataset, String column) {
        org.apache.spark.sql.Dataset<Row> predictions = dataset.select(column);
        Row[] rows = (Row[]) predictions.collect();
        return Arrays.stream(rows).mapToDouble(r -> r.getDouble(0)).toArray();
    }

    public static double[][] selectAs2dDoubleArray(org.apache.spark.sql.Dataset<Row> dataset, String label) {
        org.apache.spark.sql.Dataset<Row> predictions = dataset.select(label);
        Row[] rows = (Row[]) predictions.collect();
        return Arrays.stream(rows).map(r -> {
            DenseVector denseVector = (DenseVector) (r.get(0));
            return denseVector.values();
        }).toArray(double[][]::new);
    }

    public static String[] selectAsStringArray(org.apache.spark.sql.Dataset<Row> dataset, String label) {
        org.apache.spark.sql.Dataset<Row> predictions = dataset.select(label);
        Row[] rows = (Row[]) predictions.collect();
        return Arrays.stream(rows).map(r -> r.getString(0)).toArray(String[]::new);
    }

}
