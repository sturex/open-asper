package dev.asper.stat;


import dev.asper.poker.engine.Branch;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.holding.BoardFlush;
import dev.asper.poker.holding.BoardPair;
import dev.asper.poker.holding.BoardStraight;

import java.io.Serializable;

public record PostflopBoardStatisticsKey(
        PokerSituation pokerSituation,
        Branch inBranch,
        BoardFlush boardFlush,
        BoardStraight boardStraight,
        BoardPair boardPair
) implements Serializable {
}
