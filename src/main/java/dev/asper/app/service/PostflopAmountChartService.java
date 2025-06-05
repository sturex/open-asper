package dev.asper.app.service;

import dev.asper.advice.WeightedRandomAmount;
import dev.asper.poker.ai.ChartType;
import dev.asper.poker.chart.PostflopAmountChart;
import dev.asper.poker.engine.spot.Spot;

import java.util.List;
import java.util.UUID;

public interface PostflopAmountChartService {
    PostflopAmountChart createChart(String name,
                                    Spot spot,
                                    WeightedRandomAmount weightedRandomAmount);

    PostflopAmountChart findByName(String name);

    PostflopAmountChart findById(UUID id);

    List<String> readAllNames(String spot, String mask, ChartType chartType);
}
