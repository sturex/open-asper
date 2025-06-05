package dev.asper.poker.ai;

import dev.asper.advice.AmountTweak;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.spot.Spot;
import dev.asper.poker.engine.spot.Spots;

import java.util.List;

public interface AmountAdvisor {
    default List<AmountGame> adviceAmountGames(Spot spot, List<PokerGame> pokerGames) {
        return pokerGames.stream()
                .map(pokerGame -> new AmountGame(pokerGame, adviceAmount(spot, pokerGame)))
                .toList();
    }

    default int adviceAmount(Spot spot, PokerGame pokerGame) {
        return adviceAmountGames(spot, List.of(pokerGame)).get(0).amount();
    }

    default AmountTweak getAmountTweak() {
        return AmountTweak.identity;
    }
}
