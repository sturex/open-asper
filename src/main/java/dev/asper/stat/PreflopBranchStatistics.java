package dev.asper.stat;

import dev.asper.poker.card.PreflopRange;

import java.io.Serializable;

public record PreflopBranchStatistics(double branchProbability, PreflopRange preflopRange) implements Serializable {
    public PreflopBranchStatistics multiplyBySourceBranchProbability(double sourceBranchProbability) {
        return new PreflopBranchStatistics(branchProbability * sourceBranchProbability, preflopRange);
    }
}
