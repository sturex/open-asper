package dev.asper.poker.engine;


import dev.asper.common.feature.Descriptor;
import dev.asper.common.fsm.StateMachine;
import dev.asper.common.scenario.Scenario;
import dev.asper.common.scenario.ScenarioReplayer;
import dev.asper.poker.enums.PokerGameEventType;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PokerHandReplayer extends ScenarioReplayer<PokerGameEventType, PokerSituation, PokerGame, PokerHand> {
    private static final Logger logger = Logger.getLogger(PokerHandReplayer.class.getSimpleName());

    public PokerHandReplayer(StateMachine<PokerGameEventType, PokerSituation, PokerGame, PokerHand> stateMachine) {
        super(stateMachine);
    }

    public void replayBestEffort(PokerGame pokerGame, Scenario<PokerGameEventType, PokerHand> scenario) {
        try {
            super.replay(pokerGame, scenario);
        } catch (Exception e) {
            logger.severe("Poker game replay failed. Cause message: " + e.getMessage());
            pokerGame.setState(PokerSituation.GAME_BROKEN);
        }
    }

    public void replayBestEffort(PokerGame pokerGame, PokerHand pokerHand) {
        replayBestEffort(pokerGame, PokerGameScenario.from(pokerHand));
    }
}
