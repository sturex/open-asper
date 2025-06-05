package dev.asper.clickhouse;

import dev.asper.poker.enums.BoardSize;
import dev.asper.poker.enums.CompetitionType;

public record PlayerStat(
        CompetitionType competitionType,
        String playerName,
        BoardSize boardSize,
        long totalHandsCnt,
        long openHandsCnt,
        double avgProfit,
        double vpip,
        double pfr,
        double limp,
        double pfrr,
        double wwsf,
        double wtsd,
        double wssd
) {
}
