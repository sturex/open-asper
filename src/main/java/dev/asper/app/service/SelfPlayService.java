package dev.asper.app.service;

import dev.asper.app.graphql.PokerGamesDatasetInput;
import dev.asper.app.graphql.SelfPlayPropertiesInput;
import dev.asper.app.graphql.SolutionAliasInput;
import dev.asper.poker.engine.PokerGame;

import java.util.List;
import java.util.Set;

public interface SelfPlayService {
    List<PokerGame> play(Set<SolutionAliasInput> solutionAliasInputs, SelfPlayPropertiesInput selfPlayPropertiesInput);

}
