package dev.asper.common.scenario;

public record ScenarioStep<Event extends Enum<Event>>(Event event, int eventIdx) {
}
