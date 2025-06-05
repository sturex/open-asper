package dev.asper.app.component;

import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.clickhouse.ClickhouseProperties;
import dev.asper.spark.*;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ModelTrainer {

    private final SparkSession sparkSession;
    private final ClickhouseProperties clickhouseProperties;
    private final LogService logService;

    @Autowired
    public ModelTrainer(SparkSession sparkSession,
                        ClickhouseProperties clickhouseProperties,
                        LogService logService) {
        this.sparkSession = sparkSession;
        this.clickhouseProperties = clickhouseProperties;
        this.logService = logService;

    }

    public ModelInfo buildAndTrain(ModelParams modelParams, TrainMode trainMode, int minSamples) {
        Dataset<Row> dataset;
        try {
            dataset = readDataset(modelParams.datasetQuery());
        } catch (Exception e) {
            logService.log(LogRecordCode.READ_DATASET_FAILURE, Map.of("model_name", e.getMessage()));
            return trainOnFallbackQuery(modelParams, trainMode, minSamples);
        }
        long count = dataset.count();
        if (count < minSamples) {
            return trainOnFallbackQuery(modelParams, trainMode, minSamples);
        } else {
            try {
                return trainOnDatasetQuery(modelParams, trainMode, dataset);
            } catch (Exception e) {
                logService.log(LogRecordCode.PRIMARY_DATASET_TRAIN_FAILURE, Map.of("model_name", e.getMessage()));
                return trainOnFallbackQuery(modelParams, trainMode, minSamples);
            }
        }
    }

    private ModelInfo trainOnFallbackQuery(ModelParams modelParams, TrainMode trainMode, int minSamples) {
        Dataset<Row> fallbackDataset = readDataset(modelParams.fallbackQuery());
        long fcount = fallbackDataset.count();
        if (fcount < minSamples) {
            throw new InsufficientDataException();
        } else {
            return trainOnDatasetQuery(modelParams, trainMode, fallbackDataset);
        }
    }

    private ModelInfo trainOnDatasetQuery(ModelParams modelParams, TrainMode trainMode, Dataset<Row> dataset) {
        return modelParams.modelType() == ModelType.REGRESSION ?
                PipelineUtils.buildRegressionPipeline(modelParams, dataset, trainMode) :
                PipelineUtils.buildClassificationPipeline(modelParams, dataset, trainMode);
    }

    private Dataset<Row> readDataset(String query) {
        return sparkSession.read()
                .format("org.apache.spark.sql.execution.datasources.jdbc.JdbcRelationProvider")
                .options(clickhouseProperties.options())
                .option("query", query)
                .load();
    }

}
