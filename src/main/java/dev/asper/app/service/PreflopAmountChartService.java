package dev.asper.app.service;

import dev.asper.advice.CardCellWeightedRandomAmount;
import dev.asper.poker.ai.ChartType;
import dev.asper.poker.chart.PreflopAmountChart;
import dev.asper.poker.engine.spot.PreflopSpot;

import java.util.List;
import java.util.UUID;

public interface PreflopAmountChartService {
    PreflopAmountChart createOrUpdateChart(String name,
                                           PreflopSpot preflopSpot,
                                           List<CardCellWeightedRandomAmount> cardCellWeightedRandomAmounts);

    PreflopAmountChart findByName(String name);
    PreflopAmountChart findById(UUID id);

    List<String> readAllNames(String spot, String mask, ChartType chartType);
}
