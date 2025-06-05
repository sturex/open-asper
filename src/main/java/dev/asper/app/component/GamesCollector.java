package dev.asper.app.component;

import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.enums.MiningType;

import java.util.List;

public interface GamesCollector {
    int collectPokerGames(String schemaName, String tableName, List<PokerGame> pokerGames, MiningType miningType);
    int collectHandHistories(String schemaName, String tableName, List<PokerGame> pokerGames, String playerName);
}
