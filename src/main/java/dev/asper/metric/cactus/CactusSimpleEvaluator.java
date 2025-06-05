package dev.asper.metric.cactus;

import dev.asper.poker.card.Card;
import dev.asper.poker.card.Deck;
import dev.asper.poker.card.Rank;
import dev.asper.poker.holding.ComboType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public enum CactusSimpleEvaluator {
    ;

    private final static int[] flushes;
    private final static int[] products;
    private final static int[] unique;
    private final static int[] values;
    private final static ComboType[] comboTypes;
    private final static EnumSet<Rank>[] comboRankSets;
    private final static String[] descriptions;
    private final static Rank[] comboRanks;
    private final static double[] comboNormalizedValues;
    private final static Map<Integer, Integer> product2ValueMap;
    private final static Map<String, ComboType> mapping = new HashMap<>();

    static {
        mapping.put("SF", ComboType.STRAIGHT_FLUSH);
        mapping.put("4K", ComboType.FOUR_OF_A_KIND);
        mapping.put("FH", ComboType.FULL_HOUSE);
        mapping.put("F", ComboType.FLUSH);
        mapping.put("S", ComboType.STRAIGHT);
        mapping.put("3K", ComboType.THREE_OF_A_KIND);
        mapping.put("2P", ComboType.TWO_PAIR);
        mapping.put("1P", ComboType.ONE_PAIR);
        mapping.put("HC", ComboType.HIGH_CARD);

        List<ComboType> comboTypesList = new ArrayList<>();
        List<String> descriptionsList = new ArrayList<>();
        List<EnumSet<Rank>> comboRankSetsList = new ArrayList<>();
        List<Rank> comboRanksList = new ArrayList<>();
        List<Double> comboNormalizedValuesList = new ArrayList<>();

        ClassLoader classLoader = CactusSimpleEvaluator.class.getClassLoader();
        try {
            URL resource = classLoader.getResource("cactus/combos_ex.txt");
            Path path = Paths.get((Objects.requireNonNull(resource).toURI()));
            List<String> lines = Files.readAllLines(path);
            lines.forEach(l -> {
                String[] split = l.split("\\t");
                int pos = Integer.parseInt(split[0]);
                descriptionsList.add(split[4]);
                comboTypesList.add(mapping.get(split[3]));
                comboRankSetsList.add(split[6].chars()
                        .mapToObj(c -> String.valueOf((char) c))
                        .map(Rank::of)
                        .collect(Collectors.toCollection(() -> EnumSet.noneOf(Rank.class))));
                comboRanksList.add(Rank.of(split[5]));
                comboNormalizedValuesList.add(Double.parseDouble(split[7]));
            });
            comboTypes = comboTypesList.toArray(ComboType[]::new);
            comboRankSets = comboRankSetsList.toArray(EnumSet[]::new);
            descriptions = descriptionsList.toArray(String[]::new);
            comboRanks = comboRanksList.toArray(Rank[]::new);
            comboNormalizedValues = comboNormalizedValuesList.stream().mapToDouble(Double::doubleValue).toArray();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        flushes = readIntegers(classLoader, "cactus/flushes.txt");
        products = readIntegers(classLoader, "cactus/products.txt");
        unique = readIntegers(classLoader, "cactus/unique.txt");
        values = readIntegers(classLoader, "cactus/values.txt");
        product2ValueMap = new HashMap<>();
        IntStream.range(0, products.length).forEach(idx -> product2ValueMap.put(products[idx], values[idx]));
    }

    private static int[] readIntegers(ClassLoader classLoader, String str) {
        try {

            URL resource = classLoader.getResource(str);
            Path path = Paths.get(Objects.requireNonNull(Objects.requireNonNull(resource).toURI()));
            String text = Files.readString(path);
            String[] split = text.split("[\\s,]+");
            return Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private final static int[] primes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41};
    private final static int[] cp = new int[Deck.NUM_CARDS];

    static {
        int i = 0;
        int n = 0;
        int suit = 0x8000;
        while (i < Deck.NUM_SUITS) {
            int j = 0;
            while (j <= Deck.NUM_RANKS) {
                int value = primes[j] | ((j << 8) | suit | (1 << 16 + j));
                cp[n] = value;
                j++;
                n++;
            }
            i++;
            suit = suit >> 1;
        }
    }

    public static int prime(Card card) {
        return primes[card.rank().ordinal()];
    }

    public static int encode(Card card) {
        return cp[card.ordinal()];
    }

    public static CactusCombo eval(String cards) {
        String[] split = cards.split("(?<=\\G.{2})");
        return eval(Arrays.stream(split).map(Card::from).toList());
    }

    public static CactusCombo eval(List<Card> cards) {
        int[] intCards = cards.stream().mapToInt(CactusSimpleEvaluator::encode).toArray();
        return evalCactusCombo(intCards, intCards.length);
    }

    public static CactusCombo eval(Collection<Card> pocketCards, Collection<Card> boardCards) {
        int[] intCards = Stream.of(pocketCards, boardCards)
                .flatMapToInt(cards -> cards.stream().mapToInt(CactusSimpleEvaluator::encode))
                .toArray();
        return evalCactusCombo(intCards, intCards.length);
    }

    private static CactusCombo evalCactusCombo(int[] intCards, int length) {
        int combo = switch (length) {
            case 5 -> eval5(intCards[0], intCards[1], intCards[2], intCards[3], intCards[4]);
            case 6 -> eval6(intCards);
            case 7 -> eval7(intCards);
            default -> throw new IllegalStateException("Unexpected value: " + length);
        } - 1;
        return new CactusCombo(comboTypes[combo], descriptions[combo], comboNormalizedValues[combo], comboRanks[combo], comboRankSets[combo]);
    }

    private static int eval6(int[] intCards) {
        int rate = eval5(intCards[0], intCards[1], intCards[2], intCards[3], intCards[4]);
        rate = Math.min(rate, eval5(intCards[0], intCards[1], intCards[2], intCards[3], intCards[5]));
        rate = Math.min(rate, eval5(intCards[0], intCards[1], intCards[2], intCards[4], intCards[5]));
        rate = Math.min(rate, eval5(intCards[0], intCards[1], intCards[3], intCards[4], intCards[5]));
        rate = Math.min(rate, eval5(intCards[0], intCards[2], intCards[3], intCards[4], intCards[5]));
        rate = Math.min(rate, eval5(intCards[1], intCards[2], intCards[3], intCards[4], intCards[5]));
        return rate;
    }

    private static int eval6(int c1, int c2, int c3, int c4, int c5, int c6) {
        int rate = eval5(c1, c2, c3, c4, c5);
        rate = Math.min(rate, eval5(c1, c2, c3, c4, c6));
        rate = Math.min(rate, eval5(c1, c2, c3, c5, c6));
        rate = Math.min(rate, eval5(c1, c2, c4, c5, c6));
        rate = Math.min(rate, eval5(c1, c3, c4, c5, c6));
        rate = Math.min(rate, eval5(c2, c3, c4, c5, c6));
        return rate;
    }

    private static int eval7(int[] intCards) {
        return eval7(intCards[0], intCards[1], intCards[2], intCards[3], intCards[4], intCards[5], intCards[6]);
    }

    private static int eval7(int c1, int c2, int c3, int c4, int c5, int c6, int c7) {
        int rate = eval6(c1, c2, c3, c4, c5, c6);
        rate = Math.min(rate, eval6(c1, c2, c3, c4, c5, c7));
        rate = Math.min(rate, eval6(c1, c2, c3, c4, c6, c7));
        rate = Math.min(rate, eval6(c1, c2, c3, c5, c6, c7));
        rate = Math.min(rate, eval6(c1, c2, c4, c5, c6, c7));
        rate = Math.min(rate, eval6(c1, c3, c4, c5, c6, c7));
        rate = Math.min(rate, eval6(c2, c3, c4, c5, c6, c7));
        return rate;
    }

    private static int eval5(int c1, int c2, int c3, int c4, int c5) {
        int q = (c1 | c2 | c3 | c4 | c5) >> 16;
        if (((c1 & c2 & c3 & c4 & c5) & 0xF000) != 0) {
            return flushes[q];
        }
        int s = unique[q];
        if (s > 0) {
            return s;
        } else {
            q = (c1 & 0xFF) * (c2 & 0xFF) * (c3 & 0xFF) * (c4 & 0xFF) * (c5 & 0xFF);
            return product2ValueMap.getOrDefault(q, 0);
        }
    }
}
