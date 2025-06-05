package dev.asper.stat;


import dev.asper.poker.engine.Branch;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.engine.spot.Spot;

import java.io.Serializable;

public record PostflopStatisticsKey(PokerSituation pokerSituation, Branch inBranch) implements Serializable {
}
