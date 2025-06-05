package dev.asper.poker.ai;

import dev.asper.advice.ActionTweak;
import dev.asper.advice.Advice;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.SpecialSpotTweakCollection;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.enums.Action;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleAdvisor implements Advisor {
    private final ActionAdvisor actionAdvisor;
    private final AmountAdvisor amountAdvisor;

    public SimpleAdvisor(ActionAdvisor actionAdvisor, AmountAdvisor amountAdvisor) {
        this.actionAdvisor = actionAdvisor;
        this.amountAdvisor = amountAdvisor;
    }

    @Override
    public List<AdvisedGame> advices(Spot spot, List<PokerGame> pokerGames, SpecialSpotTweakCollection specialSpotTweakCollection) {
        List<ActionMapGame> actionMapGames = adviceActionMap(spot, pokerGames);
        ActionTweak actionTweak = actionAdvisor.getActionTweak();
        Map<Action, List<ActionMapGame>> map = actionMapGames.parallelStream()
                .collect(Collectors.groupingBy(actionMapGame -> {
                    ActionMapGame tweakedBySpecialSpotTweak = specialSpotTweakCollection.apply(actionMapGame);
                    return actionTweak.apply(tweakedBySpecialSpotTweak.actionMap());
                }, Collectors.toList()));
        Stream<AdvisedGame> fStream = Optional.ofNullable(map.get(Action.F)).map(Collection::stream)
                .map(s -> s.map(a -> new AdvisedGame(a.pokerGame(), Advice.fAdvice())))
                .orElse(Stream.empty());
        Stream<AdvisedGame> cStream = Optional.ofNullable(map.get(Action.C)).map(Collection::stream)
                .map(s -> s.map(a -> {
                    int callAmount = a.pokerGame().computeCallAmount(a.pokerGame().getNextPlayerOrThrow().getPreflopPosition());
                    return new AdvisedGame(a.pokerGame(), Advice.cAdvice(callAmount));
                }))
                .orElse(Stream.empty());
        Stream<AdvisedGame> aStream = Optional.ofNullable(map.get(Action.A)).map(Collection::stream)
                .map(s -> s.map(a -> {
                    int stackStub = a.pokerGame().getNextPlayerOrThrow().getStackStub();
                    return new AdvisedGame(a.pokerGame(), Advice.aAdvice(stackStub));
                }))
                .orElse(Stream.empty());
        Stream<AdvisedGame> rStream = Optional.ofNullable(map.get(Action.R)).map(Collection::stream)
                .map(s -> adviceAmountGames(spot, s.map(ActionMapGame::pokerGame).toList()).stream()
                        .map(a -> new AdvisedGame(a.pokerGame(), Advice.rAdvice(a.amount()))))
                .orElse(Stream.empty());
        return Stream.of(fStream, cStream, aStream, rStream).flatMap(Function.identity()).toList();
    }

    @Override
    public List<ActionMapGame> adviceActionMap(Spot spot, List<PokerGame> pokerGames) {
        return actionAdvisor.adviceActionMap(spot, pokerGames);
    }

    @Override
    public List<AmountGame> adviceAmountGames(Spot spot, List<PokerGame> pokerGames) {
        return amountAdvisor.adviceAmountGames(spot, pokerGames);
    }
}
