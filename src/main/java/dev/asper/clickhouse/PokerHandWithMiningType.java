package dev.asper.clickhouse;

import dev.asper.poker.engine.PokerHand;
import dev.asper.poker.enums.MiningType;

public record PokerHandWithMiningType(PokerHand pokerHand, MiningType miningType) {
}
