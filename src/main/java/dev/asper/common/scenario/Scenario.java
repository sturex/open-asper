package dev.asper.common.scenario;

import java.util.function.BiConsumer;

public interface Scenario<Event extends Enum<Event>, C> {

    void onEachStep(BiConsumer<ScenarioStep<Event>, C> scenarioStepConsumer);
    void onBegin(BiConsumer<ScenarioStep<Event>, C> biConsumer);
    void onEnd(BiConsumer<ScenarioStep<Event>, C> scenarioStepConsumer);
}
