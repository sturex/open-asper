package dev.asper.spark;

import dev.asper.poker.engine.Branch;

public record WeightedDecisionBranch(Branch branch, double weight) {
}
