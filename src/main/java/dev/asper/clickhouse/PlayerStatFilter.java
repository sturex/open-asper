package dev.asper.clickhouse;


import dev.asper.poker.enums.BoardSize;
import dev.asper.poker.enums.CompetitionType;

public record PlayerStatFilter(
        CompetitionType competitionType,
        BoardSize boardSize,
        int handCountMin,
        int handCountMax,
        double vpipMin,
        double vpipMax,
        double pfrMin,
        double pfrMax,
        double wwsfMin,
        double wwsfMax,
        double wtsdMin,
        double wtsdMax,
        double wssdMin,
        double wssdMax,
        double vpipPfrDiffMin,
        double vpipPfrDiffMax,
        double limpMin,
        double limpMax,
        double pfrrMin,
        double pfrrMax,
        double avgProfitBb100Min,
        double avgProfitBb100Max
) {
}
