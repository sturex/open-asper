package dev.asper.app.service;

import dev.asper.app.entity.base.BaseEntity;
import dev.asper.app.repository.spec.PreflopChartSpecification;
import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.common.util.ByteArraySerializer;
import dev.asper.app.entity.PreflopChartBody;
import dev.asper.app.repository.PreflopChartRepository;
import dev.asper.poker.ai.ChartType;
import dev.asper.poker.chart.CardCellActionMap;
import dev.asper.poker.chart.PreflopActionChart;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.engine.spot.Spots;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PreflopActionChartServiceImpl implements PreflopActionChartService {

    private final PreflopChartRepository preflopChartRepository;
    private final LogService logService;

    @Autowired
    public PreflopActionChartServiceImpl(PreflopChartRepository preflopChartRepository,
                                         LogService logService) {
        this.preflopChartRepository = preflopChartRepository;
        this.logService = logService;
    }

    @Override
    @Transactional
    public PreflopActionChart createOrUpdateChart(String name,
                                                  Spot spot,
                                                  List<CardCellActionMap> cardCellActionMaps) {
        UUID id = preflopChartRepository.findByName(name)
                .map(BaseEntity::getId)
                .orElseGet(() -> {
                    PreflopChartBody preflopChartBody = preflopChartRepository.save(PreflopChartBody.builder()
                            .name(name)
                            .chartType(ChartType.ACTION)
                            .description("Custom chart")
                            .spot(spot.name())
                            .build());
                    logService.log(LogRecordCode.ENTITY_CREATED, Map.of("name", name));
                    return preflopChartBody
                            .getId();
                });
        preflopChartRepository.setBody(ByteArraySerializer.toBytes(cardCellActionMaps), id);
        return new PreflopActionChart(spot, cardCellActionMaps);
    }

    @Override
    public PreflopActionChart findByName(String name) {
        return preflopChartRepository.findByName(name)
                .map(pcb -> new PreflopActionChart(Spots.fromStr(pcb.getSpot()),
                        ByteArraySerializer.fromBytes(preflopChartRepository.getBody(pcb.getId()))))
                .orElseThrow(() -> new RuntimeException("PreflopActionChart with name " + name + " is not found."));
    }

    @Override
    public List<String> readAllNames(String spot, String mask, ChartType chartType) {
        Specification<PreflopChartBody> spec = PreflopChartSpecification.nameContains(mask)
                .and(PreflopChartSpecification.spotContains(spot))
                .and(PreflopChartSpecification.chartTypeFilter(chartType));
        return preflopChartRepository.findAll(spec).stream().map(BaseEntity::getName).toList();
    }

    @Override
    public PreflopActionChart findById(UUID id) {
        return preflopChartRepository.findById(id)
                .map(pcb -> new PreflopActionChart(Spots.fromStr(pcb.getSpot()),
                        ByteArraySerializer.fromBytes(preflopChartRepository.getBody(pcb.getId()))))
                .orElseThrow(() -> new RuntimeException("PreflopActionChart with id " + id + " is not found."));
    }
}
