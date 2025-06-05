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

public class MlAdvisor implements Advisor {
    private final MlActionAdvisor mlActionAdvisor;
    private final MlAmountAdvisor mlAmountAdvisor;

    public MlAdvisor(MlActionAdvisor mlActionAdvisor, MlAmountAdvisor mlAmountAdvisor) {
        this.mlActionAdvisor = mlActionAdvisor;
        this.mlAmountAdvisor = mlAmountAdvisor;
    }

    @Override
    public List<AdvisedGame> advices(Spot spot, List<PokerGame> pokerGames, SpecialSpotTweakCollection specialSpotTweakCollection) {
        List<ActionMapGame> actionMapGames = adviceActionMap(spot, pokerGames);
        ActionTweak actionTweak = mlActionAdvisor.getActionTweak();
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
        return mlActionAdvisor.adviceActionMap(spot, pokerGames);
    }

    @Override
    public List<AmountGame> adviceAmountGames(Spot spot, List<PokerGame> pokerGames) {
        return mlAmountAdvisor.adviceAmountGames(spot, pokerGames);
    }
}
