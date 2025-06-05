package dev.asper.poker.holding.eval;

import dev.asper.metric.cactus.CactusCombo;
import dev.asper.metric.cactus.CactusSimpleEvaluator;
import dev.asper.poker.card.*;
import dev.asper.poker.holding.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HoldingEvaluator {
    ;

    private final static EnumMap<Rank, EnumSet<Rank>> straights = new EnumMap<>(Rank.class);

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


    public static Holding eval(Cards pocketCards, Cards boardCards) {
        EnumSet<Card> pc = pocketCards.cards();
        EnumSet<Card> bc = boardCards.cards();
        EnumMap<Rank, Long> countByRank = Stream.of(pc, bc).flatMap(Collection::stream)
                .map(Card::rank).collect(Collectors.groupingBy(rank -> rank, () -> new EnumMap<>(Rank.class), Collectors.counting()));
        return eval(pc, bc, countByRank);
    }

    public static Holding eval(EnumSet<Card> pocketCards, EnumSet<Card> boardCards) {
        EnumMap<Rank, Long> countByRank = Stream.of(pocketCards, boardCards).flatMap(Collection::stream)
                .map(Card::rank).collect(Collectors.groupingBy(rank -> rank, () -> new EnumMap<>(Rank.class), Collectors.counting()));
        return eval(pocketCards, boardCards, countByRank);
    }

    public static Holding eval(String cards) {
        String[] split = cards.split("(?<=\\G.{2})");
        return eval(Arrays.stream(split).map(Card::from).toList());
    }

    public static Holding eval(List<Card> cards) {
        EnumSet<Card> pc = EnumSet.copyOf(cards.subList(0, 2));
        EnumSet<Card> bc = EnumSet.copyOf(cards.subList(2, cards.size()));
        EnumMap<Rank, Long> countByRank = cards.stream().map(Card::rank)
                .collect(Collectors.groupingBy(rank -> rank, () -> new EnumMap<>(Rank.class), Collectors.counting()));
        return eval(pc, bc, countByRank);
    }

    private static Holding eval(EnumSet<Card> pc, EnumSet<Card> bc, EnumMap<Rank, Long> countByRank) {
        Card[] cards = pc.toArray(Card[]::new);
        CardCell cardCell = CardCell.from(cards[0], cards[1]);
        EnumMap<Suit, EnumSet<Card>> bySuit = Stream.of(pc, bc)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Card::suit, () -> new EnumMap<>(Suit.class), Collectors.toCollection(() -> EnumSet.noneOf(Card.class))));
        EnumMap<Suit, Long> bySuitCountBoard = bc.stream()
                .collect(Collectors.groupingBy(Card::suit, () -> new EnumMap<>(Suit.class), Collectors.counting()));
        long suitsLongestCount = bySuitCountBoard.values().stream().max(Comparator.naturalOrder()).orElseThrow();
        CactusCombo cactusCombo = CactusSimpleEvaluator.eval(pc, bc);
        EnumSet<Suit> pcSuits = pc.stream().map(Card::suit).collect(Collectors.toCollection(() -> EnumSet.noneOf(Suit.class)));
        EnumSet<Rank> pcRanks = pc.stream().map(Card::rank).collect(Collectors.toCollection(() -> EnumSet.noneOf(Rank.class)));
        EnumSet<Card> maxBySuit = bySuit.entrySet().stream()
                .filter(e -> pcSuits.contains(e.getKey()))
                .max(Comparator.comparingInt(value -> value.getValue().size()))
                .map(Map.Entry::getValue).orElseThrow();
        Map<Long, List<Rank>> rankByCount = countByRank.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
        long rankLongestCount = Collections.max(rankByCount.keySet());
        FlushStatus flushStatus = evalFlushStatus(maxBySuit, cardCell, pc);
        EnumSet<Rank> bcRanks = toRanks(bc);
        StraightStatus straightStatus = evalStraightStatus(cardCell, pcRanks, bcRanks);
        ComboStatus comboStatus = switch (cactusCombo.comboType()) {
            case STRAIGHT_FLUSH -> getStraightFlushComboStatus(cactusCombo, flushStatus);
            case FLUSH -> getFlushComboStatus(cactusCombo, flushStatus, rankLongestCount, cardCell);
            case STRAIGHT -> getStraightComboStatus(cactusCombo, straightStatus, suitsLongestCount, cardCell);
            case FOUR_OF_A_KIND -> eval4K(cactusCombo, pc, bc, pcRanks, cardCell, bcRanks);
            case FULL_HOUSE -> evalFullHouse(cactusCombo, pcRanks, bc, rankByCount, cardCell);
            case THREE_OF_A_KIND -> eval3K(cactusCombo, pcRanks, cardCell);
            case TWO_PAIR -> evalTwoPairs(bcRanks, cactusCombo, rankByCount, cardCell);
            case ONE_PAIR -> evalPair(flushStatus, straightStatus, cactusCombo, pcRanks, bc, cardCell, bcRanks);
            case HIGH_CARD -> evalHighCard(flushStatus, straightStatus, cactusCombo, pcRanks, bc, cardCell, bySuit, pc);
        };
        return new Holding(cardCell, comboStatus, flushStatus, straightStatus);
    }

    private static ComboStatus getStraightFlushComboStatus(CactusCombo cactusCombo, FlushStatus flushStatus) {
        Handicap handicap = flushStatus.handicap();
        return new ComboStatus(ComboExType.NUTS,
                new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                handicap,
                flushStatus.rank(),
                flushStatus.nuts());
    }

    private static ComboStatus evalPair(FlushStatus flushStatus,
                                        StraightStatus straightStatus,
                                        CactusCombo cactusCombo,
                                        EnumSet<Rank> pcRanks,
                                        Collection<Card> bc,
                                        CardCell cardCell,
                                        EnumSet<Rank> bcRanks) {
        Rank comboRank = cactusCombo.rank();
        Handicap handicap = pcRanks.contains(comboRank) ? (cardCell.connector() == Connector.EQUAL ? Handicap.TWO_CARDS : Handicap.ONE_CARD) : Handicap.NO_CARDS;
        return new ComboStatus(evalPairCombo(flushStatus, straightStatus, cardCell, comboRank, pcRanks, bc, bcRanks),
                new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                handicap,
                comboRank,
                Nuts.WEAK);
    }

    private static ComboExType evalPairCombo(FlushStatus flushStatus,
                                             StraightStatus straightStatus,
                                             CardCell cardCell,
                                             Rank comboRank,
                                             EnumSet<Rank> pcRanks,
                                             Collection<Card> bc,
                                             EnumSet<Rank> bcRanks) {
        Rank[] boardRanksSorted = bc.stream().map(Card::rank).sorted(Comparator.<Rank>naturalOrder().reversed()).toArray(Rank[]::new);
        Rank maxBoardRank = boardRanksSorted[0];
        if (pcRanks.contains(comboRank)) {
            if (maxBoardRank == comboRank) {
                if (comboRank.isGreaterOrEquals(Rank.KING) && (pcRanks.contains(Rank.ACE) && pcRanks.contains(Rank.KING))) {
                    return ComboExType.TOP_PAIR_AK;
                } else if ((comboRank == Rank.ACE && pcRanks.contains(Rank.ACE) && pcRanks.contains(Rank.JACK)) ||
                        (comboRank == Rank.ACE && pcRanks.contains(Rank.ACE) && pcRanks.contains(Rank.QUEEN)) ||
                        (comboRank == Rank.KING && pcRanks.contains(Rank.KING) && pcRanks.contains(Rank.QUEEN))) {
                    return ComboExType.TOP_PAIR_AQAJKQ;
                } else if (comboRank == Rank.ACE && pcRanks.contains(Rank.ACE)) {
                    return ComboExType.TOP_PAIR_AX;
                } else if ((comboRank == Rank.QUEEN && pcRanks.contains(Rank.QUEEN)) ||
                        ((comboRank == Rank.KING && pcRanks.contains(Rank.KING)))) {
                    return ComboExType.TOP_PAIR_KQ;
                } else {
                    return cardCell.topRank() == comboRank ? ComboExType.TOP_PAIR_WK : ComboExType.TOP_PAIR_GK;
                }
            } else if (boardRanksSorted[1] == comboRank) {
                return cardCell.topRank().isGreaterOrEquals(maxBoardRank) ? ComboExType.SECOND_PAIR_GK : ComboExType.SECOND_PAIR_WK;
            } else if ((boardRanksSorted[1].isGreater(comboRank) && boardRanksSorted[2].isLess(comboRank)) || boardRanksSorted[2] == comboRank) {
                return cardCell.topRank().isGreaterOrEquals(maxBoardRank) ? ComboExType.THIRD_PAIR_GK : ComboExType.THIRD_PAIR_WK;
            } else if (boardRanksSorted[2].isGreater(comboRank)) {
                return ComboExType.WEAK_PAIR;
            } else if (cardCell.connector() == Connector.EQUAL && comboRank.isGreater(maxBoardRank)) {
                return cardCell.topRank().isGreater(Rank.JACK) ? ComboExType.OVER_PAIR_AKQ : ComboExType.OVER_PAIR;
            } else if (cardCell.connector() == Connector.EQUAL && comboRank.isLess(maxBoardRank) && comboRank.isGreater(boardRanksSorted[1])) {
                return (bcRanks.contains(Rank.ACE) || bcRanks.contains(Rank.KING)) ? ComboExType.UNDER_PAIR_AK_BOARD : ComboExType.UNDER_PAIR;
            }
        } else if (flushStatus.flush() == Flush.DRAW && flushStatus.handicap() == Handicap.TWO_CARDS) {
            return ComboExType.AIR_PAIR_FD;
        } else if (straightStatus.straight() == Straight.DRAW && straightStatus.handicap() == Handicap.TWO_CARDS) {
            return ComboExType.AIR_PAIR_SD;
        } else if (cardCell.lowRank().isGreater(maxBoardRank)) {
            return ComboExType.AIR_PAIR_OC2;
        } else if (cardCell.topRank().isGreater(maxBoardRank)) {
            return ComboExType.AIR_PAIR_OC1;
        }
        return ComboExType.AIR_PAIR_TRASH;
    }

    private static ComboStatus evalHighCard(FlushStatus flushStatus, StraightStatus straightStatus, CactusCombo cactusCombo, EnumSet<Rank> pcRanks, Collection<Card> bc, CardCell cardCell, EnumMap<Suit, EnumSet<Card>> bySuit, Collection<Card> pc) {
        Rank comboRank = cactusCombo.rank();
        Rank highestBoardRank = bc.stream().map(Card::rank).max(Comparator.naturalOrder()).orElseThrow();
        Handicap handicap = pcRanks.contains(comboRank) ? Handicap.ONE_CARD : Handicap.NO_CARDS;
        return new ComboStatus(evalHighCardCombo(flushStatus, straightStatus, cardCell, pcRanks, highestBoardRank, bySuit, pc),
                new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                handicap,
                cardCell.topRank(),
                Nuts.WEAK);
    }

    private static ComboExType evalHighCardCombo(FlushStatus flushStatus, StraightStatus straightStatus, CardCell cardCell, EnumSet<Rank> pcRanks, Rank highestBoardRank, EnumMap<Suit, EnumSet<Card>> bySuit, Collection<Card> pc) {
        if (cardCell.topRank().isGreater(highestBoardRank) && cardCell.lowRank().isGreater(highestBoardRank)) {
            if (pcRanks.contains(Rank.ACE)) {
                Suit flushSuit = bySuit.entrySet().stream()
                        .max(Comparator.comparingInt(e -> e.getValue().size())).map(Map.Entry::getKey).orElseThrow();
                boolean pocketCardsContainAceSuited = pc.stream().anyMatch(card -> card.suit() == flushSuit && card.rank() == Rank.ACE);
                if (flushStatus.flush() == Flush.DRAW) {
                    if (flushStatus.handicap() == Handicap.TWO_CARDS || (flushStatus.handicap() == Handicap.ONE_CARD && pocketCardsContainAceSuited)) {
                        return ComboExType.OC_2_AX_FD;
                    }
                } else if (straightStatus.straight() == Straight.DRAW) {
                    if (straightStatus.handicap() == Handicap.TWO_CARDS ||
                            (straightStatus.handicap() == Handicap.ONE_CARD && (straightStatus.rank() == Rank.ACE || straightStatus.rank() == Rank.FIVE))) {
                        return ComboExType.OC_2_AX_SD;
                    }
                } else {
                    return ComboExType.OC_2_AX;
                }
            } else {
                if (flushStatus.flush() == Flush.DRAW && flushStatus.handicap() == Handicap.TWO_CARDS) {
                    return ComboExType.OC_2_FD;
                } else if (straightStatus.straight() == Straight.DRAW && straightStatus.handicap() == Handicap.TWO_CARDS) {
                    return ComboExType.OC_2_SD;
                } else {
                    return ComboExType.OC_2;
                }
            }
        } else if (cardCell.topRank().isGreater(highestBoardRank) || cardCell.lowRank().isGreater(highestBoardRank)) {
            if (pcRanks.contains(Rank.ACE)) {
                Suit flushSuit = bySuit.entrySet().stream()
                        .max(Comparator.comparingInt(e -> e.getValue().size())).map(Map.Entry::getKey).orElseThrow();
                boolean pocketCardsContainAceSuited = pc.stream().anyMatch(card -> card.suit() == flushSuit && card.rank() == Rank.ACE);
                if (flushStatus.flush() == Flush.DRAW) {
                    if (flushStatus.handicap() == Handicap.TWO_CARDS || (flushStatus.handicap() == Handicap.ONE_CARD && pocketCardsContainAceSuited)) {
                        return ComboExType.OC_1_A_FD;
                    }
                } else if (straightStatus.straight() == Straight.DRAW) {
                    if (straightStatus.handicap() == Handicap.TWO_CARDS ||
                            (straightStatus.handicap() == Handicap.ONE_CARD && (straightStatus.rank() == Rank.ACE || straightStatus.rank() == Rank.FIVE))) {
                        return ComboExType.OC_1_A_SD;
                    }
                } else {
                    return ComboExType.OC_1_A;
                }
            } else {
                if (flushStatus.flush() == Flush.DRAW && flushStatus.handicap() == Handicap.TWO_CARDS) {
                    return ComboExType.OC_1_FD;
                } else if (straightStatus.straight() == Straight.DRAW && straightStatus.handicap() == Handicap.TWO_CARDS) {
                    return ComboExType.OC_1_SD;
                } else {
                    return ComboExType.OC_1;
                }
            }
        } else {
            if (flushStatus.flush() == Flush.DRAW) {
                if (flushStatus.handicap() == Handicap.TWO_CARDS) {
                    return ComboExType.AIR_FD;
                } else if (flushStatus.handicap() == Handicap.ONE_CARD) {
                    return ComboExType.AIR_WEAK_FD;
                }
            } else if (straightStatus.straight() == Straight.DRAW) {
                if (straightStatus.handicap() == Handicap.TWO_CARDS) {
                    return ComboExType.AIR_SD;
                } else if (straightStatus.handicap() == Handicap.ONE_CARD) {
                    return ComboExType.AIR_WEAK_SD;
                }
            }
        }
        return ComboExType.AIR;
    }

    private static ComboStatus evalTwoPairs(EnumSet<Rank> bcRanks, CactusCombo cactusCombo, Map<Long, List<Rank>> rankByCount, CardCell cardCell) {
        List<Rank> ranks = rankByCount.get(2L);
        Rank maxBoardRank = bcRanks.stream().max(Comparator.naturalOrder()).orElseThrow();
        Rank rank = cactusCombo.rank();
        boolean containsRank1 = cardCell.contains(ranks.get(0));
        boolean containsRank2 = cardCell.contains(ranks.get(1));
        ComboExType comboExType = containsRank1 && containsRank2 ? (rank == maxBoardRank ? ComboExType.SPLIT_PAIRS_TOP : ComboExType.SPLIT_PAIRS_LOW) : (!containsRank1 && !containsRank2 ? ComboExType.TWO_PAIRS_TRASH :
                (cardCell.ranks().contains(rank) ? (maxBoardRank == rank ? ComboExType.TWO_PAIRS_TOP : ComboExType.TWO_PAIRS_HIGH) : ComboExType.TWO_PAIRS_LOW));
        Handicap handicap = (comboExType == ComboExType.SPLIT_PAIRS_TOP || comboExType == ComboExType.SPLIT_PAIRS_LOW) ?
                Handicap.TWO_CARDS : (comboExType == ComboExType.TWO_PAIRS_TRASH ? Handicap.NO_CARDS : Handicap.ONE_CARD);
        return new ComboStatus(comboExType,
                new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                handicap,
                rank,
                Nuts.WEAK);
    }

    private static ComboStatus eval3K(CactusCombo cactusCombo, EnumSet<Rank> pcRanks, CardCell cardCell) {
        Rank rank = cactusCombo.rank();
        ComboExType comboExType = pcRanks.contains(rank) ? (cardCell.connector() == Connector.EQUAL ? ComboExType.SET : ComboExType.TRIPS) : ComboExType.BOARD_TOP_COMBO;
        Handicap handicap = comboExType == ComboExType.SET ? Handicap.TWO_CARDS : (comboExType == ComboExType.TRIPS ? Handicap.ONE_CARD : Handicap.NO_CARDS);
        return new ComboStatus(comboExType,
                new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                handicap,
                rank,
                Nuts.WEAK);
    }

    private static ComboStatus evalFullHouse(CactusCombo cactusCombo, EnumSet<Rank> pcRanks, Collection<Card> bc, Map<Long, List<Rank>> rankByCount, CardCell cardCell) {
        Rank rank = cactusCombo.rank();
        EnumSet<Rank> ranks = cactusCombo.ranks();
        Handicap handicap = (pcRanks.containsAll(ranks) || (cardCell.connector() == Connector.EQUAL && ranks.contains(cardCell.topRank()))) ? Handicap.TWO_CARDS :
                (cardCell.connector() != Connector.EQUAL && (ranks.contains(cardCell.topRank()) || ranks.contains(cardCell.lowRank())) ? Handicap.ONE_CARD : Handicap.NO_CARDS);
        Rank highRank = rankByCount.get(3L).stream().findFirst().orElseThrow();
        ComboExType comboExType = (pcRanks.contains(highRank) && cardCell.connector() == Connector.EQUAL) ? ComboExType.NUTS :
                (handicap == Handicap.NO_CARDS ? ComboExType.BOARD_TOP_COMBO : ComboExType.FULL_HOUSE_LOW);
        return new ComboStatus(comboExType,
                new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                handicap,
                rank,
                comboExType == ComboExType.NUTS ? Nuts.NUTS : Nuts.WEAK);
    }

    private static ComboStatus eval4K(CactusCombo cactusCombo, Collection<Card> pc, Collection<Card> bc, EnumSet<Rank> pcRanks, CardCell cardCell, EnumSet<Rank> bcRanks) {
        Rank rank = cactusCombo.rank();
        Rank maxBoardRank = bcRanks.stream().max(Comparator.naturalOrder()).orElseThrow();
        Handicap handicap = pcRanks.contains(rank) ? (cardCell.connector() == Connector.EQUAL ? Handicap.TWO_CARDS : Handicap.ONE_CARD) : Handicap.NO_CARDS;
        return new ComboStatus((handicap != Handicap.NO_CARDS || cardCell.topRank().isGreaterOrEquals(maxBoardRank)) ? ComboExType.NUTS : ComboExType.BOARD_TOP_COMBO,
                new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                handicap,
                rank,
                handicap != Handicap.NO_CARDS || pcRanks.contains(Rank.ACE) ? Nuts.NUTS : Nuts.WEAK);
    }

    private static ComboStatus getStraightComboStatus(CactusCombo cactusCombo, StraightStatus straightStatus, long suitsLongestCount, CardCell cardCell) {
        Handicap handicap = straightStatus.handicap();
        if (handicap == Handicap.NO_CARDS) {
            return new ComboStatus(ComboExType.BOARD_TOP_COMBO,
                    new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                    handicap,
                    cardCell.lowRank(),
                    Nuts.WEAK);
        } else {
            Nuts nuts = straightStatus.nuts();
            return nuts == Nuts.NUTS && suitsLongestCount < 3 ? new ComboStatus(ComboExType.NUTS,
                    new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                    handicap,
                    straightStatus.rank(),
                    nuts) : new ComboStatus(ComboExType.STRAIGHT_WEAK,
                    new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                    handicap,
                    straightStatus.rank(),
                    nuts);
        }
    }

    private static ComboStatus getFlushComboStatus(CactusCombo cactusCombo, FlushStatus flushStatus, long rankLongestCount, CardCell cardCell) {
        Handicap handicap = flushStatus.handicap();
        if (handicap == Handicap.NO_CARDS) {
            return new ComboStatus(ComboExType.BOARD_TOP_COMBO,
                    new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                    handicap,
                    cardCell.lowRank(),
                    Nuts.WEAK);
        } else {
            Nuts nuts = flushStatus.nuts() == Nuts.NUTS && rankLongestCount == 1 ? Nuts.NUTS : Nuts.WEAK;
            return nuts == Nuts.NUTS ? new ComboStatus(ComboExType.NUTS,
                    new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                    handicap,
                    flushStatus.rank(),
                    nuts) : new ComboStatus(ComboExType.FLUSH_WEAK,
                    new ComboDescription(cactusCombo.description(), cactusCombo.normalizedNumeric()),
                    handicap,
                    flushStatus.rank(),
                    nuts);
        }
    }

    private static StraightStatus evalStraightStatus(CardCell cardCell, EnumSet<Rank> pcRanks, EnumSet<Rank> bcRanks) {
        EnumSet<Rank> ranks = Stream.of(cardCell.ranks(), bcRanks)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Rank.class)));
        Map<Long, List<Map.Entry<Rank, EnumSet<Rank>>>> straightsIntersects = straights.entrySet().stream()
                .collect(Collectors.groupingBy(e -> ranks.stream().filter(rank -> e.getValue().contains(rank)).count()));
        if (straightsIntersects.containsKey(5L)) {
            return getReadyStraightStatus(cardCell, bcRanks, straightsIntersects, pcRanks);
        } else if (straightsIntersects.containsKey(4L)) {
            List<Map.Entry<Rank, EnumSet<Rank>>> entries = straightsIntersects.get(4L);
            int size = entries.size();
            if (size >= 2) {
                return getDrawStatus(cardCell, straightsIntersects, Straight.DRAW, pcRanks);
            } else if (size == 1) {
                return getDrawStatus(cardCell, straightsIntersects, Straight.GUTSHOT, pcRanks);
            }
        }
        return new StraightStatus(Straight.NONE, Handicap.NO_CARDS, Nuts.WEAK, cardCell.topRank());
    }

    private static StraightStatus getReadyStraightStatus(CardCell cardCell, EnumSet<Rank> bcRanks, Map<Long, List<Map.Entry<Rank, EnumSet<Rank>>>> straightsIntersects, EnumSet<Rank> pcRanks) {
        Map.Entry<Rank, EnumSet<Rank>> maxReadyStraight = straightsIntersects.get(5L).stream()
                .max(Comparator.comparingInt(value -> value.getKey().ordinal())).orElseThrow();
        EnumSet<Rank> readyStraightRanks = maxReadyStraight.getValue();
        long intersect = cardCell.ranks().stream().filter(readyStraightRanks::contains).count();
        Rank maxPossibleRankPocketCards = maxReadyStraight.getKey();
        Map<Long, List<Map.Entry<Rank, EnumSet<Rank>>>> straightsBoardIntersects = straights.entrySet().stream()
                .collect(Collectors.groupingBy(e -> bcRanks.stream().filter(rank -> e.getValue().contains(rank)).count()));
        Rank maxPossibleFlushRankByBoardCards = Optional.ofNullable(straightsBoardIntersects.get(3L))
                .map(e -> e.stream().map(Map.Entry::getKey).max(Comparator.naturalOrder()).orElseThrow()).orElseThrow();
        Handicap handicap = Handicap.fromIntersect((int) intersect);
        boolean isNuts = maxPossibleRankPocketCards.isGreaterOrEquals(maxPossibleFlushRankByBoardCards);
        Nuts nuts = isNuts ? Nuts.NUTS : Nuts.WEAK;
        Rank rank = handicap == Handicap.NO_CARDS ? cardCell.lowRank() :
                cardCell.ranks().stream().filter(readyStraightRanks::contains).max(Comparator.naturalOrder()).orElseThrow();
        return new StraightStatus(Straight.READY, handicap, nuts, rank);
    }

    private static StraightStatus getDrawStatus(CardCell cardCell,
                                                Map<Long, List<Map.Entry<Rank, EnumSet<Rank>>>> straightsIntersects,
                                                Straight straight, EnumSet<Rank> pcRanks) {
        Map<Long, List<Map.Entry<Rank, EnumSet<Rank>>>> pcIntersects = straightsIntersects.get(4L).stream()
                .collect(Collectors.groupingBy(r -> cardCell.ranks().stream().filter(rank -> r.getValue().contains(rank)).count()));
        long intersect = Collections.max(pcIntersects.keySet());
        Handicap handicap = Handicap.fromIntersect((int) intersect);
        List<Map.Entry<Rank, EnumSet<Rank>>> intersection = pcIntersects.get(intersect);
        boolean isNuts = handicap != Handicap.NO_CARDS &&
                intersection.stream().anyMatch(e -> e.getKey() == Rank.ACE && cardCell.topRank() == Rank.ACE);
        Nuts nuts = isNuts ? Nuts.NUTS : Nuts.WEAK;
        Rank rank = handicap == Handicap.NO_CARDS ? cardCell.lowRank() : intersection.stream()
                .max(Comparator.comparingInt(value -> value.getKey().ordinal()))
                .map(Map.Entry::getValue)
                .map(ranks -> {
                    pcRanks.retainAll(ranks);
                    return pcRanks.stream().max(Comparator.naturalOrder()).orElseThrow();
                }).orElseThrow();
        return new StraightStatus(straight, handicap, nuts, rank);
    }

    private static FlushStatus evalFlushStatus(EnumSet<Card> maxBySuit, CardCell cardCell, Collection<Card> pc) {
        if (maxBySuit.size() >= 5) {
            return getFlushStatus(pc, maxBySuit, Flush.READY);
        } else if (maxBySuit.size() == 4) {
            return getFlushStatus(pc, maxBySuit, Flush.DRAW);
        } else if (maxBySuit.size() == 3) {
            return getFlushStatus(pc, maxBySuit, Flush.BACKDOOR);
        } else {
            return new FlushStatus(Flush.NONE, Handicap.NO_CARDS, Nuts.WEAK, cardCell.topRank());
        }
    }

    private static FlushStatus getFlushStatus(Collection<Card> pc, EnumSet<Card> maxBySuit, Flush flush) {
        maxBySuit.retainAll(pc);
        Handicap handicap = Handicap.fromIntersect(maxBySuit.size());
        Nuts nuts = maxBySuit.stream().anyMatch(card -> card.rank() == Rank.ACE) ? Nuts.NUTS : Nuts.WEAK;
        Rank rank = maxBySuit.stream().map(Card::rank).max(Comparator.naturalOrder()).orElseThrow();
        return new FlushStatus(flush, handicap, nuts, rank);
    }


    private static EnumSet<Rank> toRanks(Collection<Card> cards) {
        return cards.stream().map(Card::rank).collect(Collectors.toCollection(() -> EnumSet.noneOf(Rank.class)));
    }
}
