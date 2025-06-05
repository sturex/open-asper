package dev.asper.app.service;

import dev.asper.app.entity.SpecialSpotTweakBody;
import dev.asper.app.graphql.SpecialSpotTweakInput;
import dev.asper.poker.engine.SpecialSpotTweakCollection;

import java.util.List;

public interface SpecialSpotTweakService {
    SpecialSpotTweakBody findByName(String name);

    SpecialSpotTweakCollection createOrUpdateSpecialSpotTweakCollection(String name, List<SpecialSpotTweakInput> specialSpotTweakInputs);

    SpecialSpotTweakCollection getByName(String name);

    List<String> readAllNames();
}
