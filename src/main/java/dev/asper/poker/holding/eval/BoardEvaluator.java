package dev.asper.poker.holding.eval;

import dev.asper.poker.card.Card;
import dev.asper.poker.card.Cards;
import dev.asper.poker.card.Rank;
import dev.asper.poker.card.Suit;
import dev.asper.poker.holding.BoardFlush;
import dev.asper.poker.holding.BoardPair;
import dev.asper.poker.holding.BoardStatus;
import dev.asper.poker.holding.BoardStraight;

import java.util.*;
import java.util.stream.Collectors;

public enum BoardEvaluator {
    ;
    public final static EnumMap<Rank, EnumSet<Rank>> straights = new EnumMap<>(Rank.class);

    static {
        straights.put(Rank.ACE, EnumSet.of(Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN));
        straights.put(Rank.KING, EnumSet.of(Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN, Rank.NINE));
        straights.put(Rank.QUEEN, EnumSet.of(Rank.QUEEN, Rank.JACK, Rank.TEN, Rank.NINE, Rank.EIGHT));
        straights.put(Rank.JACK, EnumSet.of(Rank.JACK, Rank.TEN, Rank.NINE, Rank.EIGHT, Rank.SEVEN));
        straights.put(Rank.TEN, EnumSet.of(Rank.TEN, Rank.NINE, Rank.EIGHT, Rank.SEVEN, Rank.SIX));
        straights.put(Rank.NINE, EnumSet.of(Rank.NINE, Rank.EIGHT, Rank.SEVEN, Rank.SIX, Rank.FIVE));
        straights.put(Rank.EIGHT, EnumSet.of(Rank.EIGHT, Rank.SEVEN, Rank.SIX, Rank.FIVE, Rank.FOUR));
        straights.put(Rank.SEVEN, EnumSet.of(Rank.SEVEN, Rank.SIX, Rank.FIVE, Rank.FOUR, Rank.THREE));
        straights.put(Rank.SIX, EnumSet.of(Rank.SIX, Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO));
        straights.put(Rank.FIVE, EnumSet.of(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO, Rank.ACE));
    }

    public static BoardStatus eval(Cards cards) {
        return eval(cards.cards());
    }

    public static BoardStatus eval(EnumSet<Card> cards) {
        int boardCardsSize = cards.size();
        EnumMap<Suit, Long> countBySuit = cards.stream()
                .collect(Collectors.groupingBy(Card::suit, () -> new EnumMap<>(Suit.class), Collectors.counting()));
        EnumMap<Rank, Long> countByRank = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, () -> new EnumMap<>(Rank.class), Collectors.counting()));
        Map<Long, List<Rank>> rankByCount = countByRank.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
        EnumSet<Rank> ranks = cards.stream().map(Card::rank).collect(Collectors.toCollection(() -> EnumSet.noneOf(Rank.class)));
        BoardPair boardPair = evalBoardPair(countByRank, rankByCount, ranks);
        BoardFlush boardFlush = evalBoardFlush(cards, countBySuit, boardCardsSize);
        BoardStraight boardStraight = evalBoardStraight(ranks);
        return new BoardStatus(boardPair, boardStraight, boardFlush);
    }

    private static BoardStraight evalBoardStraight(EnumSet<Rank> ranks) {
        int maxIntersection = straights.values().stream()
                .map(s -> (int) ranks.stream().filter(s::contains).count())
                .max(Comparator.naturalOrder()).orElseThrow();
        return switch (maxIntersection) {
            case 1, 2 -> BoardStraight.NONE;
            case 3 -> BoardStraight.CONN_3;
            case 4 -> BoardStraight.CONN_4;
            case 5 -> BoardStraight.READY;
            default -> throw new IllegalStateException("Unexpected value: " + maxIntersection);
        };
    }

    private static BoardFlush evalBoardFlush(EnumSet<Card> cards, EnumMap<Suit, Long> countBySuit, int boardCardsSize) {
        Collection<Long> values = countBySuit.values();
        int suitCount = countBySuit.size();
        return switch (boardCardsSize) {
            case 3 -> switch (suitCount) {
                case 1 -> BoardFlush.SUITED;
                case 2 -> BoardFlush.SUITED_21;
                case 3 -> BoardFlush.RAINBOW;
                default -> throw new IllegalStateException("Unexpected value: " + suitCount);
            };
            case 4 -> switch (suitCount) {
                case 1 -> BoardFlush.SUITED;
                case 2 -> values.contains(3L) ? BoardFlush.SUITED_31 : BoardFlush.SUITED_22;
                case 3 -> BoardFlush.SUITED_211;
                case 4 -> BoardFlush.RAINBOW;
                default -> throw new IllegalStateException("Unexpected value: " + suitCount);
            };
            case 5 -> switch (suitCount) {
                case 1 -> BoardFlush.SUITED;
                case 2 -> values.contains(3L) ? BoardFlush.SUITED_32 : BoardFlush.SUITED_41;
                case 3 -> values.contains(3L) ? BoardFlush.SUITED_311 : BoardFlush.SUITED_221;
                case 4 -> BoardFlush.SUITED_2111;
                default -> throw new IllegalStateException("Unexpected value: " + suitCount);
            };
            default -> throw new IllegalStateException("Unexpected value: " + boardCardsSize);
        };
    }

    private static BoardPair evalBoardPair(EnumMap<Rank, Long> countByRank, Map<Long, List<Rank>> rankByCount, EnumSet<Rank> ranks) {
        Collection<Long> counts = countByRank.values();
        if (counts.contains(3L)) {
            if (counts.contains(2L)) {
                return BoardPair.FULL_HOUSE;
            } else {
                return BoardPair.TRIPS;
            }
        } else {
            long pairsCount = counts.stream().filter(c -> c == 2).count();
            if (pairsCount == 1) {
                return rankByCount.get(1L).contains(Rank.ACE) || rankByCount.get(1L).contains(Rank.KING) ?
                        BoardPair.PAIR_AK_BOARD : BoardPair.PAIR;
            } else if (pairsCount == 2) {
                return rankByCount.get(2L).contains(Rank.ACE) || rankByCount.get(2L).contains(Rank.KING) ?
                        BoardPair.TWO_PAIRS_AK_BOARD : BoardPair.TWO_PAIRS;
            } else if (counts.contains(4L)) {
                return BoardPair.QUAD;
            } else {
                return ranks.contains(Rank.ACE) ? BoardPair.A_HIGH :
                        (ranks.contains(Rank.KING) ? BoardPair.K_HIGH : BoardPair.NONE);
            }
        }
    }
}
