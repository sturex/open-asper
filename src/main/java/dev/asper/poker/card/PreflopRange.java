package dev.asper.poker.card;

import dev.asper.common.util.ExceptionHelper;

import java.io.Serial;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PreflopRange extends EnumMap<CardPair, Double> {
    @Serial
    private static final long serialVersionUID = 5808851422907356348L;
    public static final PreflopRange EMPTY = new PreflopRange();
    private static final Random random = new Random();
    public static final double WEIGHT = 1. / 1326;

    private static final PreflopRange FULL_FLAT_RANGE = Arrays.stream(CardPair.values())
            .collect(Collectors.toMap(
                    cardPair -> cardPair,
                    cardPair -> WEIGHT,
                    (c1, c2) -> c1,
                    PreflopRange::new));

    private PreflopRange() {
        super(CardPair.class);
    }

    public PreflopRange(Map<CardPair, ? extends Double> m) {
        super(m);
    }

    public static PreflopRange fullFlatRange() {
        return new PreflopRange(FULL_FLAT_RANGE);
    }

    public static PreflopRange ofCardPair(CardPair cardPair) {
        PreflopRange range = new PreflopRange();
        range.put(cardPair, 1.);
        return range;
    }

    public CardPair pickAndNormalize() {
        //TODO remove assuming (make sure) totalWeight == 1 always
        double totalWeight = calculateTotalWeight();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0.0;

        for (Map.Entry<CardPair, Double> entry : entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue <= cumulativeWeight) {
                CardPair cardPair = entry.getKey();
                remove(cardPair);
                normalize();
                return cardPair;
            }
        }

        CardPair cardPair = keySet().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Empty range"));
        remove(cardPair);
        normalize();
        return cardPair;
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

    private double calculateTotalWeight() {
        return values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public void removeAndNormalize(Card card) {
        entrySet().removeIf(e -> e.getKey().contains(card));
        normalize();
    }

    public void removeAndNormalize(CardPair cardPair) {
        entrySet().removeIf(e -> {
            CardPair keyCardPair = e.getKey();
            return keyCardPair.contains(cardPair.getHighCard()) || keyCardPair.contains(cardPair.getLowCard());
        });
        normalize();
    }

    public void removeCardPairsAndNormalize(Collection<CardPair> cardPairs) {
        if (!cardPairs.isEmpty()) {
            entrySet().removeIf(e -> {
                CardPair keyCardPair = e.getKey();
                return cardPairs.stream()
                        .anyMatch(cp -> keyCardPair.contains(cp.getHighCard()) || keyCardPair.contains(cp.getLowCard()));
            });
            normalize();
        }
    }

    public void removeAndNormalize(EnumSet<Card> cards) {
        if (!cards.isEmpty()) {
            entrySet().removeIf(e -> {
                CardPair cardPair = e.getKey();
                return cards.contains(cardPair.getHighCard()) || cards.contains(cardPair.getLowCard());
            });
            normalize();
        }
    }

    public void normalize() {
        double totalWeight = calculateTotalWeight();
        replaceAll((cardPair, w) -> w / totalWeight);
    }

    public static PreflopRange with(EnumMap<CardCell, Double> cardCellMap) {
        PreflopRange range = new PreflopRange();
        cardCellMap.forEach((key, value) -> {
            EnumSet<CardPair> cardPairs = key.getCardPairs();
            double cardPairWeight = value / cardPairs.size();
            cardPairs.forEach(cardPair -> range.put(cardPair, cardPairWeight));
        });
        range.normalize();
        return range;
    }

    public double totalWeight() {
        return calculateTotalWeight();
    }

    public PreflopRange copy() {
        return (PreflopRange) super.clone();
    }

    public void multiply(PreflopRange preflopRange) {
        double totalWeight = calculateTotalWeight();
//        replaceAll((cardPair, w) -> {
//            Optional.ofNullable(preflopRange.get(cardPair))
//                    .map(oppWeight -> )
//        });
    }

    public PreflopRange multiply(double value) {
        replaceAll((cardPair, w) -> w * value);
        return this;
    }

    public PreflopRange divide(double value) {
        replaceAll((cardPair, w) -> w / value);
        return this;
    }

    public PreflopRange plus(PreflopRange r2) {
        return Stream.concat(entrySet().stream(), r2.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            double sum = Double.sum(a, b);
                            ExceptionHelper.throwIf(sum > 1, "Invalid sum %f".formatted(sum));
                            return sum;
                        },
                        () -> new PreflopRange(new EnumMap<>(CardPair.class))));
    }
}
