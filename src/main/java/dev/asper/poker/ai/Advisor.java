package dev.asper.poker.ai;

import dev.asper.advice.ActionTweak;
import dev.asper.advice.Advice;
import dev.asper.advice.AdviceCorrector;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.SpecialSpotTweakCollection;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.enums.Action;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Advisor extends ActionAdvisor, AmountAdvisor {
    default List<AdvisedGame> advices(Spot spot, List<PokerGame> pokerGames, SpecialSpotTweakCollection specialSpotTweakCollection) {
        List<ActionMapGame> actionMapGames = adviceActionMap(spot, pokerGames);
        ActionTweak actionTweak = getActionTweak();
        Map<Action, List<ActionMapGame>> map = actionMapGames.parallelStream()
                .collect(Collectors.groupingBy(actionMapGame -> {
                    ActionMapGame tweakedBySpecialSpotTweak = specialSpotTweakCollection.apply(actionMapGame);
                    return actionTweak.apply(tweakedBySpecialSpotTweak.actionMap());
                }, Collectors.toList()));
        List<AmountGame> amountGames = adviceAmountGames(spot, pokerGames);
        Stream<AdvisedGame> fStream = map.get(Action.F).stream().map(a -> new AdvisedGame(a.pokerGame(), Advice.fAdvice()));
        Stream<AdvisedGame> cStream = map.get(Action.C).stream().map(a -> {
            int callAmount = a.pokerGame().computeCallAmount(a.pokerGame().getNextPlayerOrThrow().getPreflopPosition());
            return new AdvisedGame(a.pokerGame(), Advice.cAdvice(callAmount));
        });
        Stream<AdvisedGame> aStream = map.get(Action.A).stream().map(a -> {
            int stackStub = a.pokerGame().getNextPlayerOrThrow().getStackStub();
            return new AdvisedGame(a.pokerGame(), Advice.aAdvice(stackStub));
        });
        Stream<AdvisedGame> rStream = amountGames.stream().map(a -> new AdvisedGame(a.pokerGame(), Advice.rAdvice(a.amount())));
        return Stream.of(fStream, cStream, aStream, rStream).flatMap(Function.identity()).toList();
    }

    default Advice advice(Spot spot, PokerGame pokerGame, SpecialSpotTweakCollection specialSpotTweakCollection) {
        return advices(spot, List.of(pokerGame), specialSpotTweakCollection).get(0).advice();
    }
}
