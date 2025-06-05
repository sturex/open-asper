package dev.asper.poker.ai;

import dev.asper.advice.ActionMap;
import dev.asper.advice.ActionTweak;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.engine.spot.Spots;
import dev.asper.poker.enums.Action;

import java.util.List;

public interface ActionAdvisor {
    default List<ActionMapGame> adviceActionMap(Spot spot, List<PokerGame> pokerGames) {
        return pokerGames.parallelStream()
                .map(pokerGame -> new ActionMapGame(pokerGame, adviceActionMap(spot, pokerGame)))
                .toList();
    }

    default ActionMap adviceActionMap(Spot spot, PokerGame pokerGame) {
        return adviceActionMap(spot, List.of(pokerGame)).get(0).actionMap();
    }

    default ActionMap adviceActionMap(PokerGame pokerGame) {
        Spot spot = Spots.from(pokerGame);
        return adviceActionMap(spot, pokerGame);
    }

    default Action adviceAction(PokerGame pokerGame) {
        return tweakActionMap(adviceActionMap(pokerGame));
    }

    default Action tweakActionMap(ActionMap actionMap) {
        ActionTweak actionTweak = getActionTweak();
        return actionTweak.apply(actionMap);
    }

    default ActionTweak getActionTweak() {
        return ActionTweak.randomByIdentity;
    }
}
