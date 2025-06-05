package dev.asper.poker.card;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Card {

    TWO_OF_CLUBS(Rank.TWO, Suit.CLUBS),
    THREE_OF_CLUBS(Rank.THREE, Suit.CLUBS),
    FOUR_OF_CLUBS(Rank.FOUR, Suit.CLUBS),
    FIVE_OF_CLUBS(Rank.FIVE, Suit.CLUBS),
    SIX_OF_CLUBS(Rank.SIX, Suit.CLUBS),
    SEVEN_OF_CLUBS(Rank.SEVEN, Suit.CLUBS),
    EIGHT_OF_CLUBS(Rank.EIGHT, Suit.CLUBS),
    NINE_OF_CLUBS(Rank.NINE, Suit.CLUBS),
    TEN_OF_CLUBS(Rank.TEN, Suit.CLUBS),
    JACK_OF_CLUBS(Rank.JACK, Suit.CLUBS),
    QUEEN_OF_CLUBS(Rank.QUEEN, Suit.CLUBS),
    KING_OF_CLUBS(Rank.KING, Suit.CLUBS),
    ACE_OF_CLUBS(Rank.ACE, Suit.CLUBS),
    TWO_OF_DIAMONDS(Rank.TWO, Suit.DIAMONDS),
    THREE_OF_DIAMONDS(Rank.THREE, Suit.DIAMONDS),
    FOUR_OF_DIAMONDS(Rank.FOUR, Suit.DIAMONDS),
    FIVE_OF_DIAMONDS(Rank.FIVE, Suit.DIAMONDS),
    SIX_OF_DIAMONDS(Rank.SIX, Suit.DIAMONDS),
    SEVEN_OF_DIAMONDS(Rank.SEVEN, Suit.DIAMONDS),
    EIGHT_OF_DIAMONDS(Rank.EIGHT, Suit.DIAMONDS),
    NINE_OF_DIAMONDS(Rank.NINE, Suit.DIAMONDS),
    TEN_OF_DIAMONDS(Rank.TEN, Suit.DIAMONDS),
    JACK_OF_DIAMONDS(Rank.JACK, Suit.DIAMONDS),
    QUEEN_OF_DIAMONDS(Rank.QUEEN, Suit.DIAMONDS),
    KING_OF_DIAMONDS(Rank.KING, Suit.DIAMONDS),
    ACE_OF_DIAMONDS(Rank.ACE, Suit.DIAMONDS),
    TWO_OF_SPADES(Rank.TWO, Suit.SPADES),
    THREE_OF_SPADES(Rank.THREE, Suit.SPADES),
    FOUR_OF_SPADES(Rank.FOUR, Suit.SPADES),
    FIVE_OF_SPADES(Rank.FIVE, Suit.SPADES),
    SIX_OF_SPADES(Rank.SIX, Suit.SPADES),
    SEVEN_OF_SPADES(Rank.SEVEN, Suit.SPADES),
    EIGHT_OF_SPADES(Rank.EIGHT, Suit.SPADES),
    NINE_OF_SPADES(Rank.NINE, Suit.SPADES),
    TEN_OF_SPADES(Rank.TEN, Suit.SPADES),
    JACK_OF_SPADES(Rank.JACK, Suit.SPADES),
    QUEEN_OF_SPADES(Rank.QUEEN, Suit.SPADES),
    KING_OF_SPADES(Rank.KING, Suit.SPADES),
    ACE_OF_SPADES(Rank.ACE, Suit.SPADES),
    TWO_OF_HEARTS(Rank.TWO, Suit.HEARTS),
    THREE_OF_HEARTS(Rank.THREE, Suit.HEARTS),
    FOUR_OF_HEARTS(Rank.FOUR, Suit.HEARTS),
    FIVE_OF_HEARTS(Rank.FIVE, Suit.HEARTS),
    SIX_OF_HEARTS(Rank.SIX, Suit.HEARTS),
    SEVEN_OF_HEARTS(Rank.SEVEN, Suit.HEARTS),
    EIGHT_OF_HEARTS(Rank.EIGHT, Suit.HEARTS),
    NINE_OF_HEARTS(Rank.NINE, Suit.HEARTS),
    TEN_OF_HEARTS(Rank.TEN, Suit.HEARTS),
    JACK_OF_HEARTS(Rank.JACK, Suit.HEARTS),
    QUEEN_OF_HEARTS(Rank.QUEEN, Suit.HEARTS),
    KING_OF_HEARTS(Rank.KING, Suit.HEARTS),
    ACE_OF_HEARTS(Rank.ACE, Suit.HEARTS);

    private final Rank rank;
    private final Suit suit;
    private final String shortName;

    Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.shortName = rank.letter() + suit.letter();
    }

    public Rank rank() {
        return rank;
    }

    public Suit suit() {
        return suit;
    }

    public String stdName() {
        return shortName;
    }

    public static final Map<String, Card> map = Arrays.stream(values()).collect(Collectors.toMap(Card::stdName, Function.identity()));

    public static final Card[][] cardsMatrix = new Card[Rank.values().length][Suit.values().length];

    static {
        for (Card card : values()) {
            cardsMatrix[card.rank().ordinal()][card.suit().ordinal()] = card;
        }
    }

    public static Card from(Rank rank, Suit suit) {
        return cardsMatrix[rank.ordinal()][suit.ordinal()];
    }

    public static Card from(int rankOrdinal, int suitOrdinal) {
        return cardsMatrix[rankOrdinal][suitOrdinal];
    }

    public static Card from(String value) {
        return map.get(value);
    }


    @Override
    public String toString() {
        return shortName;
    }
}
