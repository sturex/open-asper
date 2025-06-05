package dev.asper.app.service;

import dev.asper.app.component.ClickhouseQueries;
import dev.asper.app.config.DatasetSchemasConfig;
import dev.asper.app.entity.StatisticsBody;
import dev.asper.app.repository.StatisticsRepository;
import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.clickhouse.OpponentType;
import dev.asper.common.util.ByteArraySerializer;
import dev.asper.poker.engine.Branch;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.engine.spot.PreflopSpot;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.enums.*;
import dev.asper.stat.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    private final LogService logService;
    private final ClickhouseQueries clickhouseQueries;
    private final StatisticsRepository statisticsRepository;
    private final Map<OpponentType, HoldemStatistics> statisticsMap = new HashMap<>();
    private final String datasetSchemaName;
    private final String holdemStatSchemaName;

    @Autowired
    public StatisticsServiceImpl(DatasetSchemasConfig datasetSchemasConfig,
                                 LogService logService,
                                 ClickhouseQueries clickhouseQueries,
                                 StatisticsRepository statisticsRepository) {
        this.logService = logService;
        this.clickhouseQueries = clickhouseQueries;
        this.statisticsRepository = statisticsRepository;
        this.datasetSchemaName = datasetSchemasConfig.getDatasetSchemaName();
        holdemStatSchemaName = datasetSchemasConfig.getHoldemStatSchemaName();
    }

    @Override
    public synchronized void initStatisticsMap() {
        statisticsMap.putAll(Arrays.stream(OpponentType.values())
                .collect(Collectors.toMap(opponentType -> opponentType, opponentType -> findStatistics(opponentType)
                        .orElseGet(() -> {
                            HoldemStatistics holdemStatistics = computeWinRateStatistics(opponentType);
                            saveStatistics(opponentType, holdemStatistics);
                            return holdemStatistics;
                        }))));
    }

    private void saveStatistics(OpponentType opponentType, HoldemStatistics holdemStatistics) {
        byte[] bytes = ByteArraySerializer.toBytes(holdemStatistics);
        StatisticsBody statisticsBody = statisticsRepository.save(StatisticsBody.builder()
                .name(opponentType.name())
                .description("")
                .build());
        statisticsRepository.setBody(bytes, statisticsBody.getId());
        logService.log(LogRecordCode.STATISTICS_SAVED, Map.of("opponentType", opponentType.name()));
    }

    private Optional<HoldemStatistics> findStatistics(OpponentType opponentType) {
        return statisticsRepository.findByName(opponentType.name())
                .map(statisticsBody -> {
                    byte[] body = statisticsRepository.getBody(statisticsBody.getId());
                    return ByteArraySerializer.fromBytes(body);
                });
    }

    private HoldemStatistics computeWinRateStatistics(OpponentType opponentType) {
        Instant started = logService.log(LogRecordCode.STATISTICS_CALCULATION_STARTED, Map.of("schemaName", this.datasetSchemaName));
        Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> preflopStatistics = new HashMap<>();
        Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> postflopStatistics = new HashMap<>();
        Map<PostflopStatisticsKey, EnumMap<Branch, Double>> postflopRoughStatistics = new HashMap<>();
        Arrays.stream(PreflopSpot.values())
                .forEach(preflopSpot -> {
                    try {
                        Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> preflopHoldemFcarRanges = clickhouseQueries.computePreflopBranchStatistics(datasetSchemaName, preflopSpot, opponentType);
                        preflopStatistics.putAll(preflopHoldemFcarRanges);
                    } catch (Exception e) {
                        logService.log(LogRecordCode.STATISTICS_CALCULATION_FAILED, Map.of("exception", e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
                    }
                });
        PokerSituation.postflopSituations.forEach(pokerSituation -> {
            try {
                Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> pairs = clickhouseQueries.computePostflopBranchStatistics(holdemStatSchemaName, pokerSituation, opponentType);
                Map<PostflopStatisticsKey, EnumMap<Branch, Double>> pairsRough = clickhouseQueries.computePostflopRoughBranchStatistics(holdemStatSchemaName, pokerSituation, opponentType);
                postflopStatistics.putAll(pairs);
                postflopRoughStatistics.putAll(pairsRough);
            } catch (Exception e) {
                logService.log(LogRecordCode.STATISTICS_CALCULATION_FAILED, Map.of("exception", e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
            }
        });
        HoldemStatistics holdemStatistics = new HoldemStatistics(preflopStatistics, postflopStatistics, postflopRoughStatistics);
        logService.log(started, LogService.Units.SEC, LogRecordCode.STATISTICS_CALCULATION_FINISHED, Map.of("datasetSchemaName", datasetSchemaName, "holdemStatSchemaName", holdemStatSchemaName));
        return holdemStatistics;
    }

    @Override
    public HoldemStatistics getStatistics(OpponentType opponentType) {
        return Optional.ofNullable(statisticsMap.get(opponentType))
                .orElseThrow(() -> new RuntimeException("Statistics for %s is not present in pool".formatted(opponentType)));
    }

}
