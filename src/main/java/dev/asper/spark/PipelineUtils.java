package dev.asper.spark;

import dev.asper.clickhouse.ClickhouseProperties;
import dev.asper.common.feature.Feature;
import dev.asper.common.feature.FeatureType;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.spot.Spot;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.DecisionTreeRegressionModel;
import org.apache.spark.ml.regression.DecisionTreeRegressor;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum PipelineUtils {
    ;

    public static final int MAX_BINS = 1000;
    private static final double[] WEIGHTS_TRAIN_TEST_SPLIT = {0.7, 0.3};


    public static ModelInfo buildClassificationPipeline(ModelParams modelParams, Dataset<Row> dataset, TrainMode trainMode) {
        String[] quantitativeFeatureNames = modelParams.quantitativeFeatureNames();
        for (String c : quantitativeFeatureNames) {
            dataset = dataset.withColumn(c, new Column(c).cast(DataTypes.DoubleType));
        }
        Dataset<Row>[] split = dataset.randomSplit(WEIGHTS_TRAIN_TEST_SPLIT);
        Dataset<Row> trainingData = split[0];
        Dataset<Row> testData = split[1];
        String labelName = modelParams.labelFeatureName();
        String[] catColumnsNames = modelParams.categoricalFeatureNames();
        String[] outputCols = Arrays.stream(catColumnsNames)
                .map(s -> "t_" + s)
                .toArray(String[]::new);
        String[] totalCols = Stream.concat(Arrays.stream(quantitativeFeatureNames), Arrays.stream(outputCols)).toArray(String[]::new);
        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(totalCols)
                .setOutputCol("assembledFeatures");
        StringIndexer categoricalFeaturesIndexer = new StringIndexer()
                .setInputCols(catColumnsNames)
                .setOutputCols(outputCols)
                .setHandleInvalid("keep");
        StringIndexer labelIndexer = new StringIndexer()
                .setInputCol(labelName)
                .setOutputCol("indexedLabel")
                .setHandleInvalid("keep");
        PipelineModel pipelineModelFull = buildClassificationPipelineModel(modelParams, dataset, vectorAssembler, categoricalFeaturesIndexer, labelIndexer);
        StringIndexerModel stringIndexerModel = (StringIndexerModel) (pipelineModelFull.stages()[1]);
        DecisionTreeClassificationModel treeModel = (DecisionTreeClassificationModel) (pipelineModelFull.stages()[3]);
        double[] weights = treeModel.featureImportances().toArray();
        String[] featureNames = getDecodedFeatureNames(stringIndexerModel, vectorAssembler.getInputCols());
        FeatureInfo featureInfo = new FeatureInfo(FeatureImportance.combine(featureNames, weights));
        int numNodes = treeModel.numNodes();
        int depth = treeModel.depth();
        double evalResultSeenData = evalAccuracyErrorClassificationPipeline(pipelineModelFull, dataset);
        double evalResultUnknownData = trainMode == TrainMode.SEEN_DATA_EVAL_ONLY ? 0.0 :
                evalAccuracyErrorClassificationPipeline(buildClassificationPipelineModel(modelParams, trainingData, vectorAssembler, categoricalFeaturesIndexer, labelIndexer), testData);
        EvalResult evalResult = new EvalResult(MetricName.ACCURACY_ERROR, evalResultSeenData, evalResultUnknownData);
        return new ModelInfo(pipelineModelFull, featureInfo, evalResult, numNodes, depth, (int) dataset.count());
    }

    public static ModelInfo buildRegressionPipeline(ModelParams modelParams, Dataset<Row> dataset, TrainMode trainMode) {
        String[] quantitativeFeatureNames = modelParams.quantitativeFeatureNames();
        for (String c : quantitativeFeatureNames) {
            dataset = dataset.withColumn(c, new Column(c).cast(DataTypes.DoubleType));
        }
        Dataset<Row>[] split = dataset.randomSplit(WEIGHTS_TRAIN_TEST_SPLIT);
        Dataset<Row> trainingData = split[0];
        Dataset<Row> testData = split[1];
        String labelName = modelParams.labelFeatureName();
        dataset = dataset.withColumn(labelName, new Column(labelName).cast(DataTypes.DoubleType));
        String[] outputCols = Arrays.stream(modelParams.categoricalFeatureNames())
                .map(s -> "t_" + s)
                .toArray(String[]::new);
        String[] totalCols = Stream.concat(Arrays.stream(quantitativeFeatureNames),
                Arrays.stream(outputCols)).toArray(String[]::new);
        StringIndexer stringIndexer = new StringIndexer()
                .setInputCols(modelParams.categoricalFeatureNames())
                .setOutputCols(outputCols)
                .setHandleInvalid("keep");
        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(totalCols)
                .setOutputCol("assembledFeatures");
        PipelineModel pipelineModelFull = buildRegressionPipelineModel(modelParams, dataset, labelName, stringIndexer, vectorAssembler);
        StringIndexerModel stringIndexerModel = (StringIndexerModel) (pipelineModelFull.stages()[0]);
        DecisionTreeRegressionModel treeModel = (DecisionTreeRegressionModel) (pipelineModelFull.stages()[2]);
        double[] weights = treeModel.featureImportances().toArray();
        String[] features = getDecodedFeatureNames(stringIndexerModel, vectorAssembler.getInputCols());
        FeatureInfo featureInfo = new FeatureInfo(FeatureImportance.combine(features, weights));
        int numNodes = treeModel.numNodes();
        int depth = treeModel.depth();
        double evalResultSeenData = evalRmseRegressionPipeline(pipelineModelFull, labelName, dataset);
        double evalResultUnknownData = trainMode == TrainMode.SEEN_DATA_EVAL_ONLY ? 0.0 :
                evalRmseRegressionPipeline(buildRegressionPipelineModel(modelParams, trainingData, labelName, stringIndexer, vectorAssembler), labelName, testData);
        EvalResult evalResult = new EvalResult(MetricName.RMSE, evalResultSeenData, evalResultUnknownData);
        return new ModelInfo(pipelineModelFull, featureInfo, evalResult, numNodes, depth, (int) dataset.count());
    }
    @SuppressWarnings("deprecation")
    private static PipelineModel buildClassificationPipelineModel(ModelParams modelParams, Dataset<Row> dataset, VectorAssembler vectorAssembler, StringIndexer categoricalFeaturesIndexer, StringIndexer labelIndexer) {
        StringIndexerModel stringIndexerModel = categoricalFeaturesIndexer
                .fit(dataset);
        StringIndexerModel labelIndexerModel = labelIndexer
                .fit(dataset);
        DecisionTreeClassifier decisionTreeClassifier = new DecisionTreeClassifier()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("assembledFeatures")
                .setMaxBins(MAX_BINS)
                .setMinInfoGain(modelParams.minInfoGain())
                .setMinWeightFractionPerNode(modelParams.minWeightFractionPerNode())
                .setMinInstancesPerNode(modelParams.minInstancesPerNode())
                .setMaxDepth(modelParams.maxDepth());
        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(labelIndexerModel.labels());
        Pipeline pipeline = new Pipeline();
        pipeline.setStages(new PipelineStage[]{labelIndexerModel, stringIndexerModel, vectorAssembler, decisionTreeClassifier, labelConverter});
        return pipeline.fit(dataset);
    }

    private static PipelineModel buildRegressionPipelineModel(ModelParams modelParams, Dataset<Row> dataset, String labelName, StringIndexer stringIndexer, VectorAssembler vectorAssembler) {
        StringIndexerModel stringIndexerModel = stringIndexer
                .fit(dataset);
        DecisionTreeRegressor decisionTreeRegressor = new DecisionTreeRegressor()
                .setLabelCol(labelName)
                .setFeaturesCol("assembledFeatures")
                .setMaxBins(MAX_BINS)
                .setMinInfoGain(modelParams.minInfoGain())
                .setMinWeightFractionPerNode(modelParams.minWeightFractionPerNode())
                .setMinInstancesPerNode(modelParams.minInstancesPerNode())
                .setMaxDepth(modelParams.maxDepth());
        Pipeline pipeline = new Pipeline();
        pipeline.setStages(new PipelineStage[]{stringIndexerModel, vectorAssembler, decisionTreeRegressor});
        return pipeline.fit(dataset);
    }

    public static double evalRmseRegressionPipeline(PipelineModel pipelineModel, String labelName, Dataset<Row> dataset) {
        Dataset<Row> predictions = pipelineModel.transform(dataset);
        RegressionEvaluator evaluator = new RegressionEvaluator()
                .setLabelCol(labelName)
                .setPredictionCol("prediction")
                .setMetricName("rmse");
        return evaluator.evaluate(predictions);
    }

    public static double evalAccuracyErrorClassificationPipeline(PipelineModel pipelineModel, Dataset<Row> dataset) {
        Dataset<Row> predictions = pipelineModel.transform(dataset);
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("indexedLabel")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        return 1. - evaluator.evaluate(predictions);
    }

    private static String[] getDecodedFeatureNames(StringIndexerModel stringIndexerModel, String[] featureTNames) {
        String[] in = stringIndexerModel.getInOutCols()._1;
        String[] out = stringIndexerModel.getInOutCols()._2;
        Map<String, String> map = IntStream.range(0, in.length)
                .boxed()
                .collect(Collectors.toMap(o -> out[o], o -> in[o]));
        return Arrays.stream(featureTNames).map(s -> map.getOrDefault(s, s))
                .toArray(String[]::new);
    }

    public static StructType createStructType(Spot spot) {
        List<Feature<?, PokerGame>> features = spot.pokerSituation().getFeatures();
        StructType structType = new StructType();
        for (Feature<?, PokerGame> feature : features) {
            if (feature.featureType() == FeatureType.CATEGORICAL) {
                structType = structType.add(feature.name().name(), DataTypes.StringType, false);
            } else if (feature.featureType() == FeatureType.QUANTITATIVE) {
                structType = structType.add(feature.name().name(), DataTypes.DoubleType, false);
            }
        }
        return structType;
    }
}
