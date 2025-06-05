package dev.asper.app.service;

import dev.asper.advice.*;
import dev.asper.app.component.PipelineModelRecoverException;
import dev.asper.app.graphql.CardCellActionVector;
import dev.asper.app.graphql.CardCellWeightedAmount;
import dev.asper.clickhouse.ClickhouseProperties;
import dev.asper.common.feature.Descriptor;
import dev.asper.common.feature.Feature;
import dev.asper.poker.ai.ActionAdvisorType;
import dev.asper.poker.ai.AmountAdvisorType;
import dev.asper.poker.ai.MlActionAdvisor;
import dev.asper.poker.ai.MlAmountAdvisor;
import dev.asper.poker.chart.PostflopActionChart;
import dev.asper.poker.chart.PreflopActionChart;
import dev.asper.poker.chart.PreflopAmountChart;
import dev.asper.poker.engine.PokerFeatureName;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.enums.Action;
import dev.asper.poker.card.CardCell;
import dev.asper.spark.DatasetUtils;
import dev.asper.spark.FeatureSchema;
import dev.asper.spark.PipelineInfo;
import dev.asper.spark.PipelineUtils;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RealDatasetPredictionServiceImpl implements RealDatasetPredictionService {
    public static final String PREDICTION = "prediction";
    private final SparkSession sparkSession;
    private final ModelService modelService;
    private final AdviceService adviceService;
    private final PreflopActionChartService preflopActionChartService;
    private final PreflopAmountChartService preflopAmountChartService;
    private final PostflopActionChartService postflopActionChartService;
    private final ClickhouseProperties clickhouseProperties;

    @Autowired
    public RealDatasetPredictionServiceImpl(SparkSession sparkSession,
                                            ModelService modelService,
                                            AdviceService adviceService,
                                            PreflopActionChartService preflopActionChartService,
                                            PreflopAmountChartService preflopAmountChartService,
                                            PostflopActionChartService postflopActionChartService,
                                            ClickhouseProperties clickhouseProperties) {
        this.sparkSession = sparkSession;
        this.modelService = modelService;
        this.adviceService = adviceService;
        this.preflopActionChartService = preflopActionChartService;
        this.preflopAmountChartService = preflopAmountChartService;
        this.postflopActionChartService = postflopActionChartService;
        this.clickhouseProperties = clickhouseProperties;
    }

    record CardCellAction(CardCell cardCell, Action action) {
    }

    record CardCellAmount(CardCell cardCell, long amount) {
    }

    @Override
    public List<CardCellActionVector> realDatasetPreflopActionPredictions(String schemaName,
                                                                          ActionAdvisorType actionAdvisorType,
                                                                          String actionAdvisorName,
                                                                          ActionTweak actionTweak,
                                                                          Spot spot,
                                                                          int limit) throws PipelineModelRecoverException {
        return switch (actionAdvisorType) {
            case ML_ACTION -> {
                String tableName = spot.toString();
                Dataset<Row> rawDataset = readDataset(schemaName, tableName, limit);
                yield mlCardCellActionVectors(rawDataset, actionAdvisorName, actionTweak, spot);
            }
            case CHART_PREFLOP_ACTION -> preflopChartCardCellActionVectors(actionAdvisorName, actionTweak);
            default -> throw new IllegalStateException("Unexpected value: " + actionAdvisorType);
        };
    }

    @Override
    public List<CardCellWeightedAmount> realDatasetPreflopAmountPredictions(String schemaName,
                                                                            AmountAdvisorType amountAdvisorType,
                                                                            String amountAdvisorName,
                                                                            AmountTweak amountTweak,
                                                                            Spot spot,
                                                                            double minWeightThreshold,
                                                                            int limit) throws PipelineModelRecoverException {
        String tableName = spot.toString();
        FeatureSchema featureSchema = spot.amountFeatureSchema();
        double roundFactor = switch (featureSchema) {
            case AMOUNT_BB, DIFF_AMOUNT_BB -> 0.5;
            case AMOUNT_BY_POT -> 0.05;
            case DIFF_AMOUNT_BY_POT -> 0.1;
            default -> throw new IllegalStateException("Unexpected value: " + featureSchema);
        };
        Dataset<Row> rawDataset = readDataset(schemaName, tableName, limit);
        return switch (amountAdvisorType) {
            case ML_AMOUNT -> mlCardCellWeightedAmounts(rawDataset, amountAdvisorName, amountTweak, spot, roundFactor, minWeightThreshold);
            case CHART_PREFLOP_AMOUNT -> preflopAmountChartCardCellWeightedAmounts(amountAdvisorName, amountTweak, spot, roundFactor, minWeightThreshold);
            default -> throw new IllegalStateException("Unexpected value: " + amountAdvisorType);
        };
    }

    private List<CardCellWeightedAmount> preflopAmountChartCardCellWeightedAmounts(String amountAdvisorName,
                                                                                   AmountTweak amountTweak,
                                                                                   Spot spot,
                                                                                   double roundFactor,
                                                                                   double minWeightThreshold) {
        PreflopAmountChart preflopAmountChart = preflopAmountChartService.findByName(amountAdvisorName);
        List<CardCellWeightedRandomAmount> cardCellWeightedRandomAmounts = preflopAmountChart.getCardCellWeightedRandomAmounts();


        return cardCellWeightedRandomAmounts.stream()
                .map(w -> new CardCellWeightedAmount(w.cardCell(), w.weightedRandomAmount().getWeightedAmounts()))
                .toList();
    }

    private List<CardCellWeightedAmount> mlCardCellWeightedAmounts(Dataset<Row> rawDataset, String amountAdvisorName,
                                                                   AmountTweak amountTweak,
                                                                   Spot spot,
                                                                   double roundFactor,
                                                                   double minWeightThreshold) throws PipelineModelRecoverException {
        MlAmountAdvisor mlAmountAdvisor = adviceService.recoverMlAmountAdvisor(spot, amountAdvisorName, amountTweak);


        List<Row> rows = rawDataset.collectAsList();
        List<? extends List<? extends Descriptor<?>>> descriptors = recoverDescriptors(rows, spot.pokerSituation());

        Dataset<Row> transformed = mlAmountAdvisor.transformToDataset(descriptors);
        String cardCellName = PokerFeatureName.POCKET_CARDS_NAME.name();
        Dataset<Row> selectedColumns = transformed.select(cardCellName, PREDICTION);
        Row[] rowsx = (Row[]) selectedColumns.collect();
        List<CardCellAmount> cardCellAmounts = Arrays.stream(rowsx)
                .map(row -> {
                    CardCell cardCell = CardCell.from((String) row.getAs(cardCellName));
                    double amount = row.getAs(PREDICTION);
                    long rounded = Math.round(amount / roundFactor);
                    return new CardCellAmount(cardCell, rounded);
                })
                .toList();
        Map<CardCell, List<Long>> map = cardCellAmounts.stream()
                .collect(Collectors.groupingBy(CardCellAmount::cardCell,
                        Collectors.mapping(CardCellAmount::amount, Collectors.toList())));
        return map.entrySet().stream()
                .map(e -> {
                    CardCell cardCell = e.getKey();
                    List<Long> amounts = e.getValue();
                    Map<Long, Long> raws = amounts.stream()
                            .collect(Collectors.groupingBy(aLong -> aLong, Collectors.counting()));
                    long totalCountSum = raws.values().stream().mapToLong(Long::longValue).sum();
                    List<WeightedAmount> weightedAmounts = raws.entrySet().stream()
                            .map(val -> {
                                double amount = (double) val.getKey() * roundFactor;
                                double weight = (double) val.getValue() / (double) totalCountSum;
                                return new WeightedAmount(weight, amount);
                            })
                            .filter(weightedAmount -> weightedAmount.weight() > minWeightThreshold)
                            .sorted(Comparator.comparingDouble(WeightedAmount::weight).reversed())
                            .toList();
                    return new CardCellWeightedAmount(cardCell, weightedAmounts);
                })
                .toList();
    }

    @Override
    public ActionVector realDatasetPostflopActionPredictions(String schemaName,
                                                             ActionAdvisorType actionAdvisorType,
                                                             String actionAdvisorName,
                                                             ActionTweak actionTweak,
                                                             Spot spot,
                                                             int limit) throws PipelineModelRecoverException {
        String tableName = spot.toString();
        Dataset<Row> rawDataset = readDataset(schemaName, tableName, limit);
        return switch (actionAdvisorType) {
            case ML_ACTION -> mlActionVectors(rawDataset, actionAdvisorName, actionTweak, spot);
            case CHART_POSTFLOP_ACTION -> postflopChartActionVector(actionAdvisorName, actionTweak);
            default -> throw new IllegalStateException("Unexpected value: " + actionAdvisorType);
        };
    }

    private List<CardCellActionVector> mlCardCellActionVectors(Dataset<Row> rawDataset,
                                                               String actionAdvisorName,
                                                               ActionTweak actionTweak,
                                                               Spot spot) throws PipelineModelRecoverException {


        List<Row> rows = rawDataset.collectAsList();
        List<? extends List<? extends Descriptor<?>>> descriptors = recoverDescriptors(rows, spot.pokerSituation());
        MlActionAdvisor mlActionAdvisor = adviceService.recoverMlActionAdvisor(spot, actionAdvisorName, actionTweak);
        Dataset<Row> transformed = mlActionAdvisor.transformToDataset(descriptors);
        List<ActionMap> actionMaps = mlActionAdvisor.extractActionMaps(transformed);
        List<CardCellAction> cardCellActions = IntStream.range(0, rows.size())
                .mapToObj(idx -> {
                    ActionMap actionMap = actionMaps.get(idx);
                    Row row = rows.get(idx);
                    String pcn = row.getAs(PokerFeatureName.POCKET_CARDS_NAME.name());
                    CardCell cardCell = CardCell.from(pcn);
                    Action action = actionTweak.apply(actionMap);
                    return new CardCellAction(cardCell, action);
                }).toList();
        Map<CardCell, List<Action>> map = cardCellActions.stream()
                .collect(Collectors.groupingBy(CardCellAction::cardCell,
                        Collectors.mapping(CardCellAction::action, Collectors.toList())));
        return map.entrySet().stream().map(e -> {
            CardCell cardCell = e.getKey();
            List<Action> actions = e.getValue();
            EnumMap<Action, Integer> collect = actions.stream()
                    .collect(Collectors.groupingBy(action -> action,
                            () -> new EnumMap<>(Action.class),
                            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
            ActionMap actionMap = ActionMap.from(collect.getOrDefault(Action.F, 0),
                    collect.getOrDefault(Action.C, 0),
                    collect.getOrDefault(Action.A, 0),
                    collect.getOrDefault(Action.R, 0),
                    SolutionService.MIN_ACTION_VALUE_THRESHOLD);
            return new CardCellActionVector(cardCell, actionMap.toActionVector());
        }).toList();
    }

    private List<CardCellActionVector> preflopChartCardCellActionVectors(String actionAdvisorName, ActionTweak actionTweak) {
        PreflopActionChart preflopActionChart = preflopActionChartService.findByName(actionAdvisorName);
        List<CardCellAction> cardCellActions = Arrays.stream(CardCell.values())
                .flatMap(cardCell -> IntStream.range(0, 1000)
                        .mapToObj(idx -> {
                            ActionMap actionMap = preflopActionChart.adviceActionMap(cardCell);
                            Action action = actionTweak.apply(actionMap);
                            return new CardCellAction(cardCell, action);
                        })).toList();
        Map<CardCell, List<Action>> map = cardCellActions.stream()
                .collect(Collectors.groupingBy(CardCellAction::cardCell,
                        Collectors.mapping(CardCellAction::action, Collectors.toList())));
        return map.entrySet().stream().map(e -> {
            CardCell cardCell = e.getKey();
            List<Action> actions = e.getValue();
            EnumMap<Action, Integer> collect = actions.stream()
                    .collect(Collectors.groupingBy(action -> action,
                            () -> new EnumMap<>(Action.class),
                            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
            ActionMap actionMap = ActionMap.from(collect.getOrDefault(Action.F, 0),
                    collect.getOrDefault(Action.C, 0),
                    collect.getOrDefault(Action.A, 0),
                    collect.getOrDefault(Action.R, 0),
                    SolutionService.MIN_ACTION_VALUE_THRESHOLD);
            return new CardCellActionVector(cardCell, actionMap.toActionVector());
        }).toList();
    }

    private ActionVector postflopChartActionVector(String actionAdvisorName, ActionTweak actionTweak) {
        PostflopActionChart postflopActionChart = postflopActionChartService.findByName(actionAdvisorName);
        ActionMap actionMap = postflopActionChart.getActionMap();
        // 1000 times repeat to get averaged prediction
        Collection<Action> actions = IntStream.range(0, 1000)
                .mapToObj(idx -> actionTweak.apply(actionMap))
                .toList();
        ActionMap actionMap1000 = ActionMap.from(actions);
        return actionMap1000.toActionVector();
    }

    private ActionVector mlActionVectors(Dataset<Row> rawDataset, String actionAdvisorName,
                                         ActionTweak actionTweak,
                                         Spot spot) throws PipelineModelRecoverException {
        MlActionAdvisor mlActionAdvisor = adviceService.recoverMlActionAdvisor(spot, actionAdvisorName, actionTweak);


        List<Row> rows = rawDataset.collectAsList();
        List<? extends List<? extends Descriptor<?>>> descriptors = recoverDescriptors(rows, spot.pokerSituation());

        Dataset<Row> transformed = mlActionAdvisor.transformToDataset(descriptors);
        List<ActionMap> actionMaps = mlActionAdvisor.extractActionMaps(transformed);
        return ActionMap.average(actionMaps).toActionVector();
    }

    @Override
    public List<WeightedAmount> realDatasetPostflopAmountPredictions(String schemaName,
                                                                     String amountAdvisorName,
                                                                     Spot spot,
                                                                     double minWeightThreshold,
                                                                     int limit) throws PipelineModelRecoverException {
        String tableName = spot.toString();
        Dataset<Row> rawDataset = readDataset(schemaName, tableName, limit);

        List<Row> rows = rawDataset.collectAsList();


        List<? extends List<? extends Descriptor<?>>> descriptors = recoverDescriptors(rows, spot.pokerSituation());
        PipelineInfo pipelineInfo = modelService.recoverPipelineModel(amountAdvisorName);
        PipelineModel pipelineModel = pipelineInfo.pipelineModel();
        Dataset<?> dataset = DatasetUtils.toDataset(sparkSession, descriptors, PipelineUtils.createStructType(spot));
        Dataset<Row> transformed = pipelineModel.transform(dataset);
        Dataset<Row> selectedColumns = transformed.select("prediction");
        Row[] rowsx = (Row[]) selectedColumns.collect();
        double roundFactor = 0.05;
        List<Long> predictions = Arrays.stream(rowsx)
                .map(row -> {
                    double amount = row.getAs("prediction");
                    return Math.round(amount / roundFactor);
                }).toList();
        Map<Long, Long> raws = predictions.stream().collect(Collectors.groupingBy(aLong -> aLong, Collectors.counting()));
        long totalCountSum = raws.values().stream().mapToLong(Long::longValue).sum();
        return raws.entrySet().stream()
                .map(e -> {
                    double amount = (double) e.getKey() * roundFactor;
                    double weight = (double) e.getValue() / (double) totalCountSum;
                    return new WeightedAmount(weight, amount);
                })
                .filter(weightedAmount -> weightedAmount.weight() > minWeightThreshold)
                .sorted(Comparator.comparingDouble(WeightedAmount::weight).reversed())
                .toList();
    }

    private List<? extends List<? extends Descriptor<?>>> recoverDescriptors(List<Row> rows, PokerSituation pokerSituation) {
        List<Feature<?, PokerGame>> features = pokerSituation.getFeatures();
        return rows.stream()
                .map(row -> features.stream()
                        .map(f -> f.from(row.getAs(f.name().name())))
                        .collect(Collectors.toList()))
                .toList();
    }

    private Dataset<Row> readDataset(String schemaName, String tableName, int limit) {
        String query = String.format(Locale.US,
                """
                        select distinct * except ( created_at_timestamp ) from %s.%s %s
                        """,
                schemaName,
                tableName, limit == 0 ? "" : " order by rand() limit " + limit);
        return sparkSession.read()
                .format("org.apache.spark.sql.execution.datasources.jdbc.JdbcRelationProvider")
                .options(clickhouseProperties.options())
                .option("query", query)
                .load();
    }

}
