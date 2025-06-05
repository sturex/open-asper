package dev.asper.app.component;

import com.clickhouse.jdbc.ClickHouseConnection;
import com.clickhouse.jdbc.ClickHouseDataSource;
import dev.asper.advice.*;
import dev.asper.clickhouse.*;
import dev.asper.common.feature.Feature;
import dev.asper.common.util.ExceptionHelper;
import dev.asper.poker.ai.RawAmount;
import dev.asper.poker.card.*;
import dev.asper.poker.chart.CardCellActionMap;
import dev.asper.poker.engine.*;
import dev.asper.poker.engine.spot.PreflopSpot;
import dev.asper.poker.enums.*;
import dev.asper.poker.holding.BoardFlush;
import dev.asper.poker.holding.BoardPair;
import dev.asper.poker.holding.BoardStraight;
import dev.asper.stat.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.clickhouse.domain.ClickHouseDataType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@Component
public class ClickhouseQueries {

    public static final double CARD_CELL_MIN_TOTAL = 10;
    public static final double MIN_BRANCH_WEIGHT = 0.01;
    private final ClickHouseConnection connection;

    private final JdbcTemplate clickhouseJdbcTemplate;

    public ClickhouseQueries(ClickHouseDataSource clickHouseDataSource) throws SQLException {
        connection = clickHouseDataSource.getConnection();
        this.clickhouseJdbcTemplate = new JdbcTemplate(clickHouseDataSource);
    }

