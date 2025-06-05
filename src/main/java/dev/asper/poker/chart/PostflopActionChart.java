package dev.asper.poker.chart;

import dev.asper.advice.ActionMap;
import dev.asper.advice.ActionTweak;
import dev.asper.advice.ActionVector;
import dev.asper.poker.ai.ActionAdvisor;
import dev.asper.poker.ai.ActionMapGame;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.holding.ComboExType;

import java.util.List;

public class PostflopActionChart implements ActionAdvisor {

    private ActionTweak actionTweak = ActionTweak.randomByIdentity;

    public void setActionTweak(ActionTweak actionTweak) {
        this.actionTweak = actionTweak;
    }

    public ActionMap getActionMap() {
        return actionMap;
    }

    private final ActionMap actionMap;

    public PostflopActionChart(ActionMap actionMap) {
        this.actionMap = actionMap;
    }

    @Override
    public ActionMap adviceActionMap(Spot spot, PokerGame pokerGame) {
        return actionMap;
    }

    @Override
    public ActionTweak getActionTweak() {
        return actionTweak;
    }

    public ActionVector actionVector() {
        return actionMap.toActionVector();
    }
}
