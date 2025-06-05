package dev.asper.poker.chart;

import dev.asper.advice.ActionMap;
import dev.asper.advice.ActionTweak;
import dev.asper.poker.ai.ActionAdvisor;
import dev.asper.poker.card.PreflopRange;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.card.CardCell;
import dev.asper.poker.enums.Action;

import java.io.Serial;
import java.util.*;

public class PreflopActionChart extends EnumMap<CardCell, ActionMap> implements ActionAdvisor {
    @Serial
    private static final long serialVersionUID = 4468064711714984180L;
    private final List<CardCellActionMap> cardCellStrategies;
    private ActionTweak actionTweak = ActionTweak.randomByIdentity;

    public PreflopActionChart(Spot spot, List<CardCellActionMap> cardCellStrategies) {
        super(CardCell.class);
        this.cardCellStrategies = new ArrayList<>(cardCellStrategies);
        this.cardCellStrategies.forEach(cardCellStrategy -> this.put(cardCellStrategy.cardCell(), cardCellStrategy.actionMap()));
        EnumSet<CardCell> cardCells = EnumSet.allOf(CardCell.class);
        cardCells.removeAll(keySet());
        ActionMap defaultActionMap = spot.defaultActionMap();
        cardCells.forEach(cardCell -> {
            this.cardCellStrategies.add(new CardCellActionMap(cardCell, defaultActionMap));
            this.put(cardCell, defaultActionMap);
        });
    }

    public List<CardCellActionMap> getCardCellActionMaps() {
        return cardCellStrategies;
    }

    public void setActionTweak(ActionTweak actionTweak) {
        this.actionTweak = actionTweak;
    }

    @Override
    public ActionMap adviceActionMap(Spot spot, PokerGame pokerGame) {
        CardCell cardCell = pokerGame.getNextPlayerOrThrow().getCardCell();
        return adviceActionMap(cardCell);
    }

    public ActionMap adviceActionMap(CardCell cardCell) {
        return Optional.ofNullable(get(cardCell))
                .orElseThrow(() -> new RuntimeException("%s doesn't contain %s key".formatted(PreflopActionChart.class.getPackageName(), cardCell.name())));
    }

    @Override
    public ActionTweak getActionTweak() {
        return actionTweak;
    }
}
