package dev.asper.app.graphql;

import dev.asper.poker.card.Card;
import dev.asper.poker.card.Cards;
import dev.asper.poker.engine.PokerHand;
import dev.asper.poker.enums.CompetitionType;
import dev.asper.poker.enums.PreflopPosition;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public record PokerHandInput(
        String handId,
        String handDate,
        int sbAmount,
        int bbAmount,
        List<PlayerInitialStateInput> playerInitialStateInputs,
        List<MoveInput> pfMoveInputs,
        List<MoveInput> flMoveInputs,
        List<MoveInput> tnMoveInputs,
        List<MoveInput> rvMoveInputs,
        String flCards,
        String tnCard,
        String rvCard,
        CompetitionType competitionType) {
    private static final EnumMap<PreflopPosition, Integer> empty = new EnumMap<>(PreflopPosition.class);

    public PokerHand toPokerHand() {
        return new PokerHand(handId,
                handDate,
                sbAmount,
                bbAmount,
                playerInitialStateInputs.stream()
                        .collect(Collectors.toMap(PlayerInitialStateInput::preflopPosition,
                                PlayerInitialStateInput::toPlayerInitialState,
                                (o, o2) -> o,
                                () -> new EnumMap<>(PreflopPosition.class))),
                pfMoveInputs.stream().map(MoveInput::toMove).toList(),
                flMoveInputs == null ? Collections.emptyList() : flMoveInputs.stream().map(MoveInput::toMove).toList(),
                tnMoveInputs == null ? Collections.emptyList() : tnMoveInputs.stream().map(MoveInput::toMove).toList(),
                rvMoveInputs == null ? Collections.emptyList() : rvMoveInputs.stream().map(MoveInput::toMove).toList(),
                flCards == null ? null : Cards.from(flCards),
                tnCard == null ? null : Card.from(tnCard),
                rvCard == null ? null : Card.from(rvCard),
                empty,
                competitionType);
    }

}
