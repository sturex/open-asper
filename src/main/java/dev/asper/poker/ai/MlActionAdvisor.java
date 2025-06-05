package dev.asper.poker.ai;

import dev.asper.advice.ActionMap;
import dev.asper.advice.ActionTweak;
import dev.asper.common.feature.Descriptor;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.spark.DatasetUtils;
import dev.asper.spark.FeatureSchema;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class MlActionAdvisor implements ActionAdvisor {

    private final FeatureSchema featureSchema;

    private final PipelineModel pipelineModel;
    private final StructType structType;
    private final SparkSession sparkSession;

    private final ActionTweak actionTweak;

    public MlActionAdvisor(FeatureSchema featureSchema,
                           PipelineModel pipelineModel,
                           StructType structType,
                           SparkSession sparkSession,
                           ActionTweak actionTweak) {
        this.featureSchema = featureSchema;
        this.pipelineModel = pipelineModel;
        this.structType = structType;
        this.sparkSession = sparkSession;
        this.actionTweak = actionTweak;
    }

    public FeatureSchema getFeatureSchema() {
        return featureSchema;
    }

    public PipelineModel getPipelineModel() {
        return pipelineModel;
    }

    public StructType getStructType() {
        return structType;
    }

    @Override
    public List<ActionMapGame> adviceActionMap(Spot spot, List<PokerGame> pokerGames) {
        List<? extends List<? extends Descriptor<?>>> descriptors = pokerGames.stream()
                .map(pokerGame -> spot.pokerSituation().getFeatures().stream()
                        .map(f -> f.descriptor(pokerGame))
                        .toList())
                .toList();
        List<ActionMap> actionMaps = transform(descriptors);
        return IntStream.range(0, pokerGames.size())
                .mapToObj(idx -> {
                    PokerGame pokerGame = pokerGames.get(idx);
                    ActionMap actionMap = actionMaps.get(idx);
                    return new ActionMapGame(pokerGame, actionMap);
                }).toList();
    }

    public List<ActionMap> transform(List<? extends List<? extends Descriptor<?>>> descriptors) {
        Dataset<Row> transformed = transformToDataset(descriptors);
        return extractActionMaps(transformed);
    }

    public List<ActionMap> extractActionMaps(Dataset<Row> transformed) {
        return featureSchema.extractActionMaps(transformed, pipelineModel);
    }

    public Dataset<Row> transformToDataset(List<? extends List<? extends Descriptor<?>>> descriptors) {
        Dataset<?> dataset = DatasetUtils.toDataset(sparkSession, descriptors, structType);
        return pipelineModel.transform(dataset);
    }

    @Override
    public ActionTweak getActionTweak() {
        return actionTweak;
    }

}
