package dev.asper.app.service;

import dev.asper.clickhouse.OpponentType;
import dev.asper.poker.enums.BoardSize;

public interface OpponentService {
    OpponentType resolveOpponentType(String playerName, BoardSize boardSize, double vpip);
}
