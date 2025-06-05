package dev.asper.app.component;

import com.clickhouse.jdbc.ClickHouseConnection;
import com.clickhouse.jdbc.ClickHouseDataSource;
import dev.asper.app.config.ClickhouseConfig;
import dev.asper.poker.card.Card;
import dev.asper.poker.card.Cards;
import dev.asper.poker.engine.MoveStatistics;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerPlayer;
import dev.asper.poker.enums.MiningType;
import dev.asper.poker.enums.PreflopPosition;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class ClickhouseGamesCollector implements GamesCollector {
    private final JdbcTemplate clickhouseJdbcTemplate;
    private final ClickHouseConnection connection;
    private final Logger log = Logger.getLogger(this.getClass().getName());

    public ClickhouseGamesCollector(ClickHouseDataSource clickHouseDataSource) throws SQLException {
        connection = clickHouseDataSource.getConnection();
        clickhouseJdbcTemplate = new JdbcTemplate(clickHouseDataSource);
    }

    private void createSchemaIfNotExists(String schemaName) throws SQLException {
        String sql = "create database if not exists " + schemaName;
        connection.prepareStatement(sql).execute();
    }

    @Override
    public synchronized int collectPokerGames(String schemaName, String tableName, List<PokerGame> pokerGames, MiningType miningType) {
        try {
            createSchemaIfNotExists(schemaName);
            createPokerGamesTable(schemaName, tableName);
            try {
                String sql = String.format("""
                        insert into %s.%s (*) values
                        (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                        """, schemaName, tableName);
                clickhouseJdbcTemplate.batchUpdate(sql, pokerGames, ClickhouseConfig.CLICKHOUSE_BATCH_SIZE, (ps, game) -> {
                    Collection<PokerPlayer> pokerPlayers = game.getPlayersByPosition().values();
                    int idx = 0;
                    ps.setString(++idx, game.getComment());
                    ps.setString(++idx, miningType.name());
                    ps.setString(++idx, game.getHandId());
                    ps.setString(++idx, game.getHandDate());
                    ps.setLong(++idx, game.getSbAmount());
                    ps.setLong(++idx, game.getBbAmount());
                    ps.setString(++idx, game.getCompetitionType().name());
                    ps.setInt(++idx, game.getBoardSize().size());
                    ps.setObject(++idx, pokerPlayers.stream().collect(Collectors.toMap(PokerPlayer::getPreflopPosition, PokerPlayer::getPlayerName, (s, s2) -> s, () -> new EnumMap<>(PreflopPosition.class))));
                    ps.setObject(++idx, pokerPlayers.stream().collect(Collectors.toMap(PokerPlayer::getPreflopPosition, (pokerPlayer -> pokerPlayer.isPocketCardsOpen() ? pokerPlayer.getCardPair().getStdName() : ""), (s, s2) -> s, () -> new EnumMap<>(PreflopPosition.class))));
                    ps.setObject(++idx, pokerPlayers.stream().collect(Collectors.toMap(PokerPlayer::getPreflopPosition, PokerPlayer::getInitialStack, (s, s2) -> s, () -> new EnumMap<>(PreflopPosition.class))));
                    ps.setObject(++idx, pokerPlayers.stream().collect(Collectors.toMap(PokerPlayer::getPreflopPosition, PokerPlayer::getProfit, (s, s2) -> s, () -> new EnumMap<>(PreflopPosition.class))));
                    Cards flCards = game.getFlCards();
                    Card tnCard = game.getTnCard();
                    Card rvCard = game.getRvCard();
                    ps.setString(++idx, flCards == null ? "" : flCards.toString());
                    ps.setString(++idx, tnCard == null ? "" : tnCard.toString());
                    ps.setString(++idx, rvCard == null ? "" : rvCard.toString());
                    List<MoveStatistics> pfMoveStats = game.getPfMoveStats();
                    List<MoveStatistics> flMoveStats = game.getFlMoveStats();
                    List<MoveStatistics> tnMoveStats = game.getTnMoveStats();
                    List<MoveStatistics> rvMoveStats = game.getRvMoveStats();
                    ps.setArray(++idx, connection.createArrayOf("String", pfMoveStats.stream().map(moveStat -> moveStat.preflopPosition().name()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("String", pfMoveStats.stream().map(moveStat -> moveStat.decision().action().name()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("UInt64", pfMoveStats.stream().map(moveStat -> moveStat.decision().amount()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("String", flMoveStats.stream().map(moveStat -> moveStat.preflopPosition().name()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("String", flMoveStats.stream().map(moveStat -> moveStat.decision().action().name()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("UInt64", flMoveStats.stream().map(moveStat -> moveStat.decision().amount()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("String", tnMoveStats.stream().map(moveStat -> moveStat.preflopPosition().name()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("String", tnMoveStats.stream().map(moveStat -> moveStat.decision().action().name()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("UInt64", tnMoveStats.stream().map(moveStat -> moveStat.decision().amount()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("String", rvMoveStats.stream().map(moveStat -> moveStat.preflopPosition().name()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("String", rvMoveStats.stream().map(moveStat -> moveStat.decision().action().name()).toArray()));
                    ps.setArray(++idx, connection.createArrayOf("UInt64", rvMoveStats.stream().map(moveStat -> moveStat.decision().amount()).toArray()));
                });
                int size = pokerGames.size();
                log.severe("Flushed " + size + " games in total");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    log.severe("Flush games error: " + e.getMessage() + ", tableName=" + tableName);
                }
                return size;
            } catch (DataAccessException exception) {
                log.severe("Flush games error: " + exception.getMessage() + ", tableName=" + tableName);
                return 0;
            }
        } catch (SQLException e) {
            log.severe("Couldn't create schema or dataset tables: " + e.getMessage() + ", tableName=" + tableName);
            return 0;
        }
    }

    @Override
    public int collectHandHistories(String schemaName, String tableName, List<PokerGame> pokerGames, String playerName) {
        try {
            List<String> hhs = pokerGames.stream().map(pokerGame -> pokerGame.toHandHistory888(playerName)).toList();
            createSchemaIfNotExists(schemaName);
            createHandHistoriesGamesTable(schemaName, tableName);
            try {
                String sql = String.format("""
                        insert into %s.%s (*) values (?)
                        """, schemaName, tableName);
                clickhouseJdbcTemplate.batchUpdate(sql, hhs, ClickhouseConfig.CLICKHOUSE_BATCH_SIZE, (ps, hh) -> {
                    ps.setString(1, hh);
                });
                int size = hhs.size();
                log.severe("Flushed " + size + " games in total");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.severe("Flush games error: " + e.getMessage() + ", tableName=" + tableName);
                }
                return size;
            } catch (DataAccessException exception) {
                log.severe("Flush games error: " + exception.getMessage() + ", tableName=" + tableName);
                return 0;
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e, () -> "Couldn't collect hand histories.");
            return 0;
        }
    }

    private void createHandHistoriesGamesTable(String schemaName, String tableName) {
        String query = String.format("""
                create table if not exists %s.%s
                (
                    hh String
                )
                engine = MergeTree ORDER BY (tuple())
                SETTINGS index_granularity = 8192
                """, schemaName, tableName);
        try {
            clickhouseJdbcTemplate.execute(query);
        } catch (Exception e) {
            log.log(Level.SEVERE, e, () -> "Couldn't create hand histories table.");
            throw new RuntimeException(e);
        }
    }


    private void createPokerGamesTable(String schemaName, String tableName) {
        String query = String.format("""
                create table if not exists %s.%s
                (
                    file_name                       String,
                    mining_type                     String,
                    hand_id                         String,
                    hand_date                       String,
                    sb_amount                       UInt64,
                    bb_amount                       UInt64,
                    competition_type                String,
                    board_size                      UInt64,
                    
                    player_name_by_position      Map(String, String),
                    pocket_cards_by_position     Map(String, String),
                    initial_stack_by_position    Map(String, UInt64),
                    profit_by_position           Map(String, Int64),
                        
                    fl_cards                        String,
                    tn_card                         String,
                    rv_card                         String,
                    
                    pf_moves Nested
                    (
                        position                 String,
                        action               String,
                        amount             UInt64
                    ),
                    
                    fl_moves Nested
                    (
                        position                 String,
                        action               String,
                        amount             UInt64
                    ),
                    
                    tn_moves Nested
                    (
                        position                 String,
                        action               String,
                        amount             UInt64
                    ),
                    
                    rv_moves Nested
                    (
                        position                 String,
                        action               String,
                        amount             UInt64
                    )
                )
                engine = MergeTree ORDER BY (tuple(hand_id, hand_date))
                SETTINGS index_granularity = 8192
                """, schemaName, tableName);
        try {
            clickhouseJdbcTemplate.execute(query);
        } catch (Exception e) {
            log.log(Level.SEVERE, e, () -> "Couldn't create poker games table.");
            throw new RuntimeException(e);
        }
    }

}
