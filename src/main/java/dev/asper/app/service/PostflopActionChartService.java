package dev.asper.app.service;

import dev.asper.advice.ActionMap;
import dev.asper.poker.ai.ChartType;
import dev.asper.poker.chart.PostflopActionChart;
import dev.asper.poker.engine.spot.Spot;

import java.util.List;
import java.util.UUID;

public interface PostflopActionChartService {
    PostflopActionChart createOrUpdateChart(String name,
                                            Spot spot,
                                            ActionMap actionMap);

    PostflopActionChart findByName(String name);

    PostflopActionChart findById(UUID id);

    List<String> readAllNames(String spot, String mask, ChartType chartType);
}
