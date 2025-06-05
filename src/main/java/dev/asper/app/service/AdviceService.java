package dev.asper.app.service;

import dev.asper.advice.ActionTweak;
import dev.asper.advice.Advice;
import dev.asper.advice.AmountTweak;
import dev.asper.app.component.PipelineModelRecoverException;
import dev.asper.poker.ai.*;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerHand;
import dev.asper.poker.engine.SpecialSpotTweakCollection;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.enums.CounterStrategyType;

import java.util.List;
import java.util.stream.Collectors;

public interface AdviceService {
    void createSolutionPool(List<String> solutionNames);

    Advisor recoverAdvisor(Spot spot, AdvisorInfo advisorInfo);

    AmountAdvisor recoverAmountAdvisor(Spot spot, AmountAdvisorType amountAdvisorType, AmountTweak amountTweak, String amountAdvisorName);

    ActionAdvisor recoverActionAdvisor(Spot spot, ActionAdvisorType actionAdvisorType, ActionTweak actionTweak, String actionAdvisorName);

    MlActionAdvisor recoverMlActionAdvisor(Spot spot, String name, ActionTweak actionTweak) throws PipelineModelRecoverException;

    MlAmountAdvisor recoverMlAmountAdvisor(Spot spot, String name, AmountTweak amountTweak) throws PipelineModelRecoverException;

    Advice advice(PokerHand pokerHand, String solutionName, SpecialSpotTweakCollection specialSpotTweakCollection);

    Advice advice(PokerGame pokerGame, String solutionName, SpecialSpotTweakCollection specialSpotTweakCollection);

    List<AdvisedGame> advices(List<PokerGame> pokerGames, String solutionName, String specialSpotCollectionName);

    Solution getSolution(String solutionName);

    Advice adviseCS(PokerGame pokerGame, CounterStrategyType counterStrategyType);

    Advice adviseCS(PokerHand pokerHand);
}
