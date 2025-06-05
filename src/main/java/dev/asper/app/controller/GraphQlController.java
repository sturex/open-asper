package dev.asper.app.controller;


import dev.asper.advice.*;
import dev.asper.app.component.PipelineModelRecoverException;
import dev.asper.app.config.DatasetSchemasConfig;
import dev.asper.app.entity.Model;
import dev.asper.app.entity.SolutionInfo;
import dev.asper.app.graphql.*;
import dev.asper.app.service.*;
import dev.asper.app.component.ClickhouseQueries;
import dev.asper.clickhouse.OpponentType;
import dev.asper.common.util.ExceptionHelper;
import dev.asper.app.component.ClickhouseGamesCollector;
import dev.asper.poker.ai.ActionAdvisorType;
import dev.asper.poker.ai.AdvisorInfo;
import dev.asper.poker.ai.AmountAdvisorType;
import dev.asper.poker.ai.ChartType;
import dev.asper.poker.chart.*;
import dev.asper.poker.engine.*;
import dev.asper.poker.engine.spot.PreflopSpot;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.engine.spot.Spots;
import dev.asper.poker.enums.Action;
import dev.asper.poker.enums.MiningType;
import dev.asper.poker.enums.SpecialSpot;
import dev.asper.poker.enums.Street;
import dev.asper.poker.card.CardCell;
import dev.asper.spark.FeatureSchema;
import dev.asper.spark.ModelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class GraphQlController {

    public static final int REAL_DATASET_FETCH_LIMIT = 100000;
    public static final double REAL_DATASET_WEIGHT_THRESHOLD = 0.01;
    private final AdviceService adviceService;
    private final SelfPlayService selfPlayService;
    private final DatasetService datasetService;
    private final PreflopActionChartService preflopActionChartService;
    private final PostflopActionChartService postflopActionChartService;
    private final PreflopAmountChartService preflopAmountChartService;
    private final PostflopAmountChartService postflopAmountChartService;
    private final ModelService modelService;
    private final SolutionService solutionService;
    private final SpecialSpotTweakService specialSpotTweakService;
    private final ClickhouseGamesCollector clickhouseGamesCollector;
    private final ClickhouseQueries clickhouseQueries;
    private final DatasetSchemasConfig datasetSchemasConfig;
    private final RealDatasetPredictionService realDatasetPredictionService;
    private final StatisticsService statisticsService;

    @Autowired
    public GraphQlController(AdviceService adviceService,
                             SelfPlayService selfPlayService,
                             DatasetService datasetService,
                             PreflopActionChartService preflopActionChartService,
                             PostflopActionChartService postflopActionChartService,
                             PreflopAmountChartService preflopAmountChartService,
                             PostflopAmountChartService postflopAmountChartService,
                             ModelService modelService,
                             SolutionService solutionService,
                             SpecialSpotTweakService specialSpotTweakService,
                             ClickhouseGamesCollector clickhouseGamesCollector,
                             ClickhouseQueries clickhouseQueries,
                             DatasetSchemasConfig datasetSchemasConfig,
                             RealDatasetPredictionService realDatasetPredictionService,
                             StatisticsService statisticsService) {
        this.adviceService = adviceService;
        this.selfPlayService = selfPlayService;
        this.datasetService = datasetService;
        this.preflopActionChartService = preflopActionChartService;
        this.postflopActionChartService = postflopActionChartService;
        this.preflopAmountChartService = preflopAmountChartService;
        this.postflopAmountChartService = postflopAmountChartService;
        this.modelService = modelService;
        this.solutionService = solutionService;
        this.specialSpotTweakService = specialSpotTweakService;
        this.clickhouseGamesCollector = clickhouseGamesCollector;
        this.clickhouseQueries = clickhouseQueries;
        this.datasetSchemasConfig = datasetSchemasConfig;
        this.realDatasetPredictionService = realDatasetPredictionService;
        this.statisticsService = statisticsService;
    }

    @SchemaMapping(typeName = "Query", field = "initStatisticsMap")
    public int initStatisticsMap() {
        statisticsService.initStatisticsMap();
        return 0;
    }

    @SchemaMapping(typeName = "Query", field = "csAdvice")
    public Advice csAdvice(@Argument PokerHandInput pokerHandInput) {
        return adviceService.adviseCS(pokerHandInput.toPokerHand());
    }


    @SchemaMapping(typeName = "Query", field = "realDatasetPreflopActionPredictions")
    public List<CardCellActionVector> realDatasetPreflopActionPredictions(@Argument String schemaName,
                                                                          @Argument ActionAdvisorType actionAdvisorType,
                                                                          @Argument String actionAdvisorName,
                                                                          @Argument ActionTweakInput actionTweakInput,
                                                                          @Argument String spot,
                                                                          @Argument Integer limit) throws PipelineModelRecoverException {
        ActionTweak actionTweak = actionTweakInput.toActionTweak();
        return realDatasetPredictionService.realDatasetPreflopActionPredictions(schemaName,
                actionAdvisorType,
                actionAdvisorName,
                actionTweak,
                Spots.fromStr(spot),
                limit == null ? REAL_DATASET_FETCH_LIMIT : limit);
    }

    @SchemaMapping(typeName = "Query", field = "realDatasetPostflopActionPredictions")
    public ActionVector realDatasetPostflopActionPredictions(@Argument String schemaName,
                                                             @Argument ActionAdvisorType actionAdvisorType,
                                                             @Argument String actionAdvisorName,
                                                             @Argument ActionTweakInput actionTweakInput,
                                                             @Argument String spot,
                                                             @Argument Integer limit) throws PipelineModelRecoverException {
        ActionTweak actionTweak = actionTweakInput.toActionTweak();
        return realDatasetPredictionService.realDatasetPostflopActionPredictions(schemaName,
                actionAdvisorType,
                actionAdvisorName,
                actionTweak,
                Spots.fromStr(spot),
                limit == null ? REAL_DATASET_FETCH_LIMIT : limit);
    }

    @SchemaMapping(typeName = "Query", field = "realDatasetPreflopAmountPredictions")
    public List<CardCellWeightedAmount> realDatasetPreflopAmountPredictions(@Argument String schemaName,
                                                                            @Argument AmountAdvisorType amountAdvisorType,
                                                                            @Argument String amountAdvisorName,
                                                                            @Argument AmountTweak amountTweak,
                                                                            @Argument String spot,
                                                                            @Argument Double minWeightThreshold,
                                                                            @Argument Integer limit) throws PipelineModelRecoverException {
        return realDatasetPredictionService.realDatasetPreflopAmountPredictions(schemaName,
                amountAdvisorType,
                amountAdvisorName,
                amountTweak,
                Spots.fromStr(spot),
                minWeightThreshold == null ? REAL_DATASET_WEIGHT_THRESHOLD : minWeightThreshold,
                limit == null ? REAL_DATASET_FETCH_LIMIT : limit
        );
    }

    @SchemaMapping(typeName = "Query", field = "model")
    public Model model(@Argument String modelName) {
        return modelService.findModelByName(modelName);
    }

    @SchemaMapping(typeName = "Query", field = "deleteSolution")
    public int deleteSolution(@Argument String name) {
        solutionService.deleteSolution(name);
        return 0;
    }

    @SchemaMapping(typeName = "Query", field = "realDatasetPostflopAmountPredictions")
    public List<WeightedAmount> realDatasetPostflopAmountPredictions(@Argument String schemaName,
                                                                     @Argument String amountAdvisorName,
                                                                     @Argument String spot,
                                                                     @Argument Double minWeightThreshold,
                                                                     @Argument Integer limit) throws PipelineModelRecoverException {
        return realDatasetPredictionService.realDatasetPostflopAmountPredictions(schemaName,
                amountAdvisorName,
                Spots.fromStr(spot),
                minWeightThreshold == null ? REAL_DATASET_WEIGHT_THRESHOLD : minWeightThreshold,
                limit == null ? REAL_DATASET_FETCH_LIMIT : limit
        );
    }

    @SchemaMapping(typeName = "Query", field = "actionAdvisorNames")
    public List<ActionAdvisorInfo> actionAdvisorNames(@Argument String spot,
                                                      @Argument String mask) {
        Spot recoveredSpot = Spots.fromStr(spot);
        Street street = recoveredSpot.pokerSituation().getStreet();
        List<ActionAdvisorInfo> chartNames = switch (street) {
            case PF -> preflopActionChartService.readAllNames(spot, mask, ChartType.ACTION)
                    .stream().map(name -> new ActionAdvisorInfo(ActionAdvisorType.CHART_PREFLOP_ACTION, name)).toList();
            case FL, TN, RV -> postflopActionChartService.readAllNames(spot, mask, ChartType.ACTION)
                    .stream().map(name -> new ActionAdvisorInfo(ActionAdvisorType.CHART_POSTFLOP_ACTION, name)).toList();
        };
        List<ActionAdvisorInfo> modelNames = modelService.findModelNames(spot, mask, ModelType.CLASSIFICATION)
                .stream().map(name -> new ActionAdvisorInfo(ActionAdvisorType.ML_ACTION, name)).toList();
        return Stream.of(chartNames, modelNames).flatMap(Collection::stream).toList();
    }

    @SchemaMapping(typeName = "Query", field = "amountAdvisorNames")
    public List<AmountAdvisorInfo> amountAdvisorNames(@Argument String spot,
                                                      @Argument String mask) {
        Spot recoveredSpot = Spots.fromStr(spot);
        Street street = recoveredSpot.pokerSituation().getStreet();
        List<AmountAdvisorInfo> chartNames = switch (street) {
            case PF -> preflopActionChartService.readAllNames(spot, mask, ChartType.AMOUNT)
                    .stream().map(name -> new AmountAdvisorInfo(AmountAdvisorType.CHART_PREFLOP_AMOUNT, name)).toList();
            case FL, TN, RV -> postflopActionChartService.readAllNames(spot, mask, ChartType.AMOUNT)
                    .stream().map(name -> new AmountAdvisorInfo(AmountAdvisorType.CHART_POSTFLOP_AMOUNT, name)).toList();
        };
        List<AmountAdvisorInfo> modelNames = modelService.findModelNames(spot, mask, ModelType.REGRESSION)
                .stream().map(name -> new AmountAdvisorInfo(AmountAdvisorType.ML_AMOUNT, name)).toList();
        return Stream.of(chartNames, modelNames).flatMap(Collection::stream).toList();
    }

    @SchemaMapping(typeName = "Query", field = "solutionNames")
    public List<String> solutionNames(@Argument String mask) {
        return solutionService.findAllNames(mask == null ? "" : mask);
    }

    @SchemaMapping(typeName = "Query", field = "preflopActionChartBodyNames")
    public List<String> preflopActionChartBodyNames(@Argument String spot,
                                                    @Argument String mask) {
        return preflopActionChartService.readAllNames(spot, mask == null ? "" : mask, ChartType.ACTION);
    }

    @SchemaMapping(typeName = "Query", field = "preflopAmountChartBodyNames")
    public List<String> preflopAmountChartBodyNames(@Argument String spot,
                                                    @Argument String mask) {
        return preflopAmountChartService.readAllNames(spot, mask == null ? "" : mask, ChartType.AMOUNT);
    }

    @SchemaMapping(typeName = "Query", field = "postflopActionChartBodyNames")
    public List<String> postflopActionChartBodyNames(@Argument String spot,
                                                     @Argument String mask) {
        return postflopActionChartService.readAllNames(spot, mask == null ? "" : mask, ChartType.ACTION);
    }

    @SchemaMapping(typeName = "Query", field = "postflopAmountChartBodyNames")
    public List<String> postflopAmountChartBodyNames(@Argument String spot,
                                                     @Argument String mask) {
        return postflopAmountChartService.readAllNames(spot, mask == null ? "" : mask, ChartType.AMOUNT);
    }

    @SchemaMapping(typeName = "Query", field = "solution")
    public SolutionInfoOutput solution(@Argument String solutionName) {
        SolutionInfo solutionInfo = solutionService.findByName(solutionName).orElseThrow(() -> new RuntimeException("Solution not found, solutionName=%s".formatted(solutionName)));
        List<AdvisorInfo> advisorInfos = solutionService.recoverAdvisorInfos(solutionInfo).stream()
                .toList();
        return new SolutionInfoOutput(advisorInfos);
    }

    @SchemaMapping(typeName = "Query", field = "createCustomSolution")
    public SolutionInfoOutput createCustomSolution(@Argument String solutionName,
                                                   @Argument List<SpotAdvisorInput> spotAdvisorInputs) {
        List<AdvisorInfo> advisorInfos = spotAdvisorInputs.stream().map(s -> {
                    Spot spot = Spots.fromStr(s.spot());
                    ActionAdvisorInput actionAdvisorInput = s.actionAdvisorInput();
                    AmountAdvisorInput amountAdvisorInput = s.amountAdvisorInput();
                    ActionTweak actionTweak = s.actionTweakInput().toActionTweak();
                    AmountTweak amountTweak = s.amountTweakInput().toAmountTweak();
                    return new AdvisorInfo(spot.toSpotInfo(),
                            actionAdvisorInput.name(),
                            actionAdvisorInput.actionAdvisorType(),
                            actionTweak,
                            amountAdvisorInput.name(),
                            amountAdvisorInput.amountAdvisorType(),
                            amountTweak);
                })
                .toList();
        SolutionInfo solutionInfo = solutionService.create(advisorInfos, solutionName, "custom solution");
        return new SolutionInfoOutput(advisorInfos.stream().toList());
    }

    @SchemaMapping(typeName = "Query", field = "copySolution")
    public SolutionInfoOutput copySolution(@Argument String solutionName,
                                           @Argument String newSolutionName) {
        SolutionInfo solutionInfo = solutionService.copySolution(solutionName, newSolutionName, "Copy of %s".formatted(solutionName));
        return solution(newSolutionName);
    }

    @SchemaMapping(typeName = "Query", field = "pfDonorSolution")
    public String pfDonorSolution(@Argument String solutionName,
                                  @Argument String donorSolutionName) {
        SolutionInfo solutionInfo = solutionService.pfDonorSolution(solutionName, donorSolutionName);
        return solutionName;
    }

    @SchemaMapping(typeName = "Query", field = "updateSolution")
    public SolutionInfoOutput updateSolution(@Argument String solutionName,
                                             @Argument String newSolutionName,
                                             @Argument List<SpotAdvisorInput> spotAdvisorInputs) {
        SolutionInfo solutionToUpdate = solutionService.findByName(solutionName)
                .orElseThrow(() -> new RuntimeException("Solution %s not found".formatted(solutionName)));
        List<AdvisorInfo> advisorToUpdate = solutionService.recoverAdvisorInfos(solutionToUpdate);
        Map<Spot, SpotAdvisorInput> bySpot = spotAdvisorInputs.stream().collect(Collectors.toMap(spotAdvisorInput -> Spots.fromStr(spotAdvisorInput.spot()), Function.identity()));
        List<AdvisorInfo> updated = advisorToUpdate.stream()
                .map(advisorInfo -> {
                    SpotInfo spotInfo = advisorInfo.spotInfo();
                    Spot spot = spotInfo.spot();
                    return Optional.ofNullable(bySpot.get(spot))
                            .map(ai -> {
                                ActionAdvisorInput actionAdvisorInput = ai.actionAdvisorInput();
                                AmountAdvisorInput amountAdvisorInput = ai.amountAdvisorInput();
                                ActionTweak actionTweak = ai.actionTweakInput().toActionTweak();
                                AmountTweak amountTweak = ai.amountTweakInput().toAmountTweak();
                                return new AdvisorInfo(spotInfo,
                                        actionAdvisorInput == null ? advisorInfo.actionAdvisorName() : actionAdvisorInput.name(),
                                        actionAdvisorInput == null ? advisorInfo.actionAdvisorType() : actionAdvisorInput.actionAdvisorType(),
                                        actionAdvisorInput == null ? ActionTweak.randomByIdentity : actionTweak,
                                        amountAdvisorInput == null ? advisorInfo.amountAdvisorName() : amountAdvisorInput.name(),
                                        amountAdvisorInput == null ? advisorInfo.amountAdvisorType() : amountAdvisorInput.amountAdvisorType(),
                                        amountAdvisorInput == null ? AmountTweak.identity : amountTweak);
                            })
                            .orElse(advisorInfo);
                })
                .toList();
        SolutionInfo solutionInfo = newSolutionName == null ?
                solutionService.update(updated, solutionName, "Updated with some new advisor infos.") :
                solutionService.create(updated, newSolutionName, "Copied from %s and updated with some new advisor infos".formatted(solutionName));
        return new SolutionInfoOutput(updated);
    }

    @SchemaMapping(typeName = "Query", field = "spotInfos")
    public List<SpotInfo> spotInfos(@Argument Street street) {
        return street == null ? Spots.spots.stream()
                .map(Spot::toSpotInfo).toList() : Spots.spots(street).stream()
                .map(Spot::toSpotInfo).toList();
    }

    @SchemaMapping(typeName = "Query", field = "readAllSpecialSpotTweakNames")
    public List<String> readAllSpecialSpotTweakNames() {
        return specialSpotTweakService.readAllNames();
    }

    @SchemaMapping(typeName = "Query", field = "preflopActionChart")
    public List<CardCellActionVector> preflopActionChart(@Argument String name) {
        PreflopActionChart preflopActionChart = preflopActionChartService.findByName(name);
        return preflopActionChart.getCardCellActionMaps().stream()
                .map(c -> new CardCellActionVector(c.cardCell(), c.actionMap().toActionVector()))
                .toList();
    }

    @SchemaMapping(typeName = "Query", field = "preflopAmountChart")
    public List<CardCellWeightedRandomAmountOutput> preflopAmountChart(@Argument String name) {
        PreflopAmountChart preflopAmountChart = preflopAmountChartService.findByName(name);
        return preflopAmountChart.getCardCellWeightedRandomAmounts().stream()
                .map(CardCellWeightedRandomAmountOutput::from)
                .toList();
    }

    @SchemaMapping(typeName = "Query", field = "postflopActionChart")
    public ActionVector postflopActionChart(@Argument String name) {
        PostflopActionChart postflopActionChart = postflopActionChartService.findByName(name);
        return postflopActionChart.actionVector();
    }

    @SchemaMapping(typeName = "Query", field = "postflopAmountChart")
    public List<WeightedAmount> postflopAmountChart(@Argument String name) {
        PostflopAmountChart postflopAmountChart = postflopAmountChartService.findByName(name);
        return WeightedRandomAmountOutput.from(postflopAmountChart.getWeightedAmounts());
    }

    @SchemaMapping(typeName = "Query", field = "advice")
    public Advice advice(@Argument PokerHandInput pokerHandInput,
                         @Argument String solutionName,
                         @Argument String specialSpotCollectionName) {
        PokerHand pokerHand = pokerHandInput.toPokerHand();
        SpecialSpotTweakCollection specialSpotTweakCollection = specialSpotCollectionName == null ? SpecialSpotTweakCollection.identity : specialSpotTweakService.getByName(specialSpotCollectionName);
        return adviceService.advice(pokerHand, solutionName, specialSpotTweakCollection);
    }

    @SchemaMapping(typeName = "Query", field = "trainAll")
    public List<String> trainAll() {
        return modelService.trainAll();
    }

    @SchemaMapping(typeName = "Query", field = "fixSolutions")
    public List<String> fixSolutions(@Argument List<String> solutionNames,
                                     @Argument String donorSolutionName) {
        solutionService.fixSolutions(solutionNames, donorSolutionName);
        return solutionNames;
    }

    @SchemaMapping(typeName = "Query", field = "createSolutionPool")
    public List<String> createSolutionPool(@Argument List<String> solutionNames) {
        adviceService.createSolutionPool(solutionNames);
        return solutionNames;
    }

    @SchemaMapping(typeName = "Query", field = "createSpecialSpotTweak")
    public SpecialSpotTweak createSpecialSpotTweak(@Argument String name,
                                                   @Argument List<SpecialSpotTweakInput> specialSpotTweakInputs) {
        SpecialSpotTweakCollection specialSpotTweakCollection = specialSpotTweakService.createOrUpdateSpecialSpotTweakCollection(name,
                specialSpotTweakInputs);
        EnumMap<SpecialSpot, ActionMap> collection = specialSpotTweakCollection.collection();
        return new SpecialSpotTweak(name, collection.entrySet().stream()
                .map(e -> new SpecialSpotTweakOutput(e.getKey(), e.getValue().toActionVector()))
                .toList());
    }

    @SchemaMapping(typeName = "Query", field = "specialSpotTweak")
    public SpecialSpotTweak specialSpotTweak(@Argument String name) {
        SpecialSpotTweakCollection specialSpotTweakCollection = specialSpotTweakService.getByName(name);
        EnumMap<SpecialSpot, ActionMap> collection = specialSpotTweakCollection.collection();
        return new SpecialSpotTweak(name, collection.entrySet().stream()
                .map(e -> new SpecialSpotTweakOutput(e.getKey(), e.getValue().toActionVector()))
                .toList());
    }

    @SchemaMapping(typeName = "Query", field = "createPlayersTable")
    public int createPlayersTable(@Argument String pokerGamesSchemaName,
                                  @Argument String pokerGamesTableName,
                                  @Argument String playersSchemaName) {
        clickhouseQueries.createPlayersTable(pokerGamesSchemaName, pokerGamesTableName, playersSchemaName);
        return 0;
    }

    @SchemaMapping(typeName = "Query", field = "play")
    public int play(@Argument List<SolutionAliasInput> solutionAliasInputs,
                    @Argument SelfPlayPropertiesInput selfPlayPropertiesInput,
                    @Argument PokerGamesDatasetInput pokerGamesDatasetInput,
                    @Argument HandHistoryDatasetInput handHistoryDatasetInput) {
        ExceptionHelper.throwIf(!pokerGamesDatasetInput.isEnabled() && !handHistoryDatasetInput.isEnabled(), "Both datasets are disabled to save in.");

        List<PokerGame> pokerGames = new ArrayList<>();
        for (int i = 0; i < selfPlayPropertiesInput.batchCount(); i++) {
            pokerGames.addAll(selfPlayService.play(new HashSet<>(solutionAliasInputs), selfPlayPropertiesInput));
        }
        if (pokerGamesDatasetInput.isEnabled()) {
            clickhouseGamesCollector.collectPokerGames(pokerGamesDatasetInput.schemaName(),
                    pokerGamesDatasetInput.tableName() == null ? UUID.randomUUID().toString() : pokerGamesDatasetInput.tableName(),
                    pokerGames,
                    MiningType.OPEN);
        }
        if (handHistoryDatasetInput.isEnabled()) {
            handHistoryDatasetInput.playerNames().forEach(playerName -> clickhouseGamesCollector.collectHandHistories(handHistoryDatasetInput.schemaName(),
                    handHistoryDatasetInput.tableName() == null ? UUID.randomUUID().toString() : handHistoryDatasetInput.tableName(),
                    pokerGames,
                    playerName));
        }
        return pokerGames.size();
    }

    @SchemaMapping(typeName = "Query", field = "createDatasets")
    public int createDatasets(@Argument String pokerGamesSchemaName,
                              @Argument String pokerGamesTableName,
                              @Argument String datasetsSchemaName) {
        return datasetService.createDatasetsAsync(pokerGamesSchemaName, pokerGamesTableName, datasetsSchemaName);
    }

    @SchemaMapping(typeName = "Query", field = "createHoldemStatDatasets")
    public int createHoldemStatDatasets(@Argument String pokerGamesSchemaName,
                                        @Argument String pokerGamesTableName,
                                        @Argument String datasetsSchemaName) {
        return datasetService.createHoldemStatDatasetsAsync(pokerGamesSchemaName, pokerGamesTableName, datasetsSchemaName);
    }

    @SchemaMapping(typeName = "Query", field = "createPreflopActionChart")
    public List<CardCellActionVector> createPreflopActionChart(@Argument String name,
                                                               @Argument String spot,
                                                               @Argument List<CardCellActionVectorInput> cardCellActionVectorInputs,
                                                               @Argument ActionVectorInput defaultActionVectorInput) {
        PreflopActionChart preflopActionChart = preflopActionChartService.createOrUpdateChart(name,
                PreflopSpot.valueOf(spot),
                cardCellActionVectorInputs.stream().map(CardCellActionVectorInput::toCardCellActionMap).toList());
        return preflopActionChart.getCardCellActionMaps().stream()
                .map(c -> new CardCellActionVector(c.cardCell(), c.actionMap().toActionVector()))
                .toList();
    }

    @SchemaMapping(typeName = "Query", field = "createPostflopActionChart")
    public ActionVector createPostflopActionChart(@Argument String name,
                                                  @Argument String spot,
                                                  @Argument ActionVectorInput actionVectorInput) {
        PostflopActionChart postflopActionChart = postflopActionChartService.createOrUpdateChart(name,
                Spots.fromStr(spot),
                actionVectorInput.toActionMap());
        return postflopActionChart.getActionMap().toActionVector();
    }

    @SchemaMapping(typeName = "Query", field = "createPreflopAmountChart")
    public List<CardCellWeightedRandomAmountOutput> createPreflopAmountChart(@Argument String name,
                                                                             @Argument String spot,
                                                                             @Argument List<CardCellWeightedAmountInput> cardCellWeightedAmountInputs) {
        Map<CardCell, List<WeightedAmountInput>> byCardCell = cardCellWeightedAmountInputs.stream()
                .collect(Collectors.groupingBy(CardCellWeightedAmountInput::cardCell, Collectors.flatMapping(c -> c.weightedAmountInputs().stream(), Collectors.toList())));
        List<CardCellWeightedRandomAmount> cardCellWeightedRandomAmounts = byCardCell.entrySet().stream().map(e -> {
            CardCell cardCell = e.getKey();
            List<WeightedAmount> weightedAmounts = e.getValue().stream()
                    .map(WeightedAmountInput::toWeightedAmount)
                    .toList();
            WeightedRandomAmount wra = new WeightedRandomAmount(weightedAmounts);
            return new CardCellWeightedRandomAmount(cardCell, wra);
        }).toList();
        PreflopAmountChart preflopAmountChart = preflopAmountChartService.createOrUpdateChart(name,
                PreflopSpot.valueOf(spot),
                cardCellWeightedRandomAmounts);
        return preflopAmountChart.getCardCellWeightedRandomAmounts().stream()
                .map(CardCellWeightedRandomAmountOutput::from)
                .toList();
    }

    @SchemaMapping(typeName = "Query", field = "createPostflopAmountChart")
    public List<WeightedAmount> createPostflopAmountChart(@Argument String name,
                                                          @Argument String spot,
                                                          @Argument List<WeightedAmountInput> weightedAmountInputs) {
        List<WeightedAmount> weightedAmounts = weightedAmountInputs.stream().map(WeightedAmountInput::toWeightedAmount).toList();
        WeightedRandomAmount weightedRandomAmount = new WeightedRandomAmount(weightedAmounts);
        Spot recoveredSpot = Spots.fromStr(spot);
        PostflopAmountChart postflopAmountChart = postflopAmountChartService.createChart(name,
                recoveredSpot,
                weightedRandomAmount);
        return WeightedRandomAmountOutput.from(postflopAmountChart.getWeightedRandomAmount().getWeightedAmounts());
    }

    @SchemaMapping(typeName = "Query", field = "createChartSolution")
    public SolutionInfoOutput createChartSolution(@Argument String datasetSchemaName,
                                                  @Argument String playersSchemaName,
                                                  @Argument OpponentType opponentType,
                                                  @Argument List<String> playerNames) {
        OpponentType nonNullOpponentType = opponentType == null ? OpponentType.OVERALL : opponentType;
        String solutionName = "CHART_SOLUTION_" + nonNullOpponentType;
        SolutionInfo solutionInfo = solutionService.findByName(solutionName)
                .orElseGet(() -> solutionService.createChartSolution(datasetSchemaName,
                        playersSchemaName == null ? datasetSchemaName : playersSchemaName,
                        nonNullOpponentType,
                        playerNames == null ? Collections.emptyList() : playerNames,
                        solutionName));
        List<AdvisorInfo> advisorInfos = solutionService.recoverAdvisorInfos(solutionInfo).stream()
                .toList();
        return new SolutionInfoOutput(advisorInfos);
    }

    @SchemaMapping(typeName = "Query", field = "createChartSolutions")
    public int createChartSolutions(@Argument String datasetSchemaName,
                                    @Argument String playersSchemaName,
                                    @Argument List<OpponentType> opponentTypes,
                                    @Argument List<String> playerNames) {
        opponentTypes.parallelStream().forEach(opponentType -> createChartSolution(datasetSchemaName, playersSchemaName, opponentType, playerNames));
        return 0;
    }

    @SchemaMapping(typeName = "Query", field = "createMlSolution")
    public SolutionInfoOutput createMlSolution(@Argument String datasetsSchemaName,
                                               @Argument String playersSchemaName,
                                               @Argument OpponentType opponentType,
                                               @Argument DecisionTreeParamsInput decisionTreeParamsInput,
                                               @Argument List<String> playerNames,
                                               @Argument boolean profitableHandsOnly,
                                               @Argument String identity) {
        // TODO move to solution service
        List<ModelInput> modelInputs = Spots.stream()
                .map(spot -> {
                    PokerSituation pokerSituation = spot.pokerSituation();
                    EnumSet<Action> actionAllowedActions = pokerSituation.actionAllowedActions();
                    String mlActionDatasetViewQuery = clickhouseQueries.createMlDatasetViewQuery(datasetsSchemaName, spot.name(), playersSchemaName, opponentType.playerStatFilters(), actionAllowedActions, playerNames, profitableHandsOnly);
                    String mlAmountDatasetViewQuery = clickhouseQueries.createMlDatasetViewQuery(datasetsSchemaName, spot.name(), playersSchemaName, opponentType.playerStatFilters(), Action.rAction, playerNames, profitableHandsOnly);
                    FeatureSchema actionFeatureSchema = spot.actionFeatureSchema();
                    FeatureSchema amountFeatureSchema = spot.amountFeatureSchema();
                    String mlActionFallbackDatasetViewQuery = clickhouseQueries.createMlDatasetViewQuery(datasetsSchemaName, spot.name(), playersSchemaName, OpponentType.OVERALL.playerStatFilters(), actionAllowedActions, playerNames, profitableHandsOnly);
                    String mlAmountFallbackDatasetViewQuery = clickhouseQueries.createMlDatasetViewQuery(datasetsSchemaName, spot.name(), playersSchemaName, OpponentType.OVERALL.playerStatFilters(), Action.rAction, playerNames, profitableHandsOnly);
                    String actionModelName = createModelName(spot,
                            actionFeatureSchema,
                            opponentType,
                            decisionTreeParamsInput,
                            identity, profitableHandsOnly);
                    String amountModelName = createModelName(spot,
                            amountFeatureSchema,
                            opponentType,
                            decisionTreeParamsInput,
                            identity, profitableHandsOnly);
                    ModelInput actionModelInput = createModelInput(pokerSituation, spot, actionFeatureSchema, decisionTreeParamsInput, mlActionDatasetViewQuery, mlActionFallbackDatasetViewQuery, actionModelName);
                    ModelInput amountModelInput = createModelInput(pokerSituation, spot, amountFeatureSchema, decisionTreeParamsInput, mlAmountDatasetViewQuery, mlAmountFallbackDatasetViewQuery, amountModelName);
                    return Stream.of(actionModelInput, amountModelInput);
                })
                .flatMap(Function.identity())
                .toList();
        List<Model> models = modelService.createModels(modelInputs);


        Map<String, List<Model>> bySpot = models.stream().collect(Collectors.groupingBy(Model::getSpot, Collectors.toList()));

        List<AdvisorInfo> advisorInfos = bySpot.entrySet().stream()
                .map(e -> {
                    Spot spot = Spots.fromStr(e.getKey());
                    List<Model> m = e.getValue();

                    String actionModelName = m.stream().filter(model -> model.getModelType() == ModelType.CLASSIFICATION).findFirst().orElseThrow().getName();
                    String amountModelName = m.stream().filter(model -> model.getModelType() == ModelType.REGRESSION).findFirst().orElseThrow().getName();

                    return new AdvisorInfo(spot.toSpotInfo(), actionModelName, ActionAdvisorType.ML_ACTION, spot.actionTweak(), amountModelName, AmountAdvisorType.ML_AMOUNT, spot.amountTweak());
                })
                .toList();

        String solutionName = createSolutionName(opponentType,
                decisionTreeParamsInput,
                identity, profitableHandsOnly);
        SolutionInfo solutionInfo = solutionService.create(advisorInfos, solutionName, "ML Solution");
        return new SolutionInfoOutput(advisorInfos);
    }


    private Model getOrCreateModel(PokerSituation pokerSituation,
                                   Spot spot,
                                   FeatureSchema featureSchema,
                                   DecisionTreeParamsInput decisionTreeParamsInput,
                                   String datasetQuery,
                                   String fallbackQuery,
                                   String modelName) {
        try {
            return modelService.findModelByName(modelName);
        } catch (Exception ignored) {
            ModelInput modelInput = createModelInput(pokerSituation, spot, featureSchema, decisionTreeParamsInput, datasetQuery, fallbackQuery, modelName);
            return modelService.createModel(modelInput);
        }
    }

    private ModelInput createModelInput(PokerSituation pokerSituation, Spot spot, FeatureSchema featureSchema, DecisionTreeParamsInput decisionTreeParamsInput, String datasetQuery, String fallbackQuery, String modelName) {
        return ModelInput.builder()
                .name(modelName)
                .decisionTreeParamsInput(decisionTreeParamsInput)
                .featureSchema(featureSchema)
                .pokerSituation(pokerSituation)
                .description("Regular model")
                .spot(spot)
                .datasetQuery(datasetQuery)
                .fallbackQuery(fallbackQuery)
                .build();
    }

    private String createSolutionName(OpponentType opponentType,
                                      DecisionTreeParamsInput decisionTreeParamsInput,
                                      String identity,
                                      boolean profitableHandsOnly) {
        return "ML_SOLUTION_" + ((identity.isEmpty() ? "" : identity + "_") + opponentType + "_" + (profitableHandsOnly ? "PHO_" : "") + decisionTreeParamsInput.toString());
    }

    private String createModelName(Spot spot,
                                   FeatureSchema featureSchema,
                                   OpponentType opponentType,
                                   DecisionTreeParamsInput decisionTreeParamsInput,
                                   String identity,
                                   boolean profitableHandsOnly) {
        return "ML_MODEL_" + ((identity.isEmpty() ? "" : identity + "_") + opponentType + "_" + featureSchema + "_" + spot + "_" + (profitableHandsOnly ? "PHO_" : "") + decisionTreeParamsInput.toString());
    }


}
