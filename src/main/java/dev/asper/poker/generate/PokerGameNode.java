package dev.asper.poker.generate;


import dev.asper.poker.engine.PokerGame;

import java.util.Map;

public record PokerGameNode(PokerGame pokerGame, Map<PokerGameEdge, PokerGameNode> treeMap) {
    public boolean isEmpty() {
        return treeMap.isEmpty();
    }
}
