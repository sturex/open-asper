package dev.asper.clickhouse;


import dev.asper.poker.enums.BoardSize;
import dev.asper.poker.enums.CompetitionType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public enum OpponentType implements Predicate<PlayerStat> {
    OVERALL(EnumSet.of(CompetitionType.CASH, CompetitionType.MTT)) {
        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            //It should be taken all the data by this filter
            return Collections.emptyList();
        }
    },
    OVERALL_CASH(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.HEADS_UP, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_3, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_4, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_5, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_7, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_8, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_9, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0)
        );

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    UNKNOWN_CASH(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.HEADS_UP, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_3, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_4, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_5, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_7, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_8, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_9, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0),
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_10, 0, HAND_COUNT_MIN, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0)
        );

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    OVERALL_CASH_BS6(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(
                new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, 0, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 1.0, 0.0, 1.0, -1000000.0, 1000000.0)
        );

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_LIMPER_BS_6_VPIP_0_30(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.0, 0.3, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.05, 1.0, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_LIMPER_BS_6_VPIP_30PLUS(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.3, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.05, 1.0, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_VPIP_40PLUS(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.40, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_VPIP_30_40(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.3, 0.40, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_VPIP_25_30(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.25, 0.3, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_VPIP_20_25(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.20, 0.25, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_VPIP_0_20(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.0, 0.20, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_VPIP_0_20_WWSF_45PLUS(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.0, 0.20, 0.0, 1.0, 0.45, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_VPIP_20_30_WWSF_45PLUS(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.20, 0.30, 0.0, 1.0, 0.45, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_VPIP_30PLUS_WWSF_45PLUS(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.30, 1.0, 0.0, 1.0, 0.45, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_WWSF_45PLUS(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.45, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_WWSF_47PLUS(EnumSet.of(CompetitionType.CASH)) {

        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.47, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    },
    CASH_REG_BS_6_WWSF_50PLUS(EnumSet.of(CompetitionType.CASH)) {
        private final List<PlayerStatFilter> playerStatFilters = List.of(new PlayerStatFilter(CompetitionType.CASH, BoardSize.SIZE_6, HAND_COUNT_MIN, 1000000000, 0.0, 1.0, 0.0, 1.0, 0.50, 1.0, 0.0, 1.0, 0.0, 1.0, 0, 1.0, 0.0, 0.02, 0.0, 1.0, -1000000.0, 1000000.0));

        @Override
        public List<PlayerStatFilter> playerStatFilters() {
            return playerStatFilters;
        }
    };

    public static final int HAND_COUNT_MIN = 500;
    private final EnumSet<CompetitionType> competitionTypes;

    OpponentType(EnumSet<CompetitionType> competitionTypes) {
        this.competitionTypes = competitionTypes;
    }

    public abstract List<PlayerStatFilter> playerStatFilters();

    @Override
    public boolean test(PlayerStat playerStat) {
        return playerStatFilters().stream()
                .anyMatch(playerStatFilter -> playerStat.avgProfit() >= playerStatFilter.avgProfitBb100Min() && playerStat.avgProfit() <= playerStatFilter.avgProfitBb100Max() &&
                        playerStat.vpip() >= playerStatFilter.vpipMin() && playerStat.vpip() <= playerStatFilter.vpipMax() &&
                        playerStat.pfr() >= playerStatFilter.pfrMin() && playerStat.pfr() <= playerStatFilter.pfrMax() &&
                        playerStat.limp() >= playerStatFilter.limpMin() && playerStat.limp() <= playerStatFilter.limpMax() &&
                        playerStat.pfrr() >= playerStatFilter.pfrrMin() && playerStat.pfrr() <= playerStatFilter.pfrrMax() &&
                        playerStat.wwsf() >= playerStatFilter.wwsfMin() && playerStat.wwsf() <= playerStatFilter.wwsfMax() &&
                        playerStat.wtsd() >= playerStatFilter.wtsdMin() && playerStat.wtsd() <= playerStatFilter.wtsdMax() &&
                        playerStat.wssd() >= playerStatFilter.wssdMin() && playerStat.wssd() <= playerStatFilter.wssdMax() &&
                        playerStat.totalHandsCnt() >= playerStatFilter.handCountMin() && playerStat.totalHandsCnt() <= playerStatFilter.handCountMax() &&
                        playerStatFilter.boardSize() == playerStat.boardSize() &&
                        playerStatFilter.competitionType() == playerStat.competitionType())
                ;
    }


    public EnumSet<CompetitionType> competitionTypes() {
        return competitionTypes;
    }
}
