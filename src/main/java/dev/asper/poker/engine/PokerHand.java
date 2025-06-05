package dev.asper.poker.engine;

import dev.asper.poker.card.Card;
import dev.asper.poker.card.Cards;
import dev.asper.poker.enums.CompetitionType;
import dev.asper.poker.enums.PokerGameEventType;
import dev.asper.poker.enums.PreflopPosition;

import java.util.EnumMap;
import java.util.List;

public record PokerHand(
        String handId,
        String handDate,
        int sbAmount,
        int bbAmount,
        EnumMap<PreflopPosition, PlayerInitialState> playerInitialStates,
        List<Move> pfMoves,
        List<Move> flMoves,
        List<Move> tnMoves,
        List<Move> rvMoves,
        Cards flCards,
        Card tnCard,
        Card rvCard,
        EnumMap<PreflopPosition, Integer> profitByPosition,
        CompetitionType competitionType,
        String comment) {

    public PokerHand(String handId,
                     String handDate,
                     int sbAmount,
                     int bbAmount,
                     EnumMap<PreflopPosition, PlayerInitialState> playerInitialStates,
                     List<Move> pfMoves,
                     List<Move> flMoves,
                     List<Move> tnMoves,
                     List<Move> rvMoves,
                     Cards flCards,
                     Card tnCard,
                     Card rvCard,
                     EnumMap<PreflopPosition, Integer> profitByPosition,
                     CompetitionType competitionType) {
        this(handId,
                handDate,
                sbAmount,
                bbAmount,
                playerInitialStates,
                pfMoves,
                flMoves,
                tnMoves,
                rvMoves,
                flCards,
                tnCard,
                rvCard,
                profitByPosition,
                competitionType,
                "");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HAND ID: ").append(handId).append(" ")
                .append("HAND DATE: ").append(handDate).append(" ")
                .append("BB AMOUNT: ").append(bbAmount)
                .append("\n");
        sb.append("\t\t\tINITIAL STATES: ");
        if (playerInitialStates != null) {
            playerInitialStates.forEach((preflopPosition, playerInitialState) -> {
                sb.append(preflopPosition.toString()).append(":")
                        .append(playerInitialState.playerName()).append(":")
                        .append(playerInitialState.pocketCards()).append(":")
                        .append(playerInitialState.initialStack()).append("\n");
            });
        }
        sb.append("\n")
                .append("\t\t\tBOARD CARDS: ")
                .append(flCards == null ? "" : "[" + flCards + "]")
                .append(tnCard == null ? "" : "[" + tnCard + "]")
                .append(rvCard == null ? "" : "[" + rvCard + "]")
                .append("\n");
        if (pfMoves != null && !pfMoves.isEmpty()) {
            sb.append("\t\t\tPF MOVES: ");
            pfMoves.forEach(move -> sb.append(move.toString()).append(" "));
            sb.append("\n");
        }
        if (flMoves != null && !flMoves.isEmpty()) {
            sb.append("\t\t\tFL MOVES: ");
            flMoves.forEach(move -> sb.append(move.toString()).append(" "));
            sb.append("\n");
        }
        if (tnMoves != null && !tnMoves.isEmpty()) {
            sb.append("\t\t\tTN MOVES: ");
            tnMoves.forEach(move -> sb.append(move.toString()).append(" "));
            sb.append("\n");
        }
        if (rvMoves != null && !rvMoves.isEmpty()) {
            sb.append("\t\t\tRV MOVES: ");
            rvMoves.forEach(move -> sb.append(move.toString()).append(" "));
            sb.append("\n");
        }
        if (!comment.isEmpty()) {
            sb.append("\t\t\t").append(comment);
            sb.append("\n");
        }
        return sb.toString();
    }


    //TODO refactor. Dirty solution
    public static PokerHand createForMove(PokerGameEventType pokerGameEventType, List<Move> moves) {
        return switch (pokerGameEventType) {
            case PF_MOVE -> new PokerHand(null, null, 0, 0, null, moves, null, null, null, null, null, null, null, null, null);
            case FL_MOVE -> new PokerHand(null, null, 0, 0, null, null, moves, null, null, null, null, null, null, null, null);
            case TN_MOVE -> new PokerHand(null, null, 0, 0, null, null, null, moves, null, null, null, null, null, null, null);
            case RV_MOVE -> new PokerHand(null, null, 0, 0, null, null, null, null, moves, null, null, null, null, null, null);
            default -> throw new RuntimeException("Unexpected pokerGameEventType=" + pokerGameEventType);
        };
    }


    //TODO refactor. Dirty solution
    public static PokerHand createForCards(PokerGameEventType pokerGameEventType, Cards flCards, Card tnCard, Card rvCard) {
        return switch (pokerGameEventType) {
            case FL_CARDS -> new PokerHand(null, null, 0, 0, null, null, null, null, null, flCards, null, null, null, null, null);
            case TN_CARD -> new PokerHand(null, null, 0, 0, null, null, null, null, null, flCards, tnCard, null, null, null, null);
            case RV_CARD -> new PokerHand(null, null, 0, 0, null, null, null, null, null, flCards, tnCard, rvCard, null, null, null);
            default -> throw new RuntimeException("Unexpected pokerGameEventType=" + pokerGameEventType);
        };
    }

    public boolean containsOpenCards() {
        return playerInitialStates.values().stream().anyMatch(PlayerInitialState::isPocketCardsOpen);
    }
}
