package dev.asper.app.graphql;

import dev.asper.clickhouse.OpponentType;

public record SolutionAliasInput(String solutionNameAdvice, String alias, OpponentType opponentType, String specialSpotTweakCollectionName, boolean isMandatory) {
}
