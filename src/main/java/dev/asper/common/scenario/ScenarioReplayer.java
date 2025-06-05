package dev.asper.common.scenario;


import dev.asper.common.fsm.StateMachine;
import dev.asper.common.fsm.Stateful;

import java.util.function.BiConsumer;

public class ScenarioReplayer<Event extends Enum<Event>, State extends Enum<State>, S extends Stateful<State>, C> {
    protected final StateMachine<Event, State, S, C> stateMachine;

    public ScenarioReplayer(StateMachine<Event, State, S, C> stateMachine) {
        this.stateMachine = stateMachine;
    }

    public void play(S s,
                     Scenario<Event, C> scenario,
                     BiConsumer<State, S> beginConsumer,
                     BiConsumer<State, S> stepConsumer,
                     BiConsumer<State, S> endConsumer) {
        scenario.onBegin((scenarioStep, c) -> {
            State prevState = s.getState();
            beginConsumer.accept(prevState, s);
            stateMachine.apply(s, scenarioStep.event(), scenarioStep.eventIdx(), c);
        });
        scenario.onEachStep((scenarioStep, c) -> {
            State prevState = s.getState();
            stepConsumer.accept(prevState, s);
            stateMachine.apply(s, scenarioStep.event(), scenarioStep.eventIdx(), c);
        });
        scenario.onEnd((scenarioStep, c) -> {
            State prevState = s.getState();
            endConsumer.accept(prevState, s);
            stateMachine.apply(s, scenarioStep.event(), scenarioStep.eventIdx(), c);
        });
    }

    public void play(S s,
                     Scenario<Event, C> scenario,
                     BiConsumer<State, S> onBeforeConsumer,
                     BiConsumer<State, S> onAfterConsumer) {
        scenario.onEachStep((scenarioStep, c) -> {
            State prevState = s.getState();
            onBeforeConsumer.accept(prevState, s);
            stateMachine.apply(s, scenarioStep.event(), scenarioStep.eventIdx(), c);
            State nextState = s.getState();
            onAfterConsumer.accept(nextState, s);
        });
    }

    public void replay(S s, Scenario<Event, C> scenario) {
        scenario.onBegin((scenarioStep, c) -> {
            stateMachine.apply(s, scenarioStep.event(), scenarioStep.eventIdx(), c);
        });
        scenario.onEachStep((scenarioStep, c) -> {
            stateMachine.apply(s, scenarioStep.event(), scenarioStep.eventIdx(), c);
        });
    }

}
