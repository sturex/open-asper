package dev.asper.app.component;

import dev.asper.app.config.DatasetSchemasConfig;
import dev.asper.clickhouse.OpponentType;
import dev.asper.clickhouse.PlayerStat;
import dev.asper.poker.enums.BoardSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class PlayersRepository {
    private final ClickhouseQueries clickhouseQueries;
    private final String playersSchemaName;
    private final String playersTableName;

    @Autowired
    public PlayersRepository(ClickhouseQueries clickhouseQueries,
                             DatasetSchemasConfig datasetSchemasConfig) {
        this.clickhouseQueries = clickhouseQueries;
        playersSchemaName = datasetSchemasConfig.getPlayersSchemaName();
        playersTableName = datasetSchemasConfig.getPlayersTableName();
    }

    private Map<String, EnumMap<BoardSize, PlayerStat>> repository = new HashMap<>();

    public OpponentType resolveNarrowOpponentType(String playerName, BoardSize boardSize) {
        return Optional.ofNullable(repository.get(playerName))
                .flatMap(m -> Optional.ofNullable(m.get(boardSize))
                        .map(ps -> OpponentType.OVERALL))
                .orElse(OpponentType.OVERALL);
    }

    public PlayerStat getPlayerStat(String playerName, BoardSize boardSize) {
        return Optional.ofNullable(repository.get(playerName))
                .flatMap(m -> Optional.ofNullable(m.get(boardSize)))
                .orElseThrow(() -> new RuntimeException("PlayerStat is not found for %s".formatted(playerName)));
    }

    public synchronized void init() {
        if (repository.isEmpty()) {
            repository = clickhouseQueries.readPlayerStats(playersSchemaName, playersTableName);
        }
    }
}
