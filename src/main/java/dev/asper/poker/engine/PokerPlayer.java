package dev.asper.poker.engine;

import dev.asper.clickhouse.OpponentType;
import dev.asper.poker.card.*;
import dev.asper.poker.enums.PreflopPosition;
import dev.asper.poker.enums.Street;
import dev.asper.poker.holding.Holding;
import dev.asper.poker.holding.eval.HoldingEvaluator;

import java.util.*;
import java.util.stream.Collectors;

public class PokerPlayer {
    private final CardPair cardPair;
    private final int initialStack;
    private final PreflopPosition preflopPosition;
    private final String playerName;
    private final CardCell cardCell;
    private final OpponentType opponentType;
    private PokerPlayerState pokerPlayerState = PokerPlayerState.DECISION_MAKER;
    private Cards flCards;
    private Card tnCard;
    private Card rvCard;
    private int totalInvested = 0;
    private int totalCollected = 0;
    private int stackStub;
    private Holding flHolding;
    private Holding tnHolding;
    private Holding rvHolding;
    private int profit = 0;
    private final EnumMap<Street, List<MoveStatistics>> movesByStreet = Arrays.stream(Street.values())
            .collect(Collectors.toMap(street -> street, street -> new ArrayList<>(), (f, f2) -> f, () -> new EnumMap<>(Street.class)));

    public CardCell getCardCell() {
        return cardCell;
    }

    public PokerPlayerState getPokerPlayerState() {
        return pokerPlayerState;
    }

    public void setPokerPlayerState(PokerPlayerState pokerPlayerState) {
        this.pokerPlayerState = pokerPlayerState;
    }

    public OpponentType getOpponentType() {
        return opponentType;
    }

    public void setFlCards(Cards flCards) {
        this.flCards = flCards;
    }

    public void calculateProfit() {
        profit = totalCollected - totalInvested;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public void setTnCard(Card tnCard) {
        this.tnCard = tnCard;
    }

    public void setRvCard(Card rvCard) {
        this.rvCard = rvCard;
    }

    public Holding getHolding(Street street) {
        return switch (street) {
            case FL -> getFlHolding();
            case TN -> getTnHolding();
            case RV -> getRvHolding();
            default -> throw new IllegalStateException("Unexpected value: " + street);
        };
    }

    public Holding getFlHolding() {
        return flHolding == null ? (flHolding = computeFlHolding()) : flHolding;
    }

    public Holding getTnHolding() {
        return tnHolding == null ? (tnHolding = computeTnHolding()) : tnHolding;
    }

    public Holding getRvHolding() {
        return rvHolding == null ? (rvHolding = computeRvHolding()) : rvHolding;
    }

    public void addCollectedAmount(int totalCollected) {
        this.totalCollected += totalCollected;
    }

    private synchronized Holding computeFlHolding() {
        return HoldingEvaluator.eval(cardPair.cards(), flCards.cards());
    }

    private synchronized Holding computeTnHolding() {
        return HoldingEvaluator.eval(cardPair.cards(), flCards.plus(tnCard).cards());
    }

    private synchronized Holding computeRvHolding() {
        return HoldingEvaluator.eval(cardPair.cards(), flCards.plus(tnCard, rvCard).cards());
    }

    public PokerPlayer(CardPair cardPair, int initialStack, PreflopPosition preflopPosition, String playerName, OpponentType opponentType) {
        Objects.requireNonNull(this.cardPair = cardPair);
        this.initialStack = initialStack;
        this.stackStub = initialStack;
        this.preflopPosition = preflopPosition;
        this.playerName = playerName;
        this.opponentType = opponentType;
        this.cardCell = CardCell.from(this.cardPair.getHighCard(), this.cardPair.getLowCard());
    }

    public PokerPlayer(int initialStack, PreflopPosition preflopPosition, String playerName, OpponentType opponentType) {
        this.cardPair = null;
        this.cardCell = null;
        this.initialStack = initialStack;
        this.stackStub = initialStack;
        this.preflopPosition = preflopPosition;
        this.playerName = playerName;
        this.opponentType = opponentType;
    }

    private PokerPlayer(CardPair cardPair, CardCell cardCell, int initialStack, PreflopPosition preflopPosition, String playerName, OpponentType opponentType) {
        this.cardPair = cardPair;
        this.cardCell = cardCell;
        this.initialStack = initialStack;
        this.stackStub = initialStack;
        this.preflopPosition = preflopPosition;
        this.playerName = playerName;
        this.opponentType = opponentType;
    }

    public void takeAmount(int amount) {
        totalInvested += amount;
        stackStub -= amount;
    }

    public int computeCallAmount(int totalBetAmount) {
        return totalBetAmount - totalInvested;
    }

    public String getPlayerName() {
        return playerName;
    }

    public CardPair getCardPair() {
        return cardPair;
    }

    public int getInitialStack() {
        return initialStack;
    }

    public int getStackStub() {
        return stackStub;
    }

    public int getTotalInvestedAmount() {
        return totalInvested;
    }

    public PreflopPosition getPreflopPosition() {
        return preflopPosition;
    }

    public int getTotalCollectedAmount() {
        return totalCollected;
    }

    @Override
    public String toString() {
        return preflopPosition + "-" + opponentType + "(" + playerName + ")" + (cardPair == null ? "" : cardPair + " ") + initialStack + "->" + stackStub;
    }

    public int getProfit() {
        return profit;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PokerPlayer && ((PokerPlayer) obj).playerName.equals(playerName);
    }

    @Override
    public int hashCode() {
        return playerName.hashCode();
    }

    public boolean isPocketCardsOpen() {
        return cardPair != null;
    }

    private PokerPlayer(PokerPlayer from) {
        this.cardPair = from.cardPair;
        this.initialStack = from.initialStack;
        this.preflopPosition = from.preflopPosition;
        this.playerName = from.playerName;
        this.cardCell = from.cardCell;
        this.opponentType = from.opponentType;
        this.flCards = from.flCards;
        this.tnCard = from.tnCard;
        this.rvCard = from.rvCard;
        this.totalInvested = from.totalInvested;
        this.totalCollected = from.totalCollected;
        this.stackStub = from.stackStub;
        this.flHolding = from.flHolding;
        this.tnHolding = from.tnHolding;
        this.rvHolding = from.rvHolding;
        this.profit = from.profit;
        this.pokerPlayerState = from.pokerPlayerState;
        from.movesByStreet.forEach((street, moveStatistics) -> this.movesByStreet.put(street, new ArrayList<>(moveStatistics)));
    }

    public PokerPlayer copy() {
        return new PokerPlayer(this);
    }

    public void addMoveStatistics(Street street, MoveStatistics moveStatistics) {
        this.movesByStreet.get(street).add(moveStatistics);
    }

    public List<MoveStatistics> getMoveStatistics(Street street) {
        return movesByStreet.get(street);
    }

    public boolean isFolded() {
        return pokerPlayerState == PokerPlayerState.FOLDED;
    }
}
