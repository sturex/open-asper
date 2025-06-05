package dev.asper.app.service;

import dev.asper.advice.WeightedRandomAmount;
import dev.asper.app.entity.base.BaseEntity;
import dev.asper.app.repository.spec.PostflopChartSpecification;
import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.common.util.ByteArraySerializer;
import dev.asper.app.entity.PostflopChartBody;
import dev.asper.app.repository.PostflopChartRepository;
import dev.asper.poker.ai.ChartType;
import dev.asper.poker.chart.PostflopAmountChart;
import dev.asper.poker.engine.spot.Spot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PostflopAmountChartServiceImpl implements PostflopAmountChartService {

    private final PostflopChartRepository postflopChartRepository;
    private final LogService logService;

    @Autowired
    public PostflopAmountChartServiceImpl(PostflopChartRepository postflopChartRepository,
                                          LogService logService) {
        this.postflopChartRepository = postflopChartRepository;
        this.logService = logService;
    }

    @Override
    public PostflopAmountChart createChart(String name,
                                           Spot spot,
                                           WeightedRandomAmount weightedRandomAmount) {
        UUID id = postflopChartRepository.findByName(name)
                .map(BaseEntity::getId)
                .orElseGet(() -> {
                    PostflopChartBody postflopChartBody = postflopChartRepository.save(PostflopChartBody.builder()
                            .name(name)
                            .chartType(ChartType.AMOUNT)
                            .description("Custom chart")
                            .spot(spot.name())
                            .build());
                    logService.log(LogRecordCode.ENTITY_CREATED, Map.of("name", name));
                    return postflopChartBody
                            .getId();
                });
        postflopChartRepository.setBody(ByteArraySerializer.toBytes(weightedRandomAmount), id);
        return new PostflopAmountChart(weightedRandomAmount);
    }

    @Override
    public PostflopAmountChart findByName(String name) {
        return postflopChartRepository.findByName(name)
                .map(pcb -> new PostflopAmountChart(ByteArraySerializer.fromBytes(postflopChartRepository.getBody(pcb.getId()))))
                .orElseThrow(() -> new RuntimeException("PostflopAmountChart with name " + name + " is not found."));
    }

    @Override
    public PostflopAmountChart findById(UUID id) {
        return postflopChartRepository.findById(id)
                .map(pcb -> new PostflopAmountChart(ByteArraySerializer.fromBytes(postflopChartRepository.getBody(pcb.getId()))))
                .orElseThrow(() -> new RuntimeException("PostflopAmountChart with id " + id + " is not found."));
    }

    @Override
    public List<String> readAllNames(String spot, String mask, ChartType chartType) {
        Specification<PostflopChartBody> spec = PostflopChartSpecification.nameContains(mask)
                .and(PostflopChartSpecification.spotLike(spot))
                .and(PostflopChartSpecification.chartTypeFilter(chartType));
        return postflopChartRepository.findAll(spec).stream().map(BaseEntity::getName).toList();
    }
}
