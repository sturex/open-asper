package dev.asper.app.service;


import dev.asper.clickhouse.OpponentType;
import dev.asper.stat.HoldemStatistics;

public interface StatisticsService {
    void initStatisticsMap();

    HoldemStatistics getStatistics(OpponentType opponentType);
}
