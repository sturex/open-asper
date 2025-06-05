package dev.asper.app.service;

import dev.asper.advice.*;
import dev.asper.app.entity.Model;
import dev.asper.app.entity.base.BaseEntity;
import dev.asper.app.repository.spec.SolutionSpecification;
import dev.asper.clickhouse.OpponentType;
import dev.asper.clickhouse.PlayerStatFilter;
import dev.asper.poker.ai.ActionAdvisorType;
import dev.asper.poker.ai.AdvisorInfo;
import dev.asper.app.entity.SolutionInfo;
import dev.asper.app.repository.SolutionInfoRepository;
import dev.asper.app.component.ClickhouseQueries;
import dev.asper.common.util.ByteArraySerializer;
import dev.asper.poker.ai.AmountAdvisorType;
import dev.asper.poker.chart.*;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.engine.spot.PreflopSpot;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.engine.spot.Spots;
import dev.asper.poker.enums.Street;
import dev.asper.spark.ModelStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SolutionServiceImpl implements SolutionService {
    private static final double MIN_AMOUNT_WEIGHT_THRESHOLD = 0.05;
    private final SolutionInfoRepository solutionInfoRepository;
    private final ClickhouseQueries clickhouseQueries;
    private final PreflopActionChartService preflopActionChartService;
    private final PreflopAmountChartService preflopAmountChartService;
    private final PostflopActionChartService postflopActionChartService;
    private final PostflopAmountChartService postflopAmountChartService;
    private final ModelService modelService;

    @Autowired
    public SolutionServiceImpl(SolutionInfoRepository solutionInfoRepository,
                               ClickhouseQueries clickhouseQueries,
                               PreflopActionChartService preflopActionChartService,
                               PreflopAmountChartService preflopAmountChartService,
                               PostflopActionChartService postflopActionChartService,
                               PostflopAmountChartService postflopAmountChartService,
                               ModelService modelService) {
        this.solutionInfoRepository = solutionInfoRepository;
        this.clickhouseQueries = clickhouseQueries;
        this.preflopActionChartService = preflopActionChartService;
        this.preflopAmountChartService = preflopAmountChartService;
        this.postflopActionChartService = postflopActionChartService;
        this.postflopAmountChartService = postflopAmountChartService;
        this.modelService = modelService;
    }

    @Override
    public Optional<SolutionInfo> findByName(String name) {
        return solutionInfoRepository.findByName(name);
    }

    @Override
    public Set<SolutionInfo> findByNameIn(Collection<String> names) {
        return solutionInfoRepository.findByNameIn(names);
    }


    @Override
    public SolutionInfo create(List<AdvisorInfo> advisorInfos, String solutionName, String description) {
        byte[] body = ByteArraySerializer.toBytes(advisorInfos);
        SolutionInfo solutionInfo = SolutionInfo.builder()
                .name(solutionName)
                .description(description)
                .build();
        SolutionInfo saved = solutionInfoRepository.save(solutionInfo);
        solutionInfoRepository.setSolutionInfoBody(body, saved.getId());
        return saved;
    }

    @Override
    public SolutionInfo createChartSolution(String datasetSchemaName,
                                            String playersSchemaName,
                                            OpponentType opponentType,
                                            List<String> playerNames,
                                            String solutionName) {
        List<AdvisorInfo> advisors = createAdvisorsFromDatasets(datasetSchemaName, playersSchemaName, opponentType, playerNames);
        return create(advisors, solutionName, "Chart Solution");
    }

    @Override
    public List<AdvisorInfo> recoverAdvisorInfos(SolutionInfo solutionInfo) {
        byte[] body = solutionInfoRepository.getSolutionInfoBody(solutionInfo.getId());
        return ByteArraySerializer.fromBytes(body);
    }

    @Override
    public SolutionInfo copySolution(String solutionName, String newSolutionName, String description) {
        SolutionInfo solutionInfo = solutionInfoRepository.findByName(solutionName).orElseThrow();
        byte[] body = solutionInfoRepository.getSolutionInfoBody(solutionInfo.getId());
        SolutionInfo saved = solutionInfoRepository.save(SolutionInfo.builder()
                .name(newSolutionName)
                .description(description)
                .build());
        solutionInfoRepository.setSolutionInfoBody(body, saved.getId());
        return saved;
    }

    @Override
    public SolutionInfo update(List<AdvisorInfo> advisorInfos, String solutionName, String description) {
        byte[] body = ByteArraySerializer.toBytes(advisorInfos);
        UUID id = findByName(solutionName).orElseThrow().getId();
        solutionInfoRepository.setSolutionInfoBody(body, id);
        return findByName(solutionName).orElseThrow();
    }

    @Override
    public List<String> findAllNames(String mask) {
        Specification<SolutionInfo> spec = SolutionSpecification.nameContains(mask);
        return solutionInfoRepository.findAll(spec).stream().map(BaseEntity::getName).toList();
    }

    @Override
    public void deleteSolution(String name) {
        solutionInfoRepository.delete(findByName(name).orElseThrow());
    }

    @Override
    public void fixSolutions(List<String> solutionNames, String donorSolutionName) {
        SolutionInfo solutionInfo = findByName(donorSolutionName).orElseThrow();
        List<AdvisorInfo> advisorInfos = recoverAdvisorInfos(solutionInfo);
        Map<String, AdvisorInfo> bySpotName = advisorInfos.stream()
                .collect(Collectors.toMap(s -> s.spotInfo().name(), Function.identity()));
        solutionNames.forEach(s -> fixSolution(s, donorSolutionName, bySpotName));
    }

    @Override
    public SolutionInfo pfDonorSolution(String solutionName, String donorSolutionName) {
        SolutionInfo solutionInfo = findByName(solutionName).orElseThrow();
        List<AdvisorInfo> advisorInfos = recoverAdvisorInfos(solutionInfo);
        SolutionInfo donorSolutionInfo = findByName(donorSolutionName).orElseThrow();
        List<AdvisorInfo> donorAdvisorInfos = recoverAdvisorInfos(donorSolutionInfo);
        EnumMap<Street, List<AdvisorInfo>> src = advisorInfos.stream()
                .collect(Collectors.groupingBy(advisorInfo -> advisorInfo.spotInfo().street(), () -> new EnumMap<>(Street.class), Collectors.toList()));
        EnumMap<Street, List<AdvisorInfo>> donor = donorAdvisorInfos.stream()
                .collect(Collectors.groupingBy(advisorInfo -> advisorInfo.spotInfo().street(), () -> new EnumMap<>(Street.class), Collectors.toList()));
        List<AdvisorInfo> fixedAdvisorInfos = src.entrySet().stream()
                .map(e -> e.getKey() == Street.PF ? donor.get(Street.PF) : e.getValue())
                .flatMap(Collection::stream).toList();
        return update(fixedAdvisorInfos, solutionName, "Preflop taken from %s".formatted(donorSolutionName));
    }

    private void fixSolution(String solutionName, String donorSolutionName, Map<String, AdvisorInfo> bySpotName) {
        SolutionInfo solutionInfo = findByName(solutionName).orElseThrow();
        List<AdvisorInfo> advisorInfos = recoverAdvisorInfos(solutionInfo);
        List<AdvisorInfo> fixedAdvisorInfos = advisorInfos.stream()
                .map(advisorInfo -> {
                    String spotName = advisorInfo.spotInfo().name();
                    AdvisorInfo donorAdvisorInfo = bySpotName.get(spotName);
                    return fixAdvisor(advisorInfo, donorAdvisorInfo);
                }).toList();
        update(fixedAdvisorInfos, solutionName, "fixed with %s".formatted(donorSolutionName));
    }

    private AdvisorInfo fixAdvisor(AdvisorInfo advisorInfo, AdvisorInfo donorAdvisorInfo) {
        String actionAdvisorName = advisorInfo.actionAdvisorName();
        ActionAdvisorType actionAdvisorType = advisorInfo.actionAdvisorType();
        if (advisorInfo.actionAdvisorType() == ActionAdvisorType.ML_ACTION) {
            Model model = modelService.findModelByName(actionAdvisorName);
            if (model.getModelStatus() != ModelStatus.TRAINED) {
                actionAdvisorName = donorAdvisorInfo.actionAdvisorName();
                actionAdvisorType = donorAdvisorInfo.actionAdvisorType();
            }
        }
        String amountAdvisorName = advisorInfo.amountAdvisorName();
        AmountAdvisorType amountAdvisorType = advisorInfo.amountAdvisorType();
        if (advisorInfo.amountAdvisorType() == AmountAdvisorType.ML_AMOUNT) {
            Model model = modelService.findModelByName(amountAdvisorName);
            if (model.getModelStatus() != ModelStatus.TRAINED) {
                amountAdvisorName = donorAdvisorInfo.amountAdvisorName();
                amountAdvisorType = donorAdvisorInfo.amountAdvisorType();
            }
        }
        return new AdvisorInfo(advisorInfo.spotInfo(),
                actionAdvisorName,
                actionAdvisorType,
                advisorInfo.actionTweak(),
                amountAdvisorName,
                amountAdvisorType,
                advisorInfo.amountTweak());
    }

    private List<AdvisorInfo> createAdvisorsFromDatasets(String datasetSchemaName,
                                                         String playersSchemaName,
                                                         OpponentType opponentType,
                                                         List<String> playerNames) {
        return Spots.stream()
                .map(spot -> createAdvisor(spot, datasetSchemaName, playersSchemaName, opponentType, playerNames))
                .toList();
    }


    private AdvisorInfo createAdvisor(Spot spot,
                                      String datasetsSchemaName,
                                      String playersSchemaName,
                                      OpponentType opponentType,
                                      List<String> playerNames) {
        PokerSituation pokerSituation = spot.pokerSituation();
        Street street = pokerSituation.getStreet();
        return switch (street) {
            case PF -> {
                PreflopSpot preflopSpot = PreflopSpot.valueOf(spot.name());
                yield createPreflopAdvisor(preflopSpot, datasetsSchemaName, playersSchemaName, opponentType, playerNames);
            }
            case FL, TN, RV -> createPostflopAdvisor(spot, datasetsSchemaName, playersSchemaName, opponentType, playerNames);
        };
    }

    private AdvisorInfo createPreflopAdvisor(PreflopSpot preflopSpot,
                                             String datasetsSchemaName,
                                             String playersSchemaName,
                                             OpponentType opponentType,
                                             List<String> playerNames) {
        List<PlayerStatFilter> playerStatFilters = opponentType.playerStatFilters();
        String actionChartName = createActionChartName(preflopSpot, opponentType);
        int rowCount = clickhouseQueries.rowCount(datasetsSchemaName, preflopSpot.name());
        Spot fallbackSpot = Spots.fallbackSpot(preflopSpot);
        try {
            List<CardCellActionMap> cardCellActionMaps = clickhouseQueries.cardCellStrategies(datasetsSchemaName,
                    playersSchemaName,
                    rowCount < 100 || preflopSpot.isShortStack() || preflopSpot.isForceFallback() ? fallbackSpot.name() : preflopSpot.name(),
                    playerStatFilters,
                    playerNames,
                    MIN_ACTION_VALUE_THRESHOLD);
            PreflopActionChart preflopActionChart = preflopActionChartService.createOrUpdateChart(actionChartName, preflopSpot, cardCellActionMaps);
        } catch (Exception e) {
            PreflopActionChart preflopActionChart = preflopActionChartService.createOrUpdateChart(actionChartName, preflopSpot, Collections.emptyList());
        }
        String amountChartName = createAmountChartName(preflopSpot, opponentType);
        try {
            List<CardCellWeightedRandomAmount> weightedRandomAmounts = clickhouseQueries.cardCellWeightedRandomAmounts(datasetsSchemaName,
                    playersSchemaName,
                    rowCount < 100 || preflopSpot.isShortStack() || preflopSpot.isForceFallback() ? fallbackSpot.name() : preflopSpot.name(),
                    playerStatFilters,
                    playerNames,
                    preflopSpot.amountFeatureSchema().getLabelFeature(),
                    MIN_AMOUNT_WEIGHT_THRESHOLD);
            PreflopAmountChart preflopAmountChart = preflopAmountChartService.createOrUpdateChart(amountChartName, preflopSpot, weightedRandomAmounts);
        } catch (Exception ignored) {
            PreflopAmountChart preflopAmountChart = preflopAmountChartService.createOrUpdateChart(amountChartName, preflopSpot, Collections.emptyList());
        }
        return new AdvisorInfo(preflopSpot.toSpotInfo(),
                actionChartName,
                ActionAdvisorType.CHART_PREFLOP_ACTION,
                preflopSpot.actionTweak(),
                amountChartName,
                AmountAdvisorType.CHART_PREFLOP_AMOUNT,
                preflopSpot.amountTweak());
    }

    private AdvisorInfo createPostflopAdvisor(Spot spot,
                                              String datasetsSchemaName,
                                              String playersSchemaName,
                                              OpponentType opponentType,
                                              List<String> playerNames) {
        List<PlayerStatFilter> playerStatFilters = opponentType.playerStatFilters();
        String actionChartName = createActionChartName(spot, opponentType);
        try {
            ActionMap actionMap = clickhouseQueries.actionMap(datasetsSchemaName,
                    playersSchemaName,
                    spot.name(),
                    playerStatFilters,
                    playerNames,
                    MIN_ACTION_VALUE_THRESHOLD);
            PostflopActionChart postflopActionChart = postflopActionChartService.createOrUpdateChart(actionChartName, spot, actionMap);
        } catch (Exception e) {
            PostflopActionChart postflopActionChart = postflopActionChartService.createOrUpdateChart(actionChartName, spot, spot.defaultActionMap());
        }
        String amountChartName = createAmountChartName(spot, opponentType);
        try {
            WeightedRandomAmount weightedRandomAmount = clickhouseQueries.weightedRandomAmount(datasetsSchemaName,
                    playersSchemaName,
                    spot.name(),
                    playerStatFilters,
                    playerNames,
                    spot.amountFeatureSchema().getLabelFeature(),
                    MIN_AMOUNT_WEIGHT_THRESHOLD);
            PostflopAmountChart postflopAmountChart = postflopAmountChartService.createChart(amountChartName, spot, weightedRandomAmount);
        } catch (Exception e) {
            List<WeightedAmount> weightedAmounts = List.of(new WeightedAmount(1., spot.defaultAmount()));
            PostflopAmountChart postflopAmountChart = postflopAmountChartService.createChart(amountChartName, spot, new WeightedRandomAmount(weightedAmounts));
        }
        return new AdvisorInfo(spot.toSpotInfo(),
                actionChartName,
                ActionAdvisorType.CHART_POSTFLOP_ACTION,
                spot.actionTweak(),
                amountChartName,
                AmountAdvisorType.CHART_POSTFLOP_AMOUNT,
                spot.amountTweak());

    }

    private String createActionChartName(Spot spot, OpponentType psf) {
        return spot.actionFeatureSchema() + "_" + spot + "_" + psf;
    }

    private String createAmountChartName(Spot spot, OpponentType psf) {
        return spot.amountFeatureSchema() + "_" + spot + "_" + psf;
    }

}
