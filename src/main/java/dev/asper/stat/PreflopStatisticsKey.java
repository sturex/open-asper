package dev.asper.stat;


import dev.asper.poker.engine.Branch;
import dev.asper.poker.engine.spot.PreflopSpot;

import java.io.Serializable;

public record PreflopStatisticsKey(PreflopSpot preflopSpot, Branch inBranch) implements Serializable {
}
