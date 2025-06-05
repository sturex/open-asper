package dev.asper.common.fsm;

public interface StateMachine<Event extends Enum<Event>, State extends Enum<State>, S extends Stateful<State>, C> {
    void apply(S stateful, Event event, int idx, C context);

    StateMachine<Event, State, S, C> addTransition(State src,
                                                   Event event,
                                                   Guard<S, C> guard,
                                                   TransitionFunction<State, S, C> transitionFunction,
                                                   Action<S, C> errorAction);

    default StateMachine<Event, State, S, C> addTransition(State src,
                                                           Event event,
                                                           Guard<S, C> guard,
                                                           TransitionFunction<State, S, C> transitionFunction) {
        return addTransition(src, event, guard, transitionFunction, (stateful, context) -> {
            throw new RuntimeException("Error while making the transition on entity=" + stateful + " from state=" + src + " by event=" + event + " with context=" + context);
        });
    }

    default StateMachine<Event, State, S, C> addTransition(State src,
                                                           Event event,
                                                           TransitionFunction<State, S, C> transitionFunction) {
        return addTransition(src, event, (s, idx, c) -> true, transitionFunction);
    }

}
