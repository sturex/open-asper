package dev.asper.app.graphql;

import dev.asper.poker.ai.AdvisorInfo;

import java.util.List;

public record SolutionInfoOutput(List<AdvisorInfo> advisorInfos) {
}
