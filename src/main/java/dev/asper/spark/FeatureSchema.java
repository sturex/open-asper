package dev.asper.spark;

import dev.asper.advice.ActionMap;
import dev.asper.advice.ActionVector;
import dev.asper.advice.AmountTweak;
import dev.asper.advice.Decision;
import dev.asper.common.feature.Feature;
import dev.asper.poker.engine.*;
import dev.asper.poker.enums.Action;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.feature.IndexToString;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.ml.param.StringArrayParam;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum FeatureSchema {

    ACTION {
        @Override
        public Feature<?, ?> getLabelFeature() {
            return PokerFeatures.heroAction;
        }

        @Override
        public ModelType getModelType() {
            return ModelType.CLASSIFICATION;
        }

        @Override
        public DatasetType getDatasetType() {
            return DatasetType.ACTION;
        }

        @Override
        public List<ActionMap> extractActionMaps(Dataset<Row> decisionTypeDataset, PipelineModel pipelineModel) {
            Dataset<Row> probabilityRows = decisionTypeDataset.select("probability");
            IndexToString indexToString = (IndexToString) (pipelineModel.stages()[4]);
            StringArrayParam stringArrayParam = indexToString.labels();
            Action[] actions = Arrays.stream(indexToString.get(stringArrayParam).get()).map(Action::valueOf).toArray(Action[]::new);
            int dtLength = actions.length;
            Row[] rows = (Row[]) probabilityRows.collect();
            return Arrays.stream(rows).map(row -> {
                double[] probabilities = ((DenseVector) row.getAs("probability")).values();
                EnumMap<Action, Double> map = IntStream.range(0, dtLength)
                        .boxed()
                        .collect(Collectors.toMap(idx -> actions[idx], idx -> probabilities[idx], (aDouble, aDouble2) -> aDouble, () -> new EnumMap<>(Action.class)));
                return ActionMap.from(map);
            }).toList();
        }

    }, BRANCH {
        @Override
        public Feature<?, ?> getLabelFeature() {
            return PokerFeatures.heroBranch;
        }

        @Override
        public ModelType getModelType() {
            return ModelType.CLASSIFICATION;
        }

        @Override
        public DatasetType getDatasetType() {
            return DatasetType.ACTION;
        }

        @Override
        public List<Decision> extractDecisions(Dataset<Row> dataset, PipelineModel pipelineModel, List<PokerGame> pokerGames) {
            Dataset<Row> predictions = dataset.select("predictedLabel", "probability");
            IndexToString indexToString = (IndexToString) (pipelineModel.stages()[4]);
            StringArrayParam stringArrayParam = indexToString.labels();
            String[] labels = indexToString.get(stringArrayParam).get();
            Row[] rows = (Row[]) predictions.collect();
            return IntStream.range(0, pokerGames.size()).boxed()
                    .map((i) -> {
                        Row row = rows[i];
                        PokerGame pokerGame = pokerGames.get(i);
                        int potSize = pokerGame.getPotSize();
                        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                        int heroStackStub = hero.getStackStub();
                        int heroCallAmount = pokerGame.computeCallAmount(hero.getPreflopPosition());
                        Branch branch = getExpectedDecisionBranchByWeightingProbabilities(labels, row);
                        return branch.toDecision(potSize, heroCallAmount, heroStackStub);
                    })
                    .toList();
        }

    }, AMOUNT_BB {
        @Override
        public Feature<?, ?> getLabelFeature() {
            return PokerFeatures.heroAmountBb;
        }

        @Override
        public ModelType getModelType() {
            return ModelType.REGRESSION;
        }

        @Override
        public DatasetType getDatasetType() {
            return DatasetType.AMOUNT;
        }

        @Override
        public List<Integer> extractAmounts(Dataset<Row> decisionAmountDataset, PipelineModel pipelineModel, List<PokerGame> pokerGames, AmountTweak amountTweak) {
            double[] amountBbs = DatasetUtils.selectAsDoubleArray(decisionAmountDataset, "prediction");
            return IntStream.range(0, pokerGames.size()).boxed()
                    .map((i) -> (int) (amountTweak.apply(amountBbs[i] * pokerGames.get(i).getBbAmount())))
                    .toList();
        }

    }, DIFF_AMOUNT_BB {
        @Override
        public Feature<?, ?> getLabelFeature() {
            return PokerFeatures.heroDiffAmountBb;
        }

        @Override
        public ModelType getModelType() {
            return ModelType.REGRESSION;
        }

        @Override
        public DatasetType getDatasetType() {
            return DatasetType.AMOUNT;
        }

        @Override
        public List<Integer> extractAmounts(Dataset<Row> decisionAmountDataset, PipelineModel pipelineModel, List<PokerGame> pokerGames, AmountTweak amountTweak) {
            double[] diffAmountBbs = DatasetUtils.selectAsDoubleArray(decisionAmountDataset, "prediction");
            return IntStream.range(0, pokerGames.size()).boxed()
                    .map(i -> {
                        PokerGame pokerGame = pokerGames.get(i);
                        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                        int heroCallAmount = pokerGame.computeCallAmount(hero.getPreflopPosition());
                        return heroCallAmount + (int) (amountTweak.apply(diffAmountBbs[i] * pokerGame.getBbAmount()));
                    }).toList();
        }

    }, AMOUNT_BY_POT {
        @Override
        public Feature<?, ?> getLabelFeature() {
            return PokerFeatures.heroAmountByPot;
        }

        @Override
        public ModelType getModelType() {
            return ModelType.REGRESSION;
        }

        @Override
        public DatasetType getDatasetType() {
            return DatasetType.AMOUNT;
        }

        @Override
        public List<Integer> extractAmounts(Dataset<Row> decisionAmountDataset, PipelineModel pipelineModel, List<PokerGame> pokerGames, AmountTweak amountTweak) {
            double[] amountByPots = DatasetUtils.selectAsDoubleArray(decisionAmountDataset, "prediction");
            return IntStream.range(0, pokerGames.size()).boxed()
                    .map(i -> (int) (amountTweak.apply(amountByPots[i]) * pokerGames.get(i).getPotSize()))
                    .toList();
        }

    }, DIFF_AMOUNT_BY_POT {
        @Override
        public Feature<?, ?> getLabelFeature() {
            return PokerFeatures.heroDiffAmountByPot;
        }

        @Override
        public ModelType getModelType() {
            return ModelType.REGRESSION;
        }

        @Override
        public DatasetType getDatasetType() {
            return DatasetType.AMOUNT;
        }

        @Override
        public List<Integer> extractAmounts(Dataset<Row> decisionAmountDataset, PipelineModel pipelineModel, List<PokerGame> pokerGames, AmountTweak amountTweak) {
            double[] diffAmountByPots = DatasetUtils.selectAsDoubleArray(decisionAmountDataset, "prediction");
            return IntStream.range(0, pokerGames.size()).boxed()
                    .map(i -> {
                        PokerGame pokerGame = pokerGames.get(i);
                        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                        int heroCallAmount = pokerGame.computeCallAmount(hero.getPreflopPosition());
                        return heroCallAmount + (int) (amountTweak.apply(diffAmountByPots[i] * pokerGame.getPotSize()));
                    }).toList();
        }

    };


    private static Branch getExpectedDecisionBranchByWeightingProbabilities(String[] labels, Row row) {
        double[] probabilities = ((DenseVector) row.getAs("probability")).values();
        Branch[] decisionBranches = Arrays.stream(labels).map(Branch::valueOf).toArray(Branch[]::new);
        WeightedDecisionBranch[] branches = IntStream.range(0, labels.length)
                .filter(idx -> probabilities[idx] != 0)
                .mapToObj(idx -> new WeightedDecisionBranch(decisionBranches[idx], probabilities[idx]))
                .toArray(WeightedDecisionBranch[]::new);
        Map<Action, List<WeightedDecisionBranch>> branchesByDecisionType = Arrays.stream(branches)
                .collect(Collectors.groupingBy(branch -> branch.branch().getAction(), Collectors.toList()));
        List<WeightedDecisionBranch> maxSummarizedWeightBranches = branchesByDecisionType.entrySet().stream()
                .max(Comparator.comparingDouble(value -> value.getValue().stream()
                        .mapToDouble(WeightedDecisionBranch::weight).sum()))
                .map(Map.Entry::getValue)
                .orElseThrow();
        return maxSummarizedWeightBranches.stream()
                .max(Comparator.comparingDouble(WeightedDecisionBranch::weight))
                .orElseThrow()
                .branch();
    }

    public abstract Feature<?, ?> getLabelFeature();

    public abstract ModelType getModelType();

    public abstract DatasetType getDatasetType();

    public List<ActionMap> extractActionMaps(Dataset<Row> decisionTypeDataset, PipelineModel pipelineModel) {
        throw new RuntimeException("Not implemented extractDecisions for " + this);
    }

    public List<Integer> extractAmounts(Dataset<Row> decisionAmountDataset, PipelineModel pipelineModel, List<PokerGame> pokerGames, AmountTweak amountTweak) {
        throw new RuntimeException("Not implemented extractDecisionAmounts for " + this);
    }

    public List<Decision> extractDecisions(Dataset<Row> decisionAmountDataset, PipelineModel pipelineModel, List<PokerGame> pokerGames) {
        throw new RuntimeException("Not implemented extractDecisions for " + this);
    }

}
