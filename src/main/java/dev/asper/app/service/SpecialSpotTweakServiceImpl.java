package dev.asper.app.service;

import dev.asper.advice.ActionMap;
import dev.asper.app.entity.SpecialSpotTweakBody;
import dev.asper.app.graphql.SpecialSpotTweakInput;
import dev.asper.app.repository.NameOnly;
import dev.asper.app.repository.SpecialSpotTweakRepository;
import dev.asper.common.util.ByteArraySerializer;
import dev.asper.poker.engine.SpecialSpotTweakCollection;
import dev.asper.poker.enums.SpecialSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpecialSpotTweakServiceImpl implements SpecialSpotTweakService {

    public static final String IDENTITY = "IDENTITY";
    private final SpecialSpotTweakRepository specialSpotTweakRepository;

    private final Map<String, SpecialSpotTweakCollection> pool = new HashMap<>();

    @Autowired
    public SpecialSpotTweakServiceImpl(SpecialSpotTweakRepository specialSpotTweakRepository) {
        this.specialSpotTweakRepository = specialSpotTweakRepository;
        bootstrap();
    }

    private void bootstrap() {
        try {
            SpecialSpotTweakBody identity = findByName(IDENTITY);
        } catch (Exception e) {
            createOrUpdateSpecialSpotTweakCollection(IDENTITY, Collections.emptyList());
        }
    }

    @Override
    public SpecialSpotTweakBody findByName(String name) {
        return specialSpotTweakRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("SpecialSpotTweakBody not found, name=%s".formatted(name)));
    }

    @Override
    public SpecialSpotTweakCollection createOrUpdateSpecialSpotTweakCollection(String name, List<SpecialSpotTweakInput> specialSpotTweakInputs) {
        EnumMap<SpecialSpot, ActionMap> map = specialSpotTweakInputs.stream()
                .collect(Collectors.toMap(SpecialSpotTweakInput::specialSpot, o -> ActionMap.from(o.actionVectorInput().toActionVector()), (o, o2) -> o, () -> new EnumMap<>(SpecialSpot.class)));
        SpecialSpotTweakBody specialSpotTweakBody;
        try {
            specialSpotTweakBody = findByName(name);
        } catch (Exception e) {
            SpecialSpotTweakBody tweakBody = SpecialSpotTweakBody.builder()
                    .name(name)
                    .description("")
                    .build();
            specialSpotTweakBody = specialSpotTweakRepository.save(tweakBody);
        }
        byte[] bytes = ByteArraySerializer.toBytes(map);
        specialSpotTweakRepository.setBody(bytes, specialSpotTweakBody.getId());
        SpecialSpotTweakCollection specialSpotTweakCollection = new SpecialSpotTweakCollection(map);
        pool.put(name, new SpecialSpotTweakCollection(map));
        return specialSpotTweakCollection;
    }

    @Override
    public SpecialSpotTweakCollection getByName(String name) {
        return Optional.ofNullable(pool.get(name))
                .orElseGet(() -> {
                    byte[] body = specialSpotTweakRepository.getBody(name);
                    EnumMap<SpecialSpot, ActionMap> map = ByteArraySerializer.fromBytes(body);
                    return pool.computeIfAbsent(name, n -> new SpecialSpotTweakCollection(map));
                });
    }

    @Override
    public List<String> readAllNames() {
        return specialSpotTweakRepository.findAllBy().stream().map(NameOnly::getName).toList();
    }
}
