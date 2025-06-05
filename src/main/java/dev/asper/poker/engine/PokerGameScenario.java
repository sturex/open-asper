package dev.asper.poker.engine;


import dev.asper.common.scenario.Scenario;
import dev.asper.common.scenario.ScenarioStep;
import dev.asper.poker.enums.PokerGameEventType;

import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public class PokerGameScenario implements Scenario<PokerGameEventType, PokerHand> {

    private final PokerHand pokerHand;

    public static PokerGameScenario from(PokerHand pokerHand) {
        return new PokerGameScenario(pokerHand);
    }

    private PokerGameScenario(PokerHand pokerHand) {
        this.pokerHand = pokerHand;
    }

    @Override
    public void onEachStep(BiConsumer<ScenarioStep<PokerGameEventType>, PokerHand> scenarioStepConsumer) {
        IntStream.range(0, pokerHand.pfMoves().size()).forEach(i -> scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.PF_MOVE, i), pokerHand));
        if (pokerHand.flCards() != null) {
            scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.FL_CARDS, 0), pokerHand);
            IntStream.range(0, pokerHand.flMoves().size()).forEach(i -> scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.FL_MOVE, i), pokerHand));
            if (pokerHand.tnCard() != null) {
                scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.TN_CARD, 0), pokerHand);
                IntStream.range(0, pokerHand.tnMoves().size()).forEach(i -> scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.TN_MOVE, i), pokerHand));
                if (pokerHand.rvCard() != null) {
                    scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.RV_CARD, 0), pokerHand);
                    IntStream.range(0, pokerHand.rvMoves().size()).forEach(i -> scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.RV_MOVE, i), pokerHand));
                }
            }
        }
    }

    @Override
    public void onBegin(BiConsumer<ScenarioStep<PokerGameEventType>, PokerHand> scenarioStepConsumer) {
        scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.BEGIN, 0), pokerHand);
    }

    @Override
    public void onEnd(BiConsumer<ScenarioStep<PokerGameEventType>, PokerHand> scenarioStepConsumer) {
        scenarioStepConsumer.accept(new ScenarioStep<>(PokerGameEventType.END, 0), pokerHand);
    }
}
