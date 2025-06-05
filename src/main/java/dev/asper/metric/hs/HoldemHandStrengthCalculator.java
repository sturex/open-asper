package dev.asper.metric.hs;

import dev.asper.metric.cactus.CactusSimpleEvaluator;
import dev.asper.poker.card.Card;
import dev.asper.poker.card.CardPair;
import dev.asper.poker.card.Deck;
import dev.asper.poker.card.PreflopRange;
import dev.asper.poker.enums.PreflopPosition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public enum HoldemHandStrengthCalculator {
    ;

    public static double winRate(PreflopRange heroRange, List<PreflopRange> oppRanges, EnumSet<Card> boardCards, int iterCount) {
        return IntStream.range(0, iterCount)
                .parallel()
                .mapToDouble(idx -> winRate(heroRange, oppRanges, boardCards))
                .average().orElse(0.0);
    }

    public static double winRate(CardPair cardPair, List<PreflopRange> oppRanges, EnumSet<Card> boardCards, int iterCount) {
        return IntStream.range(0, iterCount)
                .parallel()
                .mapToDouble(idx -> winRate(cardPair, oppRanges, boardCards))
                .average().orElse(0.0);
    }

    public static double winRate(PreflopRange heroRange, List<PreflopRange> oppRanges, int iterCount) {
        return IntStream.range(0, iterCount)
                .parallel()
                .mapToDouble(idx -> winRate(heroRange, oppRanges))
                .average().orElse(0.0);
    }

    public static double winRate(CardPair cardPair, List<PreflopRange> oppRanges, int iterCount) {
        return IntStream.range(0, iterCount)
                .parallel()
                .mapToDouble(idx -> winRate(cardPair, oppRanges))
                .average().orElse(0.0);
    }

    public static EnumMap<PreflopPosition, Double> winRate(PreflopPosition heroPosition,
                                                           CardPair heroCardPair,
                                                           Map<PreflopPosition, PreflopRange> oppRangeMap,
                                                           int iterCount) {
        return IntStream.range(0, iterCount)
                .parallel()
                .mapToObj(idx -> winRate(heroPosition, heroCardPair, oppRangeMap))
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        () -> new EnumMap<>(PreflopPosition.class),
                        Collectors.averagingDouble(Map.Entry::getValue)));
    }

    public static EnumMap<PreflopPosition, Double> winRate(PreflopPosition heroPosition,
                                                           CardPair heroCardPair,
                                                           Map<PreflopPosition, PreflopRange> oppRangeMap,
                                                           EnumSet<Card> boardCards,
                                                           int iterCount) {
        return IntStream.range(0, iterCount)
                .parallel()
                .mapToObj(idx -> winRate(heroPosition, heroCardPair, oppRangeMap, boardCards))
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        () -> new EnumMap<>(PreflopPosition.class),
                        Collectors.averagingDouble(Map.Entry::getValue)));
    }


    public static EnumMap<PreflopPosition, Double> winRate(PreflopPosition heroPosition,
                                                           CardPair heroCardPair,
                                                           Map<PreflopPosition, PreflopRange> oppRangeMap) {
        EnumMap<PreflopPosition, CardPair> cardPairs = new EnumMap<>(PreflopPosition.class);
        cardPairs.put(heroPosition, heroCardPair);
        oppRangeMap.forEach((preflopPosition, preflopRange) -> {
            PreflopRange copy = preflopRange.copy();
            copy.removeCardPairsAndNormalize(cardPairs.values());
            CardPair oppCardPair = copy.pickAndNormalize();
            cardPairs.put(preflopPosition, oppCardPair);
        });
        EnumSet<Card> deadCards = cardPairs.values().stream().flatMap(cardPair -> cardPair.cards().stream())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Card.class)));
        Deck deck = Deck.shuffledWithout(deadCards);
        EnumSet<Card> boardCards = EnumSet.of(deck.pick(), deck.pick(), deck.pick(), deck.pick(), deck.pick());
        EnumMap<PreflopPosition, Double> hsMap = cardPairs.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> CactusSimpleEvaluator.eval(e.getValue().cards(), boardCards).normalizedNumeric(),
                        (o, o2) -> o,
                        () -> new EnumMap<>(PreflopPosition.class)));
        double maxHs = Collections.max(hsMap.values());
        long count = hsMap.values().stream().filter(v -> v >= maxHs).count();
        hsMap.replaceAll((preflopPosition, hs) -> hs >= maxHs ? 1.0 / count : 0.0);
        return hsMap;
    }


    public static EnumMap<PreflopPosition, Double> winRate(PreflopPosition heroPosition,
                                                           CardPair heroCardPair,
                                                           Map<PreflopPosition, PreflopRange> oppRangeMap,
                                                           EnumSet<Card> boardCards) {
        EnumMap<PreflopPosition, CardPair> cardPairs = new EnumMap<>(PreflopPosition.class);
        cardPairs.put(heroPosition, heroCardPair);
        oppRangeMap.forEach((preflopPosition, preflopRange) -> {
            PreflopRange copy = preflopRange.copy();
            copy.removeAndNormalize(boardCards);
            copy.removeCardPairsAndNormalize(cardPairs.values());
            CardPair oppCardPair = copy.pickAndNormalize();
            cardPairs.put(preflopPosition, oppCardPair);
        });
        EnumSet<Card> deadCards = Stream.concat(cardPairs.values().stream().flatMap(cardPair -> cardPair.cards().stream()), boardCards.stream())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Card.class)));
        Deck deck = Deck.shuffledWithout(deadCards);
        EnumSet<Card> boardCardsClone = boardCards.clone();
        for (int i = boardCardsClone.size(); i < 5; i++) {
            boardCardsClone.add(deck.pick());
        }
        EnumMap<PreflopPosition, Double> hsMap = cardPairs.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> CactusSimpleEvaluator.eval(e.getValue().cards(), boardCardsClone).normalizedNumeric(),
                        (o, o2) -> o,
                        () -> new EnumMap<>(PreflopPosition.class)));
        double maxHs = Collections.max(hsMap.values());
        long count = hsMap.values().stream().filter(v -> v >= maxHs).count();
        hsMap.replaceAll((preflopPosition, hs) -> hs >= maxHs ? 1.0 / count : 0.0);
        return hsMap;
    }

    private static double winRate(PreflopRange heroRange, List<PreflopRange> oppRanges, EnumSet<Card> boardCards) {
        PreflopRange heroRangeCopy = heroRange.copy();
        heroRangeCopy.removeAndNormalize(boardCards);
        List<PreflopRange> oppRangeCopies = oppRanges.stream().map(PreflopRange::copy).toList();
        oppRangeCopies.forEach(holdemRange -> holdemRange.removeAndNormalize(boardCards));
        CardPair heroCardPair = heroRangeCopy.pickAndNormalize();
        EnumSet<CardPair> oppCardPairs = EnumSet.of(heroCardPair);
        oppRangeCopies.forEach(holdemRange -> {
            holdemRange.removeCardPairsAndNormalize(oppCardPairs);
            CardPair oppCardPair = holdemRange.pickAndNormalize();
            oppCardPairs.add(oppCardPair);
        });
        EnumSet<Card> deadCards = Stream.concat(oppCardPairs.stream().flatMap(cardPair -> cardPair.cards().stream()),
                        boardCards.stream())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Card.class)));
        oppCardPairs.remove(heroCardPair);
        EnumSet<Card> boardCardsClone = boardCards.clone();
        Deck deck = Deck.shuffledWithout(deadCards);
        for (int i = boardCardsClone.size(); i < 5; i++) {
            boardCardsClone.add(deck.pick());
        }
        double heroStrength = CactusSimpleEvaluator.eval(heroCardPair.cards(), boardCardsClone).normalizedNumeric();
        return oppCardPairs.stream()
                .anyMatch(cardPair -> heroStrength <= CactusSimpleEvaluator.eval(cardPair.cards(), boardCardsClone).normalizedNumeric()) ? 0 : 1;
    }

    private static double winRate(CardPair heroCardPair, List<PreflopRange> oppRanges, EnumSet<Card> boardCards) {
        List<PreflopRange> oppRangeCopies = oppRanges.stream().map(PreflopRange::copy).toList();
        oppRangeCopies.forEach(holdemRange -> holdemRange.removeAndNormalize(boardCards));
        EnumSet<CardPair> oppCardPairs = EnumSet.of(heroCardPair);
        oppRangeCopies.forEach(holdemRange -> {
            holdemRange.removeCardPairsAndNormalize(oppCardPairs);
            CardPair oppCardPair = holdemRange.pickAndNormalize();
            oppCardPairs.add(oppCardPair);
        });
        EnumSet<Card> deadCards = Stream.concat(oppCardPairs.stream().flatMap(cardPair -> cardPair.cards().stream()),
                        boardCards.stream())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Card.class)));
        oppCardPairs.remove(heroCardPair);
        EnumSet<Card> boardCardsClone = boardCards.clone();
        Deck deck = Deck.shuffledWithout(deadCards);
        for (int i = boardCardsClone.size(); i < 5; i++) {
            boardCardsClone.add(deck.pick());
        }
        double heroStrength = CactusSimpleEvaluator.eval(heroCardPair.cards(), boardCardsClone).normalizedNumeric();
        return oppCardPairs.stream()
                .anyMatch(cardPair -> heroStrength <= CactusSimpleEvaluator.eval(cardPair.cards(), boardCardsClone).normalizedNumeric()) ? 0 : 1;
    }

    private static double winRate(PreflopRange heroRange, List<PreflopRange> oppRanges) {
        PreflopRange heroRangeCopy = heroRange.copy();
        List<PreflopRange> oppRangeCopies = oppRanges.stream().map(PreflopRange::copy).toList();
        CardPair heroCardPair = heroRangeCopy.pickAndNormalize();
        EnumSet<CardPair> oppCardPairs = EnumSet.of(heroCardPair);
        oppRangeCopies.forEach(holdemRange -> {
            holdemRange.removeCardPairsAndNormalize(oppCardPairs);
            CardPair oppCardPair = holdemRange.pickAndNormalize();
            oppCardPairs.add(oppCardPair);
        });
        EnumSet<Card> deadCards = oppCardPairs.stream().flatMap(cardPair -> cardPair.cards().stream())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Card.class)));
        oppCardPairs.remove(heroCardPair);
        Deck deck = Deck.shuffledWithout(deadCards);
        EnumSet<Card> boardCards = EnumSet.of(deck.pick(), deck.pick(), deck.pick(), deck.pick(), deck.pick());
        double heroStrength = CactusSimpleEvaluator.eval(heroCardPair.cards(), boardCards).normalizedNumeric();
        return oppCardPairs.stream()
                .anyMatch(cardPair -> heroStrength <= CactusSimpleEvaluator.eval(cardPair.cards(), boardCards).normalizedNumeric()) ? 0 : 1;
    }

    private static double winRate(CardPair heroCardPair, List<PreflopRange> oppRanges) {
        List<PreflopRange> oppRangeCopies = oppRanges.stream().map(PreflopRange::copy).toList();
        EnumSet<CardPair> oppCardPairs = EnumSet.of(heroCardPair);
        oppRangeCopies.forEach(holdemRange -> {
            holdemRange.removeCardPairsAndNormalize(oppCardPairs);
            CardPair oppCardPair = holdemRange.pickAndNormalize();
            oppCardPairs.add(oppCardPair);
        });
        EnumSet<Card> deadCards = oppCardPairs.stream().flatMap(cardPair -> cardPair.cards().stream())
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Card.class)));
        oppCardPairs.remove(heroCardPair);
        Deck deck = Deck.shuffledWithout(deadCards);
        EnumSet<Card> boardCards = EnumSet.of(deck.pick(), deck.pick(), deck.pick(), deck.pick(), deck.pick());
        double heroStrength = CactusSimpleEvaluator.eval(heroCardPair.cards(), boardCards).normalizedNumeric();
        return oppCardPairs.stream()
                .anyMatch(cardPair -> heroStrength <= CactusSimpleEvaluator.eval(cardPair.cards(), boardCards).normalizedNumeric()) ? 0 : 1;
    }
}
