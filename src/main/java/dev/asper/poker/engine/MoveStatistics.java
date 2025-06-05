package dev.asper.poker.engine;

import dev.asper.advice.Decision;
import dev.asper.poker.enums.Action;
import dev.asper.poker.enums.PreflopPosition;
import dev.asper.poker.holding.BoardStatus;


public record MoveStatistics(
        int idx,
        int idxPosition,
        int idxDecisionType,
        PreflopPosition preflopPosition,
        Decision decision,
        Branch outBranch,
        Branch inBranch,
        BoardStatus boardStatus,
        PokerSituation prevPokerSituation,
        int callAmount,
        int diffAmount,
        int stackStub,
        int potSize
) {

    public Action action() {
        return decision.action();
    }

    public int getAmount() {
        return decision.amount();
    }

    @Override
    public String toString() {
        return preflopPosition + ":" + decision;
    }

}
