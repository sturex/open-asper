package dev.asper.app.service;

import dev.asper.poker.ai.AdvisorInfo;
import dev.asper.app.entity.SolutionInfo;
import dev.asper.clickhouse.OpponentType;

import java.util.*;

public interface SolutionService {
    double MIN_ACTION_VALUE_THRESHOLD = 0.01;

    Optional<SolutionInfo> findByName(String name);

    Set<SolutionInfo> findByNameIn(Collection<String> names);

    SolutionInfo create(List<AdvisorInfo> advisorInfos, String solutionName, String description);

    SolutionInfo createChartSolution(String datasetSchemaName, String playersSchemaName, OpponentType opponentType, List<String> playerNames, String solutionName);

    List<AdvisorInfo> recoverAdvisorInfos(SolutionInfo solutionInfo);

    SolutionInfo copySolution(String solutionName, String newSolutionName, String description);

    SolutionInfo update(List<AdvisorInfo> advisorInfos, String solutionName, String description);

    List<String> findAllNames(String mask);

    void deleteSolution(String name);

    void fixSolutions(List<String> solutionNames, String donorSolutionName);

    SolutionInfo pfDonorSolution(String solutionName, String donorSolutionName);
}