    private static class PlayerRowMapper implements RowMapper<PlayerStat> {
        @Override
        public PlayerStat mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PlayerStat(
                    CompetitionType.valueOf(rs.getString("competition_type")),
                    rs.getString("player_name"),
                    BoardSize.of(rs.getInt("board_size")),
                    rs.getLong("total_hands_cnt"),
                    rs.getLong("open_hands_cnt"),
                    rs.getDouble("avg_profit"),
                    rs.getDouble("vpip"),
                    rs.getDouble("pfr"),
                    rs.getDouble("limp"),
                    rs.getDouble("pf_rr"),
                    rs.getDouble("wwsf"),
                    rs.getDouble("wtsd"),
                    rs.getDouble("wssd"));
        }

    }

    public Map<String, EnumMap<BoardSize, PlayerStat>> readPlayerStats(String playersSchemaName, String playersTableName) {
        String sql = "SELECT player_name, board_size, competition_type, total_hands_cnt, open_hands_cnt, avg_profit, vpip, pfr, limp, wwsf, wtsd, wssd, pf_rr FROM %s.%s".formatted(playersSchemaName, playersTableName);
        return clickhouseJdbcTemplate.query(sql, new PlayerRowMapper())
                .stream()
                .collect(Collectors.groupingBy(
                        PlayerStat::playerName,
                        Collectors.toMap(PlayerStat::boardSize,
                                stat -> stat,
                                (existing, replacement) -> existing,
                                () -> new EnumMap<>(BoardSize.class)
                        )
                ));
    }

    public void createPlayersTable(String pokerGamesSchemaName, String pokerGamesTableName, String playersSchemaName) {
        String sql = String.format("""
                CREATE TABLE %s.players
                        ENGINE MergeTree
                            PRIMARY KEY (player_name, board_size, competition_type)
                            ORDER BY (player_name, board_size, competition_type)
                            SETTINGS index_granularity = 8192
                    AS (SELECT player_name,
                               board_size,
                               competition_type,
                               total_hands_cnt,
                               open_hands_cnt,
                               avg_profit,
                               vpip,
                               pfr,
                               limp,
                               wwsf,
                               wtsd,
                               wssd,
                               pf_rr
                        FROM (
                                 SELECT player_name,
                                        board_size,
                                        competition_type,
                                        count()                                                                                                                 AS total_hands_cnt,
                                        round(avg(bb_profit * 100), 2)                                                                                          AS avg_profit,
                                        round(sum(vpip_sample) / total_hands_cnt, 3)                                                                            AS vpip,
                                        round(sum(pf_raiser) / total_hands_cnt, 3)                                                                              AS pfr,
                                        round(sum(limp_sample) / total_hands_cnt, 3)                                                                            AS limp,
                                        if(saw_flop_sum == 0, 0, round(won_hand_when_saw_flop_sum / saw_flop_sum, 3))                                           AS wwsf,
                                        if(saw_flop_sum == 0, 0, round(went_to_sd_cases_when_saw_flop / saw_flop_sum, 3))                                       AS wtsd,
                                        if(went_to_sd_cases_when_saw_flop == 0, 0, round(won_hand_at_sd_when_saw_flop_sum / went_to_sd_cases_when_saw_flop, 3)) AS wssd,
                                        if(opportunity_to_reraise_sum == 0, 0, round(sum(pf_reraise_sample) / opportunity_to_reraise_sum, 3))                   AS pf_rr,
                                        sum(went_to_sd_when_saw_flop)                                                                                           AS went_to_sd_cases_when_saw_flop,
                                        sum(won_hand_when_saw_flop)                                                                                             AS won_hand_when_saw_flop_sum,
                                        sum(won_hand_on_sd_when_saw_flop)                                                                                       AS won_hand_at_sd_when_saw_flop_sum,
                                        sum(saw_flop)                                                                                                           AS saw_flop_sum,
                                        sum(pocket_cards_open)                                                                                                  AS open_hands_cnt,
                                        sum(opportunity_to_reraise)                                                                                             AS opportunity_to_reraise_sum
                                 FROM (
                                          SELECT tupleElement(aj, 1)  AS player_name,
                                                 board_size,
                                                 competition_type,
                                                 tupleElement(aj, 2)  AS bb_profit,
                                                 tupleElement(aj, 3)  AS limp_sample,
                                                 tupleElement(aj, 4)  AS vpip_sample,
                                                 tupleElement(aj, 5)  AS pf_raiser,
                                                 tupleElement(aj, 12) AS saw_flop,
                                                 tupleElement(aj, 15) AS went_to_sd_when_saw_flop,
                                                 tupleElement(aj, 17) AS won_hand_when_saw_flop,
                                                 tupleElement(aj, 18) AS won_hand_on_sd_when_saw_flop,
                                                 tupleElement(aj, 19) AS pocket_cards_open,
                                                 tupleElement(aj, 20) AS pf_reraise_sample,
                                                 tupleElement(aj, 24) AS opportunity_to_reraise
                                          FROM (
                                                   SELECT arrayJoin(stat_array) AS aj,
                                                          board_size,
                                                          competition_type
                                                   FROM (
                                                            WITH tuple('', '') AS empty_moves_tuple
                                                            SELECT arrayZip(pf_moves.position, pf_moves.action)                                                                        AS pf_moves_zipped,
                                                                   arrayZip(fl_moves.position, fl_moves.action)                                                                        AS fl_moves_zipped,
                                                                   arrayZip(tn_moves.position, tn_moves.action)                                                                        AS tn_moves_zipped,
                                                                   arrayZip(rv_moves.position, rv_moves.action)                                                                        AS rv_moves_zipped,
                                                                   arrayUniq(pf_moves.position)                                                                                        AS pf_seat_count,
                                                                   arrayCount(x -> x == 'F', pf_moves.action)                                                                          AS pf_folds_count,
                                                                   arrayCount(x -> x == 'F', fl_moves.action)                                                                          AS fl_folds_count,
                                                                   arrayCount(x -> x == 'F', tn_moves.action)                                                                          AS tn_folds_count,
                                                                   arrayCount(x -> x == 'F', rv_moves.action)                                                                          AS rv_folds_count,
                                                                   if((pf_seat_count - pf_folds_count - fl_folds_count - tn_folds_count - rv_folds_count) > 1 AND rv_card != '', 1, 0) AS is_showdown,
                                                                   mapValues(mapApply((position, player_profit) -> (position, tuple(
                                                                       /* 1*/ player_name_by_position[position] AS pl_name,
                                                                       /* 2*/ player_profit / bb_amount AS bb_hand_profit,
                                                                       /* 3*/ if(arrayFirst(x -> (tupleElement(x, 2) != 'F' OR tupleElement(x, 1) == position), pf_moves_zipped) == tuple(position, 'C'), 1, 0) AS limp_sample,
                                                                       /* 4*/ if(arrayFirst(x -> (tupleElement(x, 1) == position AND (tupleElement(x, 2) != 'F')), pf_moves_zipped) != empty_moves_tuple, 1, 0) AS vpip_sample,
                                                                       /* 5*/ if(arrayFirst(x -> (tupleElement(x, 1) == position AND (tupleElement(x, 2) == 'R')), pf_moves_zipped) != empty_moves_tuple, 1, 0) AS pf_raiser,
                                                                       /* 6*/ if(tupleElement(arrayFirst(x -> (tupleElement(x, 2) == 'R'), pf_moves_zipped), 1) == position, 1, 0) AS pf_first_raiser,
                                                                       /* 7*/ if(arrayFirst(x -> (tupleElement(x, 1) == position AND (tupleElement(x, 2) == 'F')), pf_moves_zipped) != empty_moves_tuple, 1, 0) AS folded_on_preflop,
                                                                       /* 8*/ if(arrayFirst(x -> (tupleElement(x, 1) == position AND (tupleElement(x, 2) == 'F')), fl_moves_zipped) != empty_moves_tuple, 1, 0) AS folded_on_flop,
                                                                       /* 9*/ if(arrayFirst(x -> (tupleElement(x, 1) == position AND (tupleElement(x, 2) == 'F')), tn_moves_zipped) != empty_moves_tuple, 1, 0) AS folded_on_turn,
                                                                       /*10*/ if(arrayFirst(x -> (tupleElement(x, 1) == position AND (tupleElement(x, 2) == 'F')), rv_moves_zipped) != empty_moves_tuple, 1, 0) AS folded_on_river,
                                                                       /*11*/ if(folded_on_flop == 1 OR folded_on_turn == 1 OR folded_on_river == 1, 1, 0) AS folded_on_postflop,
                                                                       /*12*/ if(folded_on_preflop == 0 AND fl_cards != '', 1, 0) AS saw_flop,
                                                                       /*13*/ if(folded_on_preflop == 0 AND folded_on_flop == 0 AND tn_card != '', 1, 0) AS saw_turn,
                                                                       /*14*/ if(folded_on_preflop == 0 AND folded_on_flop == 0 AND folded_on_turn == 0 AND rv_card != '', 1, 0) AS saw_river,
                                                                       /*15*/ if(is_showdown == 1 AND saw_flop == 1 AND folded_on_postflop == 0, 1, 0) AS went_to_sd_when_saw_flop,
                                                                       /*16*/ if(player_profit > 0, 1, 0) AS won_hand,
                                                                       /*17*/ if(won_hand == 1 AND saw_flop == 1, 1, 0) AS won_hand_when_saw_flop,
                                                                       /*18*/ if(went_to_sd_when_saw_flop == 1 AND won_hand == 1, 1, 0) AS won_hand_on_sd_when_saw_flop,
                                                                       /*19*/ if(pocket_cards_by_position[position] != '', 1, 0) AS pocket_cards_open,
                                                                       /*20*/ if(pf_raiser AND NOT pf_first_raiser, 1, 0) AS pf_reraise_sample,
                    
                                                                       /*21*/ indexOf(pf_moves_zipped, tuple(position, 'F')) AS pf_folded_idx,
                                                                       /*22*/ arrayFirst(x -> (tupleElement(x, 2) == 'R' AND tupleElement(x, 1) != position), pf_moves_zipped) AS first_opp_raise_tuple,
                                                                       /*23*/ indexOf(pf_moves_zipped, first_opp_raise_tuple) AS pf_first_opp_raise_idx,
                    
                                                                       /*24*/ if((NOT pf_first_raiser) AND pf_first_opp_raise_idx != 0 AND
                                                                                 (pf_folded_idx == 0 OR pf_folded_idx > pf_first_opp_raise_idx), 1, 0) AS opportunity_to_reraise
                                                                       )), profit_by_position))                                                                                        AS stat_array,
                                                                   board_size,
                                                                   competition_type
                                                            FROM %s.%s)
                                                   )
                                          )
                                 GROUP BY player_name, board_size, competition_type))
                """, playersSchemaName, pokerGamesSchemaName, pokerGamesTableName);
        clickhouseJdbcTemplate.execute(sql);
    }

    public List<PokerHandWithMiningType> fetchPokerHandsContainingPlayerNames(String schemaName, String tableName, List<String> playerNames) {
        String playerNamesArrayStr = playerNames.stream()
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(", ", "[", "]"));
        String q = String.format("select * from %s.%s where hasAny(mapValues(player_name_by_position), %s )", schemaName, tableName, playerNamesArrayStr);
        return clickhouseJdbcTemplate.query(q, (rs, rowNum) -> recoverPokerHand(rs));
    }

    public List<PokerHandWithMiningType> fetchPokerHands(String schemaName, String tableName) {
        String q = String.format("select * from %s.%s)", schemaName, tableName);
        return clickhouseJdbcTemplate.query(q, (resultSet, columnIndex) -> recoverPokerHand(resultSet));
    }

    public List<PokerHandWithMiningType> fetchPokerHands(String schemaName, String tableName, int offset, int limit) {
        String q = String.format("select * from %s.%s LIMIT %d, %d", schemaName, tableName, offset, limit);
        return clickhouseJdbcTemplate.query(q, (resultSet, columnIndex) -> recoverPokerHand(resultSet));
    }

    public List<PokerHandWithMiningType> fetchPokerHandsContainingPlayerName(String schemaName, String tableName, String playerName) {
        return fetchPokerHandsContainingPlayerNames(schemaName, tableName, List.of(playerName));
    }

    record PreflopStatisticsRow(PreflopSpot preflopSpot, Branch outBranch, Branch inBranch, CardCell cardCell, RawFcar rawFcar) implements HasInBranch, HasOutBranch {
    }

    public Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> computePreflopBranchStatistics(String schemaName, PreflopSpot preflopSpot, OpponentType opponentType) {
        String q;
        if (opponentType == OpponentType.OVERALL) {
            q = String.format("select HERO_BRANCH hb, PREV_BRANCH pdb, " +
                    "POCKET_CARDS_NAME pcn, " +
                    "countIf(HERO_ACTION == 'F') f_count, countIf(HERO_ACTION == 'C') c_count, " +
                    "countIf(HERO_ACTION == 'A') a_count, countIf(HERO_ACTION == 'R') r_count " +
                    "from %s.%s " +
                    "group by hb, pdb, pcn", schemaName, preflopSpot.name());
        } else {
            String oppTypesFilter = opponentType.competitionTypes().stream().map(Enum::name)
                    .map(s -> "'" + s + "'")
                    .collect(Collectors.joining(",", "[", "]"));
            q = String.format("select HERO_BRANCH hb, PREV_BRANCH pdb, " +
                    "POCKET_CARDS_NAME pcn, " +
                    "countIf(HERO_ACTION == 'F') f_count, countIf(HERO_ACTION == 'C') c_count, " +
                    "countIf(HERO_ACTION == 'A') a_count, countIf(HERO_ACTION == 'R') r_count " +
                    "from %s.%s " +
                    "  where has(OPP_TYPES, '%s') and has(%s, COMPETITION_TYPE) " +
                    "group by hb, pdb, pcn", schemaName, preflopSpot.name(), opponentType.name(), oppTypesFilter);
        }
        List<PreflopStatisticsRow> preflopData = clickhouseJdbcTemplate.query(q, (resultSet, columnIndex) -> {
            CardCell cardCell = CardCell.from(resultSet.getString("pcn"));
            int fCount = (int) ((long) resultSet.getInt("f_count"));
            int cCount = (int) ((long) resultSet.getInt("c_count"));
            int aCount = (int) ((long) resultSet.getInt("a_count"));
            int rCount = (int) ((long) resultSet.getInt("r_count"));
            RawFcar rawFcar = RawFcar.from(fCount, cCount, aCount, rCount);
            Branch outBranch = Branch.valueOf(resultSet.getString("hb"));
            Branch inBranch = Branch.valueOf(resultSet.getString("pdb"));
            return new PreflopStatisticsRow(preflopSpot, outBranch, inBranch, cardCell, rawFcar);
        });

        Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> map = toStatisticsMap(preflopData);
        map.values().forEach(m -> {
            if (m.size() == 1 && m.entrySet().stream().findFirst().orElseThrow().getKey() == Branch.F) {
                // remove nodes with single F edge
                m.clear();
            } else {
                m.entrySet().removeIf(e -> {
                    PreflopBranchStatistics branchStatistics = e.getValue();
                    return branchStatistics.branchProbability() < MIN_BRANCH_WEIGHT || branchStatistics.preflopRange().isEmpty();
                });
                double branchTotalWeightAfterPrune = m.values().stream().mapToDouble(PreflopBranchStatistics::branchProbability).sum();
                m.replaceAll((branch, bs) -> new PreflopBranchStatistics(bs.branchProbability() / branchTotalWeightAfterPrune, bs.preflopRange()));
            }
        });
        map.entrySet().removeIf(e -> e.getValue().isEmpty());
        return map;
    }

    private Map<PreflopStatisticsKey, EnumMap<Branch, PreflopBranchStatistics>> toStatisticsMap(List<PreflopStatisticsRow> preflopData) {
        List<Branch> orderedByPercentage = findTopInBranches(preflopData);
        Map<PreflopStatisticsKey, List<PreflopStatisticsRow>> byKey = preflopData.stream()
                .collect(Collectors.groupingBy(row -> new PreflopStatisticsKey(row.preflopSpot, row.inBranch().findClosest(orderedByPercentage))));
        return byKey.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    List<PreflopStatisticsRow> keyRows = e.getValue();
                    List<Branch> keyOrderedTopBranches = findTopOutBranches(keyRows);
                    double keyTotal = keyRows.stream().mapToDouble(row -> row.rawFcar().total()).sum();
                    Map<Branch, List<PreflopStatisticsRow>> byBranch = keyRows.stream()
                            .collect(Collectors.groupingBy(preflopStatisticsRow -> preflopStatisticsRow.outBranch().findClosest(keyOrderedTopBranches)));
                    return byBranch.entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, ee -> {
                                double total = ee.getValue().stream().mapToDouble(row -> row.rawFcar().total()).sum();
                                double weight = total / keyTotal;
                                EnumMap<CardCell, Double> ccm = new EnumMap<>(CardCell.class);
                                List<PreflopStatisticsRow> value = ee.getValue();
                                value.forEach(p -> {
                                    int cardCellTotal = p.rawFcar.total();
//                                    if (cardCellTotal > CARD_CELL_MIN_TOTAL) {
                                    ccm.put(p.cardCell, cardCellTotal / total);
//                                    }
                                });
                                PreflopRange range = PreflopRange.with(ccm);
                                return new PreflopBranchStatistics(weight, range);
                            }, (p1, p2) -> p1, () -> new EnumMap<>(Branch.class)));
                }));
    }

    private List<Branch> findTopInBranches(List<? extends HasInBranch> hasInBranches) {
        return hasInBranches.stream()
                .collect(Collectors.groupingBy(HasInBranch::inBranch, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.<Map.Entry<Branch, Long>>comparingLong(Map.Entry::getValue).reversed())
                .map(Map.Entry::getKey).limit(6)
                .sorted(Comparator.comparingDouble(branch -> Branch.branchesWithoutAmount.contains(branch) ? -1.0 : branch.getPercentage()))
                .collect(Collectors.toList());
    }

    private List<Branch> findTopOutBranches(List<? extends HasOutBranch> hasInBranches) {
        return hasInBranches.stream()
                .collect(Collectors.groupingBy(HasOutBranch::outBranch, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.<Map.Entry<Branch, Long>>comparingLong(Map.Entry::getValue).reversed())
                .map(Map.Entry::getKey).limit(6)
                .sorted(Comparator.comparingDouble(branch -> Branch.branchesWithoutAmount.contains(branch) ? -1.0 : branch.getPercentage()))
                .collect(Collectors.toList());
    }

    record PostflopStatisticsRow(PokerSituation pokerSituation,
                                 Branch outBranch,
                                 Branch inBranch,
                                 BoardFlush boardFlush,
                                 BoardStraight boardStraight,
                                 BoardPair boardPair,
                                 int cnt) implements HasInBranch, HasOutBranch {
    }


    public Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> computePostflopBranchStatistics(String schemaName, PokerSituation pokerSituation, OpponentType opponentType) {
        String oppTypesFilter = opponentType.competitionTypes().stream().map(Enum::name)
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(",", "[", "]"));
        String oppTypeFiltersStr = opponentType == OpponentType.OVERALL ? "" : "  where has(OPP_TYPES, '%s') and has(%s,  COMPETITION_TYPE) ".formatted(opponentType.name(), oppTypesFilter);
        Street street = pokerSituation.getStreet();
        String q = switch (street) {
            case FL -> String.format("select HERO_BRANCH hb, PREV_BRANCH pdb, FL_HOLDING_BOARD_FLUSH fl, FL_HOLDING_BOARD_STRAIGHT st, FL_HOLDING_BOARD_PAIR pa, count() cnt " +
                    "from %s.%s\n" +
                    oppTypeFiltersStr +
                    "group by fl, st, pa, hb, pdb", schemaName, pokerSituation);
            case TN -> String.format("select HERO_BRANCH hb, PREV_BRANCH pdb, TN_HOLDING_BOARD_FLUSH fl, TN_HOLDING_BOARD_STRAIGHT st, TN_HOLDING_BOARD_PAIR pa, count() cnt " +
                    "from %s.%s\n" +
                    oppTypeFiltersStr +
                    "group by fl, st, pa, hb, pdb", schemaName, pokerSituation);
            case RV -> String.format("select HERO_BRANCH hb, PREV_BRANCH pdb, RV_HOLDING_BOARD_FLUSH fl, RV_HOLDING_BOARD_STRAIGHT st, RV_HOLDING_BOARD_PAIR pa, count() cnt " +
                    "from %s.%s\n" +
                    oppTypeFiltersStr +
                    "group by fl, st, pa, hb, pdb", schemaName, pokerSituation);
            default -> throw new IllegalStateException("Unexpected value: " + street);
        };
        List<PostflopStatisticsRow> postflopStatisticsRows = clickhouseJdbcTemplate.query(q, (resultSet, columnIndex) -> {
            int cnt = (int) ((long) resultSet.getInt("cnt"));
            BoardFlush boardFlush = BoardFlush.valueOf(resultSet.getString("fl"));
            BoardStraight boardStraight = BoardStraight.valueOf(resultSet.getString("st"));
            BoardPair boardPair = BoardPair.valueOf(resultSet.getString("pa"));
            Branch outBranch = Branch.valueOf(resultSet.getString("hb"));
            Branch prevBranch = Branch.valueOf(resultSet.getString("pdb"));
            return new PostflopStatisticsRow(pokerSituation, outBranch, prevBranch, boardFlush, boardStraight, boardPair, cnt);
        });

        Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> map = toPostflopStatistics(postflopStatisticsRows);
        map.values().forEach(m -> {
            if (m.size() == 1 && m.entrySet().stream().findFirst().orElseThrow().getKey() == Branch.F) {
                // remove nodes with single F edge
                m.clear();
            } else {
                m.entrySet().removeIf(e -> e.getValue() < MIN_BRANCH_WEIGHT);
                double branchTotalWeightAfterPrune = m.values().stream().mapToDouble(Double::doubleValue).sum();
                m.replaceAll((branch, bs) -> bs / branchTotalWeightAfterPrune);
            }
        });
        map.entrySet().removeIf(e -> e.getValue().isEmpty());
        return map;
    }

    private Map<PostflopBoardStatisticsKey, EnumMap<Branch, Double>> toPostflopStatistics(List<PostflopStatisticsRow> postflopStatisticsRows) {
        List<Branch> orderedTopBranches = findTopInBranches(postflopStatisticsRows);
        Map<PostflopBoardStatisticsKey, List<PostflopStatisticsRow>> byKey = postflopStatisticsRows.stream()
                .collect(Collectors.groupingBy(row -> new PostflopBoardStatisticsKey(row.pokerSituation,
                        row.inBranch().findClosest(orderedTopBranches),
                        row.boardFlush(),
                        row.boardStraight(),
                        row.boardPair())));
        return byKey.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    List<PostflopStatisticsRow> keyRows = e.getValue();
                    List<Branch> keyOrderedTopBranches = findTopOutBranches(keyRows);
                    double keyTotal = keyRows.stream().mapToDouble(PostflopStatisticsRow::cnt).sum();
                    return keyRows.stream()
                            .collect(Collectors.toMap(postflopStatisticsRow -> postflopStatisticsRow.outBranch().findClosest(keyOrderedTopBranches),
                                    kr -> kr.cnt / keyTotal,
                                    Double::sum,
                                    () -> new EnumMap<>(Branch.class)));
                }));
    }

    private interface HasInBranch {
        Branch inBranch();
    }

    private interface HasOutBranch {
        Branch outBranch();
    }

    record PostflopRoughStatisticsRow(PokerSituation pokerSituation,
                                      Branch outBranch,
                                      Branch inBranch,
                                      int cnt) implements HasInBranch, HasOutBranch {
    }

    public Map<PostflopStatisticsKey, EnumMap<Branch, Double>> computePostflopRoughBranchStatistics(String schemaName, PokerSituation pokerSituation, OpponentType opponentType) {
        String oppTypesFilter = opponentType.competitionTypes().stream().map(Enum::name)
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(",", "[", "]"));
        String oppTypeFiltersStr = opponentType == OpponentType.OVERALL ? "" : "  where has(OPP_TYPES, '%s') and has(%s,  COMPETITION_TYPE) ".formatted(opponentType.name(), oppTypesFilter);
        Street street = pokerSituation.getStreet();
        String q = switch (street) {
            case FL -> String.format("select HERO_BRANCH hb, PREV_BRANCH pdb, count() cnt " +
                    "from %s.%s\n" +
                    oppTypeFiltersStr +
                    "group by hb, pdb", schemaName, pokerSituation);
            case TN -> String.format("select HERO_BRANCH hb, PREV_BRANCH pdb, count() cnt " +
                    "from %s.%s\n" +
                    oppTypeFiltersStr +
                    "group by hb, pdb", schemaName, pokerSituation);
            case RV -> String.format("select HERO_BRANCH hb, PREV_BRANCH pdb, count() cnt " +
                    "from %s.%s\n" +
                    oppTypeFiltersStr +
                    "group by hb, pdb", schemaName, pokerSituation);
            default -> throw new IllegalStateException("Unexpected value: " + street);
        };
        // TODO
        List<PostflopRoughStatisticsRow> postflopRoughStatisticsRows = clickhouseJdbcTemplate.query(q, (resultSet, columnIndex) -> {
            int cnt = (int) ((long) resultSet.getInt("cnt"));
            Branch outBranch = Branch.valueOf(resultSet.getString("hb"));
            Branch inBranch = Branch.valueOf(resultSet.getString("pdb"));
            return new PostflopRoughStatisticsRow(pokerSituation, outBranch, inBranch, cnt);
        });
        Map<PostflopStatisticsKey, EnumMap<Branch, Double>> map = toPostflopRoughStatistics(postflopRoughStatisticsRows);
        map.values().forEach(m -> {
            if (m.size() == 1 && m.entrySet().stream().findFirst().orElseThrow().getKey() == Branch.F) {
                // remove nodes with single F edge
                m.clear();
            } else {
                m.entrySet().removeIf(e -> e.getValue() < MIN_BRANCH_WEIGHT);
                double branchTotalWeightAfterPrune = m.values().stream().mapToDouble(Double::doubleValue).sum();
                m.replaceAll((branch, bs) -> bs / branchTotalWeightAfterPrune);
            }
        });
        map.entrySet().removeIf(e -> e.getValue().isEmpty());
        return map;
    }


    private Map<PostflopStatisticsKey, EnumMap<Branch, Double>> toPostflopRoughStatistics(List<PostflopRoughStatisticsRow> postflopRoughStatisticsRows) {
        List<Branch> orderedTopBranches = findTopInBranches(postflopRoughStatisticsRows);
        Map<PostflopStatisticsKey, List<PostflopRoughStatisticsRow>> byKey = postflopRoughStatisticsRows.stream()
                .collect(Collectors.groupingBy(row -> new PostflopStatisticsKey(row.pokerSituation(),
                        row.inBranch().findClosest(orderedTopBranches))));
        return byKey.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    List<PostflopRoughStatisticsRow> keyRows = e.getValue();
                    List<Branch> keyOrderedTopBranches = findTopOutBranches(keyRows);
                    double keyTotal = keyRows.stream().mapToDouble(PostflopRoughStatisticsRow::cnt).sum();
                    return keyRows.stream()
                            .collect(Collectors.toMap(postflopRoughStatisticsRow -> postflopRoughStatisticsRow.outBranch().findClosest(keyOrderedTopBranches),
                                    kr -> kr.cnt / keyTotal,
                                    Double::sum,
                                    () -> new EnumMap<>(Branch.class)));
                }));
    }


    private PokerHandWithMiningType recoverPokerHand(ResultSet resultSet) throws SQLException {
        MiningType miningType = MiningType.valueOf(resultSet.getString("mining_type"));
        String handId = resultSet.getString("hand_id");
        String handDate = resultSet.getString("hand_date");
        long sbAmount = resultSet.getLong("sb_amount");
        long bbAmount = resultSet.getLong("bb_amount");
        String flCards = resultSet.getString("fl_cards");
        String tnCard = resultSet.getString("tn_card");
        String rvCard = resultSet.getString("rv_card");
        CompetitionType competitionType = CompetitionType.valueOf(resultSet.getString("competition_type"));

        EnumMap<PreflopPosition, String> playerNameByPfPosition = ((Map<?, ?>) resultSet.getObject("player_name_by_position", Map.class))
                .entrySet().stream().collect(Collectors.toMap(o -> PreflopPosition.valueOf(String.valueOf(o.getKey())), o -> (String) o.getValue(), (s, s2) -> s, () -> new EnumMap<>(PreflopPosition.class)));
        EnumMap<PreflopPosition, Integer> profitByPfPosition = ((Map<?, ?>) resultSet.getObject("profit_by_position", Map.class))
                .entrySet().stream().collect(Collectors.toMap(o -> PreflopPosition.valueOf(String.valueOf(o.getKey())), o -> (int) ((long) o.getValue()), (s, s2) -> s, () -> new EnumMap<>(PreflopPosition.class)));
        EnumMap<PreflopPosition, String> pocketCardsByPosition = ((Map<?, ?>) resultSet.getObject("pocket_cards_by_position", Map.class))
                .entrySet().stream().collect(Collectors.toMap(o -> PreflopPosition.valueOf(String.valueOf(o.getKey())), e -> (String) e.getValue(), (s, s2) -> s, () -> new EnumMap<>(PreflopPosition.class)));
        EnumMap<PreflopPosition, Integer> initialStackByPosition = ((Map<?, ?>) resultSet.getObject("initial_stack_by_position", Map.class))
                .entrySet().stream().collect(Collectors.toMap(o -> PreflopPosition.valueOf(String.valueOf(o.getKey())), o -> (int) ((long) o.getValue()), (s, s2) -> s, () -> new EnumMap<>(PreflopPosition.class)));

        PreflopPosition[] pfPositions = Stream.of((String[]) (resultSet.getArray("pf_moves.position").getArray())).map(PreflopPosition::valueOf).toArray(PreflopPosition[]::new);
        Action[] pfDecisionTypes = Stream.of((String[]) (resultSet.getArray("pf_moves.action").getArray())).map(Action::valueOf).toArray(Action[]::new);
        long[] pfAmounts = (long[]) (resultSet.getArray("pf_moves.amount").getArray());

        PreflopPosition[] flPositions = Stream.of((String[]) (resultSet.getArray("fl_moves.position").getArray())).map(PreflopPosition::valueOf).toArray(PreflopPosition[]::new);
        Action[] flDecisionTypes = Stream.of((String[]) (resultSet.getArray("fl_moves.action").getArray())).map(Action::valueOf).toArray(Action[]::new);
        long[] flAmounts = (long[]) (resultSet.getArray("fl_moves.amount").getArray());

        PreflopPosition[] tnPositions = Stream.of((String[]) (resultSet.getArray("tn_moves.position").getArray())).map(PreflopPosition::valueOf).toArray(PreflopPosition[]::new);
        Action[] tnDecisionTypes = Stream.of((String[]) (resultSet.getArray("tn_moves.action").getArray())).map(Action::valueOf).toArray(Action[]::new);
        long[] tnAmounts = (long[]) (resultSet.getArray("tn_moves.amount").getArray());

        PreflopPosition[] rvPositions = Stream.of((String[]) (resultSet.getArray("rv_moves.position").getArray())).map(PreflopPosition::valueOf).toArray(PreflopPosition[]::new);
        Action[] rvDecisionTypes = Stream.of((String[]) (resultSet.getArray("rv_moves.action").getArray())).map(Action::valueOf).toArray(Action[]::new);
        long[] rvAmounts = (long[]) (resultSet.getArray("rv_moves.amount").getArray());

        EnumMap<PreflopPosition, PlayerInitialState> playerInitialStates = combinePlayerInitialStateMap(playerNameByPfPosition, initialStackByPosition, pocketCardsByPosition);
        return new PokerHandWithMiningType(new PokerHand(
                handId,
                handDate,
                (int) sbAmount,
                (int) bbAmount,
                playerInitialStates,
                combineMoves(pfPositions, pfDecisionTypes, pfAmounts),
                combineMoves(flPositions, flDecisionTypes, flAmounts),
                combineMoves(tnPositions, tnDecisionTypes, tnAmounts),
                combineMoves(rvPositions, rvDecisionTypes, rvAmounts),
                flCards == null || flCards.isBlank() ? Cards.empty : Cards.from(StringUtils.substringBetween(flCards, "[", "]")),
                tnCard == null || tnCard.isEmpty() ? null : Card.from(tnCard),
                rvCard == null || rvCard.isEmpty() ? null : Card.from(rvCard),
                profitByPfPosition,
                competitionType
        ), miningType);
    }

    private EnumMap<PreflopPosition, PlayerInitialState> combinePlayerInitialStateMap(Map<PreflopPosition, String> playerNameByPfPosition, Map<PreflopPosition, Integer> initialStackByPosition, Map<PreflopPosition, String> cards) {
        return playerNameByPfPosition.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                            String cardsStr = cards.get(e.getKey());
                            return cardsStr.isEmpty() ? new PlayerInitialState(e.getValue(), initialStackByPosition.get(e.getKey()), 0) :
                                    new PlayerInitialState(e.getValue(), CardPair.from(cardsStr), initialStackByPosition.get(e.getKey()), 0);
                        }, (o, o2) -> o,
                        () -> new EnumMap<>(PreflopPosition.class)));
    }

    private List<Move> combineMoves(PreflopPosition[] positions, Action[] decisionTypes, long[] amounts) {
        return IntStream.range(0, positions.length).mapToObj(idx -> new Move(positions[idx], new Decision(decisionTypes[idx], (int) amounts[idx]))).collect(Collectors.toList());
    }

    public int rowCount(String schemaName, String tableName) {
        String q = String.format("select count() from %s.%s", schemaName, tableName);
        try {
            return Optional.ofNullable(clickhouseJdbcTemplate.queryForObject(q, Integer.class)).orElse(0);
        } catch (Exception e) {
            return 0;
        }
    }

    public String[] columnNames(String schemaName, String tableName) {
        String q = String.format("describe table %s.%s", schemaName, tableName);
        String[] names = clickhouseJdbcTemplate.queryForList(q)
                .stream().map(d -> (String) d.get("name")).toArray(String[]::new);
        ExceptionHelper.throwIf(names.length == 0,
                () -> new RuntimeException("column names are empty"));
        return names;
    }

    public Set<String> getTableNames(String schemaName, ClickhouseEngineType clickhouseEngineType) {
        String q = String.format("""                
                        SELECT name
                        FROM system.tables
                        WHERE database == '%s' and engine == '%s'
                        """,
                schemaName, clickhouseEngineType.toString());
        return clickhouseJdbcTemplate.queryForList(q)
                .stream().map(d -> (String) d.get("name")).collect(Collectors.toSet());
    }

    public TypeColumn[] typeColumns(String schemaName, String tableName) {
        String q = String.format("SELECT type, name FROM system.columns where database == '%s' and table == '%s'", schemaName, tableName);
        return clickhouseJdbcTemplate.queryForList(q)
                .stream().map(d -> new TypeColumn(ClickHouseDataType.fromTypeString((String) d.get("type")), (String) d.get("name"))).toArray(TypeColumn[]::new);
    }

    public Map<String, TypeColumn[]> typeColumns(String schemaName, Collection<String> tableNames) {
        String tableNamesArrayStr = tableNames.stream()
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(", ", "[", "]"));
        String q = String.format("SELECT table, type, name FROM system.columns where database == '%s' and table in %s", schemaName, tableNamesArrayStr);
        return clickhouseJdbcTemplate.queryForList(q).stream()
                .map(d -> Pair.of((String) d.get("table"), new TypeColumn(ClickHouseDataType.fromTypeString((String) d.get("type")), (String) d.get("name")))).toList().stream()
                .collect(Collectors.groupingBy(Pair::getLeft, Collectors.mapping(Pair::getRight, Collectors.toList()))).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toArray(TypeColumn[]::new)));
    }

    public String showCreateTable(String schemaName, String tableName) {
        String q = String.format("show create table %s.%s", schemaName, tableName);
        return Optional.ofNullable(clickhouseJdbcTemplate.queryForObject(q, String.class))
                .orElseThrow(() -> new RuntimeException("npe in showCreateTable query"));
    }

    public boolean exists(String schemaName, String tableName) {
        String q = String.format("exists table %s.%s", schemaName, tableName);
        return Optional.ofNullable(clickhouseJdbcTemplate.queryForObject(q, Integer.class))
                .orElseThrow(() -> new RuntimeException("npe in exists query")) == 1;
    }

    public ActionMap actionMap(String datasetsSchemaName,
                               String playersSchemaName,
                               String datasetTableName,
                               List<PlayerStatFilter> playerStatFilters,
                               List<String> playerNames,
                               double minValueThreshold) {
        String playerFilterTotal = createPlayerNamesFilter(playerStatFilters, playerNames);
        String q = String.format("""                
                        SELECT
                        countIf(HERO_ACTION == 'F') f_count,
                        countIf(HERO_ACTION == 'C') c_count,
                        countIf(HERO_ACTION == 'A') a_count,
                        countIf(HERO_ACTION == 'R') r_count
                        FROM %s.%s
                        %s
                        """,
                datasetsSchemaName,
                datasetTableName,
                playerFilterTotal.isEmpty() ? "" : "inner join " +
                        playersSchemaName + ".players on " + datasetTableName + ".HERO_NAME == players.player_name where " +
                        playerFilterTotal);
        return clickhouseJdbcTemplate.queryForObject(q, (rs, rowNum) -> {
            int fCount = rs.getInt("f_count");
            int cCount = rs.getInt("c_count");
            int aCount = rs.getInt("a_count");
            int rCount = rs.getInt("r_count");
            return ActionMap.from(fCount, cCount, aCount, rCount, minValueThreshold);
        });
    }

    public List<CardCellActionMap> cardCellStrategies(String datasetsSchemaName,
                                                      String playersSchemaName,
                                                      String datasetTableName,
                                                      List<PlayerStatFilter> playerStatFilters,
                                                      List<String> playerNames,
                                                      double minValueThreshold) {
        String playerFilterTotal = createPlayerNamesFilter(playerStatFilters, playerNames);
        String q = String.format("""                
                        SELECT POCKET_CARDS_NAME pcn,
                        countIf(HERO_ACTION == 'F') f_count,
                        countIf(HERO_ACTION == 'C') c_count,
                        countIf(HERO_ACTION == 'A') a_count,
                        countIf(HERO_ACTION == 'R') r_count
                        FROM %s.%s
                        %s
                        group by pcn
                        """,
                datasetsSchemaName,
                datasetTableName,
                playerFilterTotal.isEmpty() ? "" : "inner join " +
                        playersSchemaName + ".players on " + datasetTableName + ".HERO_NAME == players.player_name where " +
                        playerFilterTotal);
        return clickhouseJdbcTemplate.queryForList(q)
                .stream().map(d -> {
                    CardCell cardCell = CardCell.from((String) d.get("pcn"));
                    int fCount = (int) ((long) d.get("f_count"));
                    int cCount = (int) ((long) d.get("c_count"));
                    int aCount = (int) ((long) d.get("a_count"));
                    int rCount = (int) ((long) d.get("r_count"));
                    ActionMap actionMap = ActionMap.from(fCount, cCount, aCount, rCount, minValueThreshold);
                    return new CardCellActionMap(cardCell, actionMap);
                }).toList();
    }

    public List<CardCellActionMap> cardCellStrategies2(String datasetsSchemaName,
                                                       String playersSchemaName,
                                                       String datasetTableName,
                                                       List<PlayerStatFilter> playerStatFilters,
                                                       List<String> playerNames,
                                                       double minValueThreshold) {
        String playerFilterTotal = createPlayerNamesFilter(playerStatFilters, playerNames);
        String q = String.format("""                
                        SELECT POCKET_CARDS_NAME pcn,
                        countIf(HERO_ACTION == 'F') f_count,
                        countIf(HERO_ACTION == 'C') c_count,
                        countIf(HERO_ACTION == 'A') a_count,
                        countIf(HERO_ACTION == 'R') r_count,
                        avg(PROFIT_BB) tot_profit,
                        avgIf(HERO_ACTION == 'F', PROFIT_BB) f_profit,
                        avgIf(HERO_ACTION == 'C', PROFIT_BB) c_profit,
                        avgIf(HERO_ACTION == 'A', PROFIT_BB) a_profit,
                        avgIf(HERO_ACTION == 'R', PROFIT_BB) r_profit
                        FROM %s.%s
                        %s
                        group by pcn
                        """,
                datasetsSchemaName,
                datasetTableName,
                playerFilterTotal.isEmpty() ? "" : "inner join " +
                        playersSchemaName + ".players on " + datasetTableName + ".HERO_NAME == players.player_name where " +
                        playerFilterTotal);
        return clickhouseJdbcTemplate.queryForList(q)
                .stream().map(d -> {
                    CardCell cardCell = CardCell.from((String) d.get("pcn"));
                    int fCount = (int) ((long) d.get("f_count"));
                    int cCount = (int) ((long) d.get("c_count"));
                    int aCount = (int) ((long) d.get("a_count"));
                    int rCount = (int) ((long) d.get("r_count"));
                    double totProfit = (double) d.get("tot_profit");
                    double fProfit = (double) d.get("f_profit");
                    double cProfit = (double) d.get("c_profit");
                    double aProfit = (double) d.get("a_profit");
                    double rProfit = (double) d.get("r_profit");


                    ActionMap actionMap = ActionMap.from(fCount, cCount, aCount, rCount, minValueThreshold);
                    return new CardCellActionMap(cardCell, actionMap);
                }).toList();
    }

    private String createPlayerNamesFilter(List<PlayerStatFilter> playerStatFilters, List<String> playerNames) {
        String playerStatFiltersStr = playerStatFilters.isEmpty() ? "" : playerStatFilters.stream()
                .map(psf -> String.format("(limp > %.6f and limp < %.6f and " +
                                "total_hands_cnt > %d and total_hands_cnt < %d and " +
                                "PROFIT_BB > %.6f and PROFIT_BB < %.6f " +
                                "and vpip > %.6f and vpip < %.6f and " +
                                "pfr > %.6f and pfr < %.6f and " +
                                "wwsf > %.6f and wwsf < %.6f and " +
                                "wtsd > %.6f and wtsd < %.6f and " +
                                "wssd > %.6f and wssd < %.6f and " +
                                "(vpip - pfr) > %.6f and (vpip - pfr) < %.6f and " +
                                "pf_rr > %.6f and pf_rr < %.6f and " +
                                "competition_type == '%s' and " +
                                "board_size == %d)",
                        psf.limpMin(), psf.limpMax(),
                        psf.handCountMin(), psf.handCountMax(),
                        psf.avgProfitBb100Min(), psf.avgProfitBb100Max(),
                        psf.vpipMin(), psf.vpipMax(),
                        psf.pfrMin(), psf.pfrMax(),
                        psf.wwsfMin(), psf.wwsfMax(),
                        psf.wtsdMin(), psf.wtsdMax(),
                        psf.wssdMin(), psf.wssdMax(),
                        psf.vpipPfrDiffMin(), psf.vpipPfrDiffMax(),
                        psf.pfrrMin(), psf.pfrrMax(),
                        psf.competitionType(),
                        psf.boardSize().size()))
                .collect(Collectors.joining(" or ", "(", ") "));
        String playerNamesFilterStr = playerNames.isEmpty() ? "" : " player_name in " + playerNames.stream()
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(", ", "[", "]"));
        return playerStatFiltersStr.isEmpty() ?
                playerNamesFilterStr :
                (playerNamesFilterStr.isEmpty() ? playerStatFiltersStr : String.join(" and ", playerStatFiltersStr, playerNamesFilterStr));
    }

    public String createMlDatasetViewQuery(String datasetSchemaName,
                                           String datasetTableName,
                                           String playersSchemaName,
                                           List<PlayerStatFilter> playerStatFilters,
                                           EnumSet<Action> allowedActions,
                                           List<String> playerNames,
                                           boolean profitableHandsOnly) {

        String playerNamesFilter = createPlayerNamesFilter(playerStatFilters, playerNames);
        String actionFilters = " HERO_ACTION in " + allowedActions.stream()
                .map(Enum::name)
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(", ", "[", "]"));

        return String.format("""                        
                        select distinct * except ( OPP_TYPES, board_size, competition_type ) from %s.%s
                        %s
                        where %s
                        """,
                datasetSchemaName,
                datasetTableName,
                playerNamesFilter.isEmpty() ? "" : "inner join " + playersSchemaName + ".players on " + datasetTableName + ".HERO_NAME == players.player_name",
                actionFilters + (playerNamesFilter.isEmpty() ? "" : " and " + playerNamesFilter) + (profitableHandsOnly ? " and (PROFIT_BB > 0 or (HERO_ACTION == 'F' or HERO_ACTION == 'C')) " : "")
        );


    }

    public WeightedRandomAmount weightedRandomAmount(String datasetsSchemaName,
                                                     String playersSchemaName,
                                                     String datasetTableName,
                                                     List<PlayerStatFilter> playerStatFilters,
                                                     List<String> playerNames, Feature<?, ?> feature,
                                                     double minWeightThreshold) {
        String playerFilterTotal = createPlayerNamesFilter(playerStatFilters, playerNames);
        String q = String.format("""                
                        SELECT round(round(%s / 0.1, 0) * 0.1, 1) am,
                        count() cnt
                        FROM %s.%s
                        %s
                        %s
                        group by am
                        """, feature.name().name(),
                datasetsSchemaName,
                datasetTableName,
                playerFilterTotal.isEmpty() ? "" : "inner join " +
                        playersSchemaName + ".players on " + datasetTableName + ".HERO_NAME == players.player_name where " +
                        playerFilterTotal,
                playerFilterTotal.isEmpty() ? " where HERO_ACTION == 'R'" : " and HERO_ACTION == 'R'");
        List<RawAmount> raws = clickhouseJdbcTemplate.queryForList(q)
                .stream().map(d -> {
                    double amount = (double) d.get("am");
                    int count = (int) ((long) d.get("cnt"));
                    return new RawAmount(amount, count);
                }).toList();
        int totalCountSum = raws.stream().mapToInt(RawAmount::count).sum();
        List<WeightedAmount> weightedAmounts = raws.stream().map(c -> {
                    double heroAmountBb = c.amount();
                    double weight = (double) c.count() / (double) totalCountSum;
                    return new WeightedAmount(weight, heroAmountBb);
                })
                .filter(weightedAmount -> weightedAmount.weight() > minWeightThreshold)
                .toList();
        double avg = weightedAmounts.stream().mapToDouble(wa -> wa.amount() * wa.weight()).sum();
        return new WeightedRandomAmount(weightedAmounts);
    }

    private record CardCellRawAmount(CardCell cardCell, double amount, int count) {
    }

    public List<CardCellWeightedRandomAmount> cardCellWeightedRandomAmounts(String datasetsSchemaName,
                                                                            String playersSchemaName,
                                                                            String datasetTableName,
                                                                            List<PlayerStatFilter> playerStatFilters,
                                                                            List<String> playerNames,
                                                                            Feature<?, ?> feature,
                                                                            double minWeightThreshold) {
        String playerFilterTotal = createPlayerNamesFilter(playerStatFilters, playerNames);
        String q = String.format("""                
                        SELECT POCKET_CARDS_NAME pcn,
                        round(round(%s / 0.1, 0) * 0.1, 1) am,
                        count() cnt
                        FROM %s.%s
                        %s
                        %s
                        group by pcn, am
                        """, feature.name().name(),
                datasetsSchemaName,
                datasetTableName,
                playerFilterTotal.isEmpty() ? "" : "inner join " +
                        playersSchemaName + ".players on " + datasetTableName + ".HERO_NAME == players.player_name where " +
                        playerFilterTotal,
                playerFilterTotal.isEmpty() ? " where HERO_ACTION == 'R'" : " and HERO_ACTION == 'R'");
        List<CardCellRawAmount> raws = clickhouseJdbcTemplate.queryForList(q)
                .stream().map(d -> {
                    CardCell cardCell = CardCell.from((String) d.get("pcn"));
                    double amount = (double) d.get("am");
                    int count = (int) ((long) d.get("cnt"));
                    return new CardCellRawAmount(cardCell, amount, count);
                }).toList();
        Map<CardCell, List<CardCellRawAmount>> map = raws.stream().collect(Collectors.groupingBy(CardCellRawAmount::cardCell));
        return map.entrySet().stream().map(e -> {
            CardCell cardCell = e.getKey();
            List<CardCellRawAmount> cardCellRawAmounts = e.getValue();
            int totalCountSum = cardCellRawAmounts.stream().mapToInt(CardCellRawAmount::count).sum();
            List<WeightedAmount> weightedAmounts = cardCellRawAmounts.stream().map(c -> {
                        double heroAmountBb = c.amount;
                        double weight = (double) c.count() / (double) totalCountSum;
                        return new WeightedAmount(weight, heroAmountBb);
                    })
                    .filter(weightedAmount -> weightedAmount.weight() > minWeightThreshold)
                    .toList();
            double avg = weightedAmounts.stream().mapToDouble(wa -> wa.amount() * wa.weight()).sum();
            WeightedRandomAmount wra = new WeightedRandomAmount(weightedAmounts);
            return new CardCellWeightedRandomAmount(cardCell, wra);
        }).toList();
    }
}
