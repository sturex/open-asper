package dev.asper.app.service;

import dev.asper.poker.ai.ChartType;
import dev.asper.poker.chart.CardCellActionMap;
import dev.asper.poker.chart.PreflopActionChart;
import dev.asper.poker.engine.spot.Spot;

import java.util.List;
import java.util.UUID;

public interface PreflopActionChartService {
    PreflopActionChart createOrUpdateChart(String name,
                                           Spot spot,
                                           List<CardCellActionMap> cardCellActionMaps);

    PreflopActionChart findByName(String name);

    List<String> readAllNames(String spot, String mask, ChartType chartType);

    PreflopActionChart findById(UUID id);
}
