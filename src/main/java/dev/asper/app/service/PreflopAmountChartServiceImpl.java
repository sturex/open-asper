package dev.asper.app.service;

import dev.asper.advice.CardCellWeightedRandomAmount;
import dev.asper.app.entity.base.BaseEntity;
import dev.asper.app.repository.spec.PreflopChartSpecification;
import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.common.util.ByteArraySerializer;
import dev.asper.app.entity.PreflopChartBody;
import dev.asper.app.repository.PreflopChartRepository;
import dev.asper.poker.ai.ChartType;
import dev.asper.poker.chart.PreflopAmountChart;
import dev.asper.poker.engine.spot.PreflopSpot;
import dev.asper.poker.engine.spot.Spots;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PreflopAmountChartServiceImpl implements PreflopAmountChartService {

    private final PreflopChartRepository preflopChartRepository;
    private final LogService logService;


    @Autowired
    public PreflopAmountChartServiceImpl(PreflopChartRepository preflopChartRepository, LogService logService) {
        this.preflopChartRepository = preflopChartRepository;
        this.logService = logService;
    }

    @Override
    public PreflopAmountChart createOrUpdateChart(String name,
                                                  PreflopSpot preflopSpot,
                                                  List<CardCellWeightedRandomAmount> cardCellWeightedRandomAmounts) {
        UUID id = preflopChartRepository.findByName(name)
                .map(BaseEntity::getId)
                .orElseGet(() -> {
                    PreflopChartBody preflopChartBody = preflopChartRepository.save(PreflopChartBody.builder()
                            .name(name)
                            .chartType(ChartType.AMOUNT)
                            .spot(preflopSpot.name())
                            .description("Custom chart")
                            .build());
                    logService.log(LogRecordCode.ENTITY_CREATED, Map.of("name", name));
                    return preflopChartBody
                            .getId();
                });
        preflopChartRepository.setBody(ByteArraySerializer.toBytes(cardCellWeightedRandomAmounts), id);
        return new PreflopAmountChart(preflopSpot, cardCellWeightedRandomAmounts);
    }

    @Override
    public PreflopAmountChart findByName(String name) {
        return preflopChartRepository.findByName(name)
                .map(pcb -> new PreflopAmountChart(Spots.fromStr(pcb.getSpot()),
                        ByteArraySerializer.fromBytes(preflopChartRepository.getBody(pcb.getId()))))
                .orElseThrow(() -> new RuntimeException("PreflopAmountChart with name " + name + " is not found."));
    }

    @Override
    public PreflopAmountChart findById(UUID id) {
        return preflopChartRepository.findById(id)
                .map(pcb -> new PreflopAmountChart(Spots.fromStr(pcb.getSpot()),
                        ByteArraySerializer.fromBytes(preflopChartRepository.getBody(pcb.getId()))))
                .orElseThrow(() -> new RuntimeException("PreflopAmountChart with id " + id + " is not found."));
    }

    @Override
    public List<String> readAllNames(String spot, String mask, ChartType chartType) {
        Specification<PreflopChartBody> spec = PreflopChartSpecification.nameContains(mask)
                .and(PreflopChartSpecification.spotContains(spot))
                .and(PreflopChartSpecification.chartTypeFilter(chartType));
        return preflopChartRepository.findAll(spec).stream().map(BaseEntity::getName).toList();
    }
}
