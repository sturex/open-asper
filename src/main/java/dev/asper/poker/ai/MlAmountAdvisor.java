package dev.asper.poker.ai;

import dev.asper.advice.AmountTweak;
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
import java.util.stream.IntStream;

public class MlAmountAdvisor implements AmountAdvisor {

    private final FeatureSchema featureSchema;
    private final PipelineModel pipelineModel;
    private final StructType structType;
    private final SparkSession sparkSession;
    private final AmountTweak amountTweak;

    public MlAmountAdvisor(FeatureSchema featureSchema,
                           PipelineModel pipelineModel,
                           StructType structType,
                           SparkSession sparkSession,
                           AmountTweak amountTweak) {
        this.featureSchema = featureSchema;
        this.pipelineModel = pipelineModel;
        this.structType = structType;
        this.sparkSession = sparkSession;
        this.amountTweak = amountTweak;
    }

    @Override
    public List<AmountGame> adviceAmountGames(Spot spot, List<PokerGame> pokerGames) {
        List<? extends List<? extends Descriptor<?>>> descriptors = pokerGames.stream()
                .map(pokerGame -> spot.pokerSituation().getFeatures().stream()
                        .map(f -> f.descriptor(pokerGame))
                        .toList())
                .toList();
        Dataset<Row> transformed = transformToDataset(descriptors);
        List<Integer> amounts = featureSchema.extractAmounts(transformed, pipelineModel, pokerGames, amountTweak);
        return IntStream.range(0, pokerGames.size())
                .mapToObj(idx -> new AmountGame(pokerGames.get(idx), amounts.get(idx)))
                .toList();
    }


    public Dataset<Row> transformToDataset(List<? extends List<? extends Descriptor<?>>> descriptors) {
        Dataset<?> dataset = DatasetUtils.toDataset(sparkSession, descriptors, structType);
        return pipelineModel.transform(dataset);
    }

    @Override
    public AmountTweak getAmountTweak() {
        return amountTweak;
    }

}
