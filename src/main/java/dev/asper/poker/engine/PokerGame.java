package dev.asper.poker.engine;

import dev.asper.advice.Decision;
import dev.asper.clickhouse.OpponentType;
import dev.asper.common.fsm.Stateful;
import dev.asper.poker.card.Card;
import dev.asper.poker.card.CardPair;
import dev.asper.poker.card.Cards;
import dev.asper.poker.enums.*;
import dev.asper.poker.holding.BoardStatus;
import dev.asper.poker.holding.Holding;
import dev.asper.poker.holding.eval.BoardEvaluator;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PokerGame implements Stateful<PokerSituation> {
    private final static Logger logger = Logger.getLogger(PokerGame.class.getSimpleName());
    private final List<PokerPlayer> allinPlayers = new ArrayList<>();
    private final List<PokerPlayer> decisionMakingPlayers = new ArrayList<>();
    private final List<PokerPlayer> allPlayersFilteringFolded = new ArrayList<>();
    private final BoardSize boardSize;
    private final String handId;
    private String comment;
    private final String handDate;
    private final int sbAmount;
    private final int bbAmount;
    private final CompetitionType competitionType;
    private final EnumMap<PreflopPosition, PokerPlayer> playersByPosition;
    private final EnumMap<Street, List<MoveStatistics>> movesByStreet = Arrays.stream(Street.values())
            .collect(Collectors.toMap(street -> street, street -> new ArrayList<>(), (f, f2) -> f, () -> new EnumMap<>(Street.class)));
    private final double totalInitialStack;
    private PokerSituation state;
    private int totalBet = 0;
    private int uncalledBet;
    private int potSize = 0;
    private PokerPlayer nextPlayer;
    private PokerPlayer currentPlayer;
    private PfPotType pfPotType;
    private PostflopPotType flPotType;
    private PostflopPotType tnPotType;
    private Cards flCards;
    private Card tnCard;
    private Card rvCard;
    private BoardStatus flBoardStatus;
    private BoardStatus tnBoardStatus;
    private BoardStatus rvBoardStatus;
    private double flAvgStack;
    private double tnAvgStack;
    private double rvAvgStack;
    private double flInitialPotSize;
    private double tnInitialPotSize;
    private double rvInitialPotSize;

    public int getSbAmount() {
        return sbAmount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CompetitionType getCompetitionType() {
        return competitionType;
    }

    public int getPotSize() {
        return potSize;
    }

    public Cards getFlCards() {
        return flCards;
    }

    public List<PokerPlayer> getAllPlayersFilteringFolded() {
        return allPlayersFilteringFolded;
    }

    public Card getTnCard() {
        return tnCard;
    }

    public Card getRvCard() {
        return rvCard;
    }

    public EnumMap<PreflopPosition, PokerPlayer> getPlayersByPosition() {
        return playersByPosition;
    }

    public PokerPlayer getCurrentPlayerOrThrow() {
        return Optional.ofNullable(currentPlayer)
                .orElseThrow(() -> new RuntimeException("Current player not found pokerGame=" + this));
    }

    public BoardStatus getFlBoardStatus() {
        return flBoardStatus == null ? (flBoardStatus = computeFlBoardStatus()) : flBoardStatus;
    }

    public BoardStatus getTnBoardStatus() {
        return tnBoardStatus == null ? (tnBoardStatus = computeTnBoardStatus()) : tnBoardStatus;
    }

    public BoardStatus getRvBoardStatus() {
        return rvBoardStatus == null ? (rvBoardStatus = computeRvBoardStatus()) : rvBoardStatus;
    }

    private synchronized BoardStatus computeFlBoardStatus() {
        return BoardEvaluator.eval(flCards);
    }

    private synchronized BoardStatus computeTnBoardStatus() {
        return BoardEvaluator.eval(flCards.plus(tnCard));
    }

    private synchronized BoardStatus computeRvBoardStatus() {
        return BoardEvaluator.eval(flCards.plus(tnCard, rvCard));
    }

    public double getFlAvgStack() {
        return flAvgStack;
    }

    public double getTnAvgStack() {
        return tnAvgStack;
    }

    public double getRvAvgStack() {
        return rvAvgStack;
    }

    public double getFlInitialPotSize() {
        return flInitialPotSize;
    }

    public double getTnInitialPotSize() {
        return tnInitialPotSize;
    }

    public double getRvInitialPotSize() {
        return rvInitialPotSize;
    }

    public double getTotalInitialStack() {
        return totalInitialStack;
    }

    public PokerGame(EnumMap<PreflopPosition, PokerPlayer> playersByPosition,
                     String handId,
                     String handDate,
                     int sbAmount,
                     int bbAmount,
                     CompetitionType competitionType,
                     String comment) {
        this.handDate = handDate;
        this.bbAmount = bbAmount;
        this.sbAmount = sbAmount;
        this.playersByPosition = playersByPosition;
        this.boardSize = BoardSize.of(playersByPosition.size());
        this.handId = handId;
        this.competitionType = competitionType;
        this.comment = comment;
        this.totalInitialStack = playersByPosition.values().stream()
                .mapToDouble(PokerPlayer::getInitialStack)
                .sum();

        this.decisionMakingPlayers.addAll(boardSize == BoardSize.HEADS_UP ? Arrays.stream(boardSize.preflopPositions()).map(playersByPosition::get).toList() :
                Arrays.stream(PreflopPosition.values()).map(playersByPosition::get).filter(Objects::nonNull).toList());
        this.allPlayersFilteringFolded.addAll(decisionMakingPlayers);

        setState(PokerSituation.BEGIN);
        if (boardSize == BoardSize.HEADS_UP) {
            nextPlayer = playersByPosition.get(PreflopPosition.SB);
        } else {
            nextPlayer = decisionMakingPlayers.get(2);
        }
        playersByPosition.get(PreflopPosition.SB).takeAmount(this.sbAmount);
        playersByPosition.get(PreflopPosition.BB).takeAmount(bbAmount);
        potSize += this.sbAmount + bbAmount;
        totalBet = bbAmount;
    }

    public static Optional<PokerGame> create(PokerHand pokerHand, Function<String, OpponentType> opponentTypeResolver) {
        try {
            EnumMap<PreflopPosition, PokerPlayer> playersByPosition = pokerHand.playerInitialStates().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            entry -> {
                                PlayerInitialState playerInitialState = entry.getValue();
                                String playerName = playerInitialState.playerName();
                                PreflopPosition preflopPosition = entry.getKey();
                                CardPair pocketCards = playerInitialState.pocketCards();
                                return pocketCards == null ?
                                        new PokerPlayer(playerInitialState.initialStack(), preflopPosition, playerName, opponentTypeResolver.apply(playerName)) :
                                        new PokerPlayer(pocketCards, playerInitialState.initialStack(), preflopPosition, playerName, opponentTypeResolver.apply(playerName));
                            },
                            (o, o2) -> o,
                            () -> new EnumMap<>(PreflopPosition.class)));
            return Optional.of(new PokerGame(playersByPosition,
                    pokerHand.handId(),
                    pokerHand.handDate(),
                    pokerHand.sbAmount(),
                    pokerHand.bbAmount(),
                    pokerHand.competitionType(),
                    pokerHand.comment()));
        } catch (Exception e) {
            logger.severe("Poker game creation error. Cause message: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Decision pushMove(Move move) {
        PreflopPosition movePosition = move.preflopPosition();
        currentPlayer = playersByPosition.get(movePosition);
        int stackStub = currentPlayer.getStackStub();
        Decision decision = move.decision();
        Action action = decision.action();
        int amount = decision.amount();
        Street currentStreet = state.getStreet();
        List<MoveStatistics> moveStats = movesByStreet.get(currentStreet);
        int callAmount = currentPlayer.computeCallAmount(totalBet);
        int movesCountIdx = moveStats.size();
        int idxPosition = (int) moveStats.stream().filter(finishedMoveStat -> finishedMoveStat.preflopPosition() == movePosition).count();
        int idxDecisionType = (int) moveStats.stream().filter(finishedMoveStat -> finishedMoveStat.decision().action() == action).count();
        int diffAmount = amount - callAmount;
        int totalInvestedAmount = currentPlayer.getTotalInvestedAmount();
        Branch outBranch = Branch.fromDecision(decision, potSize);
        Branch inBranch = moveStats.isEmpty() ? Branch.NONE : moveStats.get(moveStats.size() - 1).outBranch();
        potSize += amount;
        MoveStatistics moveStatistics = new MoveStatistics(
                movesCountIdx,
                idxPosition,
                idxDecisionType,
                movePosition,
                decision,
                outBranch,
                inBranch,
                currentStreet == Street.PF ? null : getBoardStatus(),
                state,
                callAmount,
                diffAmount,
                stackStub,
                potSize);
        moveStats.add(moveStatistics);
        currentPlayer.addMoveStatistics(currentStreet, moveStatistics);
        currentPlayer.takeAmount(amount);
        if (action == Action.C) {
            uncalledBet = 0;
        } else if (action == Action.R) {
            if (callAmount == 0) { // BET
                uncalledBet = diffAmount;
            } else { // RAISE
                uncalledBet = diffAmount;
            }
            totalBet = amount + totalInvestedAmount;
        } else if (action == Action.A) {
            if (diffAmount <= 0) { // CALL_ALLIN
                uncalledBet = Math.min(uncalledBet, -diffAmount);
            } else if (callAmount == 0) { // BET_ALLIN
                uncalledBet = diffAmount;
                totalBet = amount + totalInvestedAmount;
            } else { // RAISE_ALLIN
                uncalledBet = diffAmount;
                totalBet = amount + totalInvestedAmount;
            }
        }
        if (action == Action.A) {
            currentPlayer.setPokerPlayerState(PokerPlayerState.ALLIN);
            decisionMakingPlayers.remove(currentPlayer);
            allinPlayers.add(currentPlayer);
        } else if (action == Action.F) {
            currentPlayer.setPokerPlayerState(PokerPlayerState.FOLDED);
            decisionMakingPlayers.remove(currentPlayer);
            allPlayersFilteringFolded.remove(currentPlayer);
        }
        PreflopPosition currentPlayerPreflopPosition = currentPlayer.getPreflopPosition();
        if ((decisionMakingPlayers.size() <= 1 && allinPlayers.isEmpty()) || (decisionMakingPlayers.size() == 0)) {
            nextPlayer = null;
        } else if (boardSize == BoardSize.HEADS_UP) {
            nextPlayer = decisionMakingPlayers.stream()
                    .dropWhile(pokerPlayer -> pokerPlayer.getPreflopPosition() == currentPlayerPreflopPosition)
                    .findFirst().orElse((playersByPosition.get(PreflopPosition.BB)));
        } else {
            nextPlayer = decisionMakingPlayers.stream()
                    .dropWhile(pokerPlayer -> pokerPlayer.getPreflopPosition().isBeforeOrEquals(currentPlayerPreflopPosition))
                    .findFirst().orElse(decisionMakingPlayers.get(0));
        }
        return decision;
    }

    public int computeCallAmount(PreflopPosition preflopPosition) {
        return playersByPosition.get(preflopPosition).computeCallAmount(totalBet);
    }

    public void calculateAndDistributeCollectedAmounts() {
        try {
            if (allPlayersFilteringFolded.size() == 1) {
                if (uncalledBet != 0) {
                    allPlayersFilteringFolded.get(0).addCollectedAmount(potSize - uncalledBet);
                    allPlayersFilteringFolded.get(0).takeAmount(-uncalledBet);
                } else {
                    allPlayersFilteringFolded.get(0).addCollectedAmount(potSize);
                }
            } else {
                //Showdown
                if (uncalledBet != 0) {
                    PokerPlayer pokerPlayer = allPlayersFilteringFolded.stream().max(Comparator.comparingLong(PokerPlayer::getTotalInvestedAmount)).orElseThrow();
//                    pokerPlayer.addCollectedAmount(-uncalledBet);
//                    pokerPlayer.takeAmount(-uncalledBet);
                }
                Map<PreflopPosition, Integer> winnerStrengthByPosition = allPlayersFilteringFolded.stream()
                        .collect(Collectors.toMap(PokerPlayer::getPreflopPosition, player -> {
                            if (player.isPocketCardsOpen()) {
                                Holding playerRvHolding = player.getRvHolding();
                                return (int) (playerRvHolding.comboStatus().comboDescription().normalizedNumeric() * 100000);
                            } else {
                                return 0;
                            }
                        }));
                calculateAndDistributeCollectedAmounts(preflopPosition -> winnerStrengthByPosition.getOrDefault(preflopPosition, 0));
            }
            playersByPosition.values().forEach(PokerPlayer::calculateProfit);
        } catch (Exception e) {
            this.setState(PokerSituation.GAME_BROKEN);
            logger.log(Level.SEVERE, e, () -> "Couldn't calculateAndDistributeCollectedAmounts, pg=" + this);
        }
    }

    private void calculateAndDistributeCollectedAmounts(Function<PreflopPosition, Integer> winnerStrengthByPosition) {
        List<PokerPlayer> pokerPlayers = playersByPosition.values().stream().filter(pokerPlayer -> pokerPlayer.getTotalInvestedAmount() != 0).toList();
        int[] investments = pokerPlayers.stream().map(PokerPlayer::getTotalInvestedAmount)
                .distinct()
                .sorted()
                .mapToInt(Integer::intValue)
                .toArray();
        Map<Integer, List<PokerPlayer>> sidePotMap = new HashMap<>();
        int minInvestmentsPot = investments[0];
        List<PokerPlayer> minInvestmnetsPotPlayers = pokerPlayers.stream()
                .filter(pokerPlayer -> pokerPlayer.getTotalInvestedAmount() >= minInvestmentsPot)
                .toList();
        sidePotMap.put(minInvestmentsPot, minInvestmnetsPotPlayers);
        for (int i = 1; i < investments.length; i++) {
            int prevInvestment = investments[i - 1];
            int investment = investments[i];
            List<PokerPlayer> sidePotPlayers = pokerPlayers.stream()
                    .filter(pokerPlayer -> pokerPlayer.getTotalInvestedAmount() >= investment)
                    .toList();
            int diff = investment - prevInvestment;
            sidePotMap.put(diff, sidePotPlayers);
        }
        sidePotMap.forEach((key, sidePotPlayers) -> {
            int sidePot = key * sidePotPlayers.size();
            Map<Integer, List<PokerPlayer>> byStrength = sidePotPlayers.stream()
                    .collect(Collectors.groupingBy(pokerPlayer -> winnerStrengthByPosition.apply(pokerPlayer.getPreflopPosition())));
            int maxStrength = Collections.max(byStrength.keySet());
            List<PokerPlayer> sidePotWinners = byStrength.get(maxStrength);
            int toCollect = (int) ((double) sidePot / sidePotWinners.size());
            sidePotWinners.forEach(pokerPlayer -> pokerPlayer.addCollectedAmount(toCollect));
        });
    }

    @Override
    public void setState(PokerSituation state) {
        this.state = state;
    }

    @Override
    public PokerSituation getState() {
        return state;
    }


    public void forEachDecisionMakingPlayer(Consumer<PokerPlayer> pokerPlayerConsumer) {
        decisionMakingPlayers.forEach(pokerPlayerConsumer);
    }

    public void forEachPlayerFilteringFolded(Consumer<PokerPlayer> pokerPlayerConsumer) {
        allPlayersFilteringFolded.forEach(pokerPlayerConsumer);
    }

    public void forEachDecisionMakingPlayerExceptHero(Consumer<PokerPlayer> pokerPlayerConsumer) {
        PreflopPosition preflopPosition = nextPlayer.getPreflopPosition();
        decisionMakingPlayers.stream().filter(pokerPlayer -> pokerPlayer.getPreflopPosition() != preflopPosition).forEach(pokerPlayerConsumer);
    }

    public BoardSize getBoardSize() {
        return boardSize;
    }

    public PokerPlayer getNextPlayerOrThrow() {
        return Optional.ofNullable(nextPlayer).orElseThrow(() -> new RuntimeException("Cannot get next player. pokerGame=" + this));
    }

    public List<MoveStatistics> getPfMoveStats() {
        return movesByStreet.get(Street.PF);
    }

    public List<MoveStatistics> getFlMoveStats() {
        return movesByStreet.get(Street.FL);
    }

    public List<MoveStatistics> getTnMoveStats() {
        return movesByStreet.get(Street.TN);
    }

    public List<MoveStatistics> getRvMoveStats() {
        return movesByStreet.get(Street.RV);
    }


    public PfPotType getPfPotType() {
        return pfPotType;
    }

    public PostflopPotType getFlPotType() {
        return flPotType;
    }

    public PostflopPotType getTnPotType() {
        return tnPotType;
    }

    public List<PokerPlayer> getPlayers() {
        return decisionMakingPlayers;
    }

    public Collection<PokerPlayer> getAllPlayers() {
        return playersByPosition.values();
    }

    public void setFlCards(Cards flCards) {
        this.flCards = flCards;
    }

    public void setTnCard(Card tnCard) {
        this.tnCard = tnCard;
    }

    public void setRvCard(Card rvCard) {
        this.rvCard = rvCard;
    }

    void computePfCompletedFeatures() {
        flInitialPotSize = potSize;
        flAvgStack = computeAvgStack();
        List<MoveStatistics> pfMoveStats = getPfMoveStats();
        Action firstActionDecisionType = pfMoveStats.stream()
                .filter(move -> move.action() != Action.F)
                .filter(move -> move.idx() > 1)
                .map(MoveStatistics::action).findFirst().orElse(Action.C);
        pfPotType = switch (firstActionDecisionType) {
            case C -> {
                if (pfMoveStats.size() == boardSize.size() - 1) {
                    yield PfPotType.LIMP;
                } else {
                    ActionCountGroup raiseCount = ActionCountGroup.ofInteger((int) pfMoveStats.stream().filter(move -> move.action() == Action.R).count());
                    ActionCountGroup aiCount = ActionCountGroup.ofInteger((int) pfMoveStats.stream().filter(move -> move.action() == Action.A).count());
                    if (aiCount != ActionCountGroup.NONE) {
                        yield PfPotType.AIP;
                    } else
                        yield switch (raiseCount) {
                            case NONE -> PfPotType.LIMP;
                            case ONE -> PfPotType.ISO;
                            case TWO, THREE_PLUS -> PfPotType.MRP;
                        };
                }
            }
            case R -> {
                ActionCountGroup raiseCount = ActionCountGroup.ofInteger((int) pfMoveStats.stream().filter(move -> move.action() == Action.R).count());
                ActionCountGroup aiCount = ActionCountGroup.ofInteger((int) pfMoveStats.stream().filter(move -> move.action() == Action.A).count());
                if (aiCount != ActionCountGroup.NONE) {
                    yield PfPotType.AIP;
                } else
                    yield switch (raiseCount) {
                        case ONE -> PfPotType.SRP;
                        case TWO, THREE_PLUS -> PfPotType.MRP;
                        default -> throw new IllegalStateException("Unexpected value: " + firstActionDecisionType + ", pokerGame=" + this);
                    };
            }
            case A -> PfPotType.AIP;
            default -> throw new IllegalStateException("Unexpected value: " + firstActionDecisionType + ", pokerGame=" + this);
        };
    }


    void computeFlCompletedFeatures() {
        tnInitialPotSize = potSize;
        tnAvgStack = computeAvgStack();
        List<MoveStatistics> flMoveStats = getFlMoveStats();
        ActionCountGroup allinCount = ActionCountGroup.ofInteger((int) flMoveStats.stream().filter(move -> move.action() == Action.A).count());
        if (allinCount != ActionCountGroup.NONE) {
            flPotType = PostflopPotType.AIP;
        } else {
            ActionCountGroup raiseCount = ActionCountGroup.ofInteger((int) flMoveStats.stream().filter(move -> move.action() == Action.A).count());
            if (raiseCount == ActionCountGroup.NONE) {
                flPotType = PostflopPotType.MXP;
            } else if (raiseCount == ActionCountGroup.ONE) {
                flPotType = PostflopPotType.SRP;
            } else {
                flPotType = PostflopPotType.MRP;
            }
        }
    }

    void computeTnCompletedFeatures() {
        rvInitialPotSize = potSize;
        rvAvgStack = computeAvgStack();
        List<MoveStatistics> tnMoveStats = getTnMoveStats();
        ActionCountGroup allinCount = ActionCountGroup.ofInteger((int) tnMoveStats.stream().filter(move -> move.action() == Action.A).count());
        if (allinCount != ActionCountGroup.NONE) {
            tnPotType = PostflopPotType.AIP;
        } else {
            ActionCountGroup raiseCount = ActionCountGroup.ofInteger((int) tnMoveStats.stream().filter(move -> move.action() == Action.A).count());
            if (raiseCount == ActionCountGroup.NONE) {
                tnPotType = PostflopPotType.MXP;
            } else if (raiseCount == ActionCountGroup.ONE) {
                tnPotType = PostflopPotType.SRP;
            } else {
                tnPotType = PostflopPotType.MRP;
            }
        }
    }


    public HeroActionType computePfHeroActionType(PreflopPosition position) {
        List<MoveStatistics> pfMoveStats = getPfMoveStats();
        if (pfMoveStats.isEmpty()) {
            return HeroActionType.OTHER;
        }
        String actions = pfMoveStats.stream()
                .filter(move -> move.idx() > 1)
                .filter(move -> move.preflopPosition() == position)
                .map(move -> move.action().letter())
                .collect(Collectors.joining());
        if (actions.length() > 2) {
            actions = actions.substring(0, 2);
        }
        HeroActionType heroActionType;
        try {
            heroActionType = HeroActionType.valueOf(actions);
        } catch (IllegalArgumentException e) {
            heroActionType = HeroActionType.OTHER;
        }
        return heroActionType;
    }

    public HeroActionType computeFlHeroActionType(PreflopPosition position) {
        List<MoveStatistics> flMoveStats = getFlMoveStats();
        if (flMoveStats.isEmpty()) {
            return HeroActionType.OTHER;
        }
        String actions = flMoveStats.stream()
                .filter(move -> move.preflopPosition() == position)
                .map(move -> move.action().letter())
                .collect(Collectors.joining());
        if (actions.length() > 2) {
            actions = actions.substring(0, 2);
        }
        HeroActionType heroActionType;
        try {
            heroActionType = HeroActionType.valueOf(actions);
        } catch (IllegalArgumentException e) {
            heroActionType = HeroActionType.OTHER;
        }
        return heroActionType;
    }

    public HeroActionType computeTnHeroActionType(PreflopPosition position) {
        List<MoveStatistics> tnMoveStats = getTnMoveStats();
        if (tnMoveStats.isEmpty()) {
            return HeroActionType.OTHER;
        }
        String actions = tnMoveStats.stream()
                .filter(move -> move.preflopPosition() == position)
                .map(move -> move.action().letter())
                .collect(Collectors.joining());
        if (actions.length() > 2) {
            actions = actions.substring(0, 2);
        }
        HeroActionType heroActionType;
        try {
            heroActionType = HeroActionType.valueOf(actions);
        } catch (IllegalArgumentException e) {
            heroActionType = HeroActionType.OTHER;
        }
        return heroActionType;
    }

    public HeroActionType computeRvHeroActionType(PreflopPosition position) {
        List<MoveStatistics> rvMoveStats = getRvMoveStats();
        if (rvMoveStats.isEmpty()) {
            return HeroActionType.OTHER;
        }
        String actions = rvMoveStats.stream()
                .filter(move -> move.preflopPosition() == position)
                .map(move -> move.action().letter())
                .collect(Collectors.joining());
        if (actions.length() > 2) {
            actions = actions.substring(0, 2);
        }
        HeroActionType heroActionType;
        try {
            heroActionType = HeroActionType.valueOf(actions);
        } catch (IllegalArgumentException e) {
            heroActionType = HeroActionType.OTHER;
        }
        return heroActionType;
    }

    public double computeAvgStack() {
        return decisionMakingPlayers.isEmpty() ? 0. : decisionMakingPlayers.stream()
                .mapToDouble(PokerPlayer::getStackStub)
                .average().orElseThrow(() -> new RuntimeException("Couldn't compute avgStack for pokerGame=" + this));
    }

    public void updateNextPlayer() {
        nextPlayer = !decisionMakingPlayers.isEmpty() ? decisionMakingPlayers.get(0) : null;
    }


    public boolean isCompleted() {
        int decisionMakingPlayersCount = decisionMakingPlayers.size();
        int allinPlayersCount = allinPlayers.size();
        return (decisionMakingPlayersCount == 1 && allinPlayersCount == 0) || (decisionMakingPlayersCount == 0);
    }

    public String getHandId() {
        return handId;
    }

    public String getHandDate() {
        return handDate;
    }

    public int getBbAmount() {
        return bbAmount;
    }


    public List<MoveStatistics> getMoveStatistics() {
        return movesByStreet.get(state.getStreet());
    }

    public MoveStatistics lastMoveStatistics() {
        List<MoveStatistics> moveStatistics = getMoveStatistics();
        return moveStatistics.get(moveStatistics.size() - 1);
    }


    public boolean isValid() {
        int pfMovesSize = getPfMoveStats().size();
        MoveStatistics moveStat = getPfMoveStats().get(pfMovesSize - 1);
        if (getPfMoveStats().stream().anyMatch(moveStatistics -> moveStatistics.decision().amount() < 0)) {
            return false;
        }
        if (getFlMoveStats().stream().anyMatch(moveStatistics -> moveStatistics.decision().amount() < 0)) {
            return false;
        }
        if (getTnMoveStats().stream().anyMatch(moveStatistics -> moveStatistics.decision().amount() < 0)) {
            return false;
        }
        if (getRvMoveStats().stream().anyMatch(moveStatistics -> moveStatistics.decision().amount() < 0)) {
            return false;
        }
        if (playersByPosition.values().stream().allMatch(pokerPlayer -> pokerPlayer.getProfit() < 0)) {
            return false;
        }
        boolean isValid =
                bbAmount > 0 &&
                        totalBet > 0 &&
                        potSize > 0 &&
                        getBoardSize().size() >= 2 &&
                        totalInitialStack > 0 &&
                        playersByPosition.values().stream().allMatch(pokerPlayer -> pokerPlayer.getInitialStack() > 0 &&
                                pokerPlayer.getTotalInvestedAmount() >= 0 &&
                                pokerPlayer.getTotalCollectedAmount() >= 0);
        if (!isValid) {
            return false;
        } else {
            int flSize = getFlMoveStats().size();
            int tnSize = getTnMoveStats().size();
            int rvSize = getRvMoveStats().size();
            if ((flSize == 0 || tnSize == 0) && rvSize != 0) {
                return false;
            } else return flSize != 0 || tnSize == 0;
        }
    }

    public long calculateCallersCount() {
        return decisionMakingPlayers.stream().filter(p -> p.getTotalInvestedAmount() < totalBet).count();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("\nPLAYERS: \n\t")
                .append(playersByPosition.values().stream()
                        .map(PokerPlayer::toString)
                        .collect(Collectors.joining("\n\t")))
                .append("\n");
        if (flCards != null) {
            sb
                    .append("BOARD: ")
                    .append(flCards)
                    .append(tnCard == null ? "" : "[" + tnCard + "]")
                    .append(rvCard == null ? "" : "[" + rvCard + "]")
                    .append("\n");
        }
        List<MoveStatistics> pfMoveStats = getPfMoveStats();
        if (!pfMoveStats.isEmpty()) {
            sb.append("PF: ")
                    .append("SB(").append(sbAmount).append("), BB(").append(bbAmount).append("), ")
                    .append(pfMoveStats.stream()
                            .map(MoveStatistics::toString)
                            .collect(Collectors.joining(", ")));
        }
        List<MoveStatistics> flMoveStats = getFlMoveStats();
        if (!flMoveStats.isEmpty()) {
            sb
                    .append("\n")
                    .append("FL: ")
                    .append(flMoveStats.stream()
                            .map(MoveStatistics::toString)
                            .collect(Collectors.joining(", ")));
        }
        List<MoveStatistics> tnMoveStats = getTnMoveStats();
        if (!tnMoveStats.isEmpty()) {
            sb
                    .append("\n")
                    .append("TN: ")
                    .append(tnMoveStats.stream()
                            .map(MoveStatistics::toString)
                            .collect(Collectors.joining(", ")));
        }
        List<MoveStatistics> rvMoveStats = getRvMoveStats();
        if (!rvMoveStats.isEmpty()) {
            sb
                    .append("\n")
                    .append("RV: ")
                    .append(rvMoveStats.stream()
                            .map(MoveStatistics::toString)
                            .collect(Collectors.joining(", ")));
        }
        return sb.toString();
    }

    public PreflopPosition getHeroPreflopPosition() {
        return getNextPlayerOrThrow().getPreflopPosition();
    }

    private final static String lineSeparator = System.lineSeparator();

    private final static SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm:ss");

    public String toHandHistory888(String heroName) {
        List<PokerPlayer> initialPlayers = boardSize == BoardSize.HEADS_UP ? Arrays.stream(boardSize.preflopPositions()).map(playersByPosition::get).toList() :
                Arrays.stream(PreflopPosition.values()).map(playersByPosition::get).filter(Objects::nonNull).toList();
        PokerPlayer hero = initialPlayers.stream().filter(pokerPlayer -> pokerPlayer.getPlayerName().equals(heroName)).findFirst().orElseThrow();
        StringBuilder sb = new StringBuilder();
        int id = Math.abs(handId.hashCode());
        sb.append("#Game No : ").append(id).append(lineSeparator);
        sb.append("***** 888poker Hand History for Game ").append(id).append(" *****").append(lineSeparator);
        long sbAmount = bbAmount / 2;
        sb.append("$").append(sbAmount).append("/").append("$").append(bbAmount).append(" Blinds No Limit Holdem - *** ").append(formatter.format(new Date())).append(lineSeparator);
        int tableSize = boardSize.size();
        sb.append("Table OuterSpace ").append(tableSize).append(" Max (Real Money)").append(lineSeparator);
        sb.append("Seat ").append(tableSize).append(" is the button").append(lineSeparator);
        sb.append("Total number of players : ").append(tableSize).append(lineSeparator);
        IntStream.range(0, initialPlayers.size()).forEach(idx -> {
            PokerPlayer pokerPlayer = initialPlayers.get(idx);
            sb.append("Seat ").append(idx + 1).append(": ").append(pokerPlayer.getPlayerName()).append(" ( $").append(pokerPlayer.getInitialStack()).append(" )").append(lineSeparator);
        });
        sb.append(playersByPosition.get(PreflopPosition.SB).getPlayerName()).append(" posts small blind [$").append(sbAmount).append("]").append(lineSeparator);
        sb.append(playersByPosition.get(PreflopPosition.BB).getPlayerName()).append(" posts big blind [$").append(bbAmount).append("]").append(lineSeparator);
        sb.append("** Dealing down cards **").append(lineSeparator);
        CardPair cardPair = hero.getCardPair();
        sb.append("Dealt to ").append(heroName).append(" [ ").append(cardPair.getHighCard()).append(", ").append(cardPair.getLowCard()).append(" ]").append(lineSeparator);

        getPfMoveStats().forEach(m -> {
            sb.append(playersByPosition.get(m.preflopPosition()).getPlayerName()).append(" ");
            appendMovesInfo(sb, m, Street.PF);
        });
        List<Card> flCardsList = flCards.asList();
        sb.append("** Dealing flop ** [ ").append(flCardsList.get(0)).append(", ").append(flCardsList.get(1)).append(", ").append(flCardsList.get(2)).append(" ]").append(lineSeparator);
        getFlMoveStats().forEach(m -> {
            sb.append(playersByPosition.get(m.preflopPosition()).getPlayerName()).append(" ");
            appendMovesInfo(sb, m, Street.FL);
        });
        sb.append("** Dealing turn ** [ ").append(tnCard).append(" ]").append(lineSeparator);
        getTnMoveStats().forEach(m -> {
            sb.append(playersByPosition.get(m.preflopPosition()).getPlayerName()).append(" ");
            appendMovesInfo(sb, m, Street.TN);
        });
        sb.append("** Dealing river ** [ ").append(rvCard).append(" ]").append(lineSeparator);
        getRvMoveStats().forEach(m -> {
            sb.append(playersByPosition.get(m.preflopPosition()).getPlayerName()).append(" ");
            appendMovesInfo(sb, m, Street.RV);
        });
        sb.append("** Summary **").append(lineSeparator);
        initialPlayers.stream().filter(PokerPlayer::isPocketCardsOpen).forEach(pokerPlayer -> {
            CardPair pc = pokerPlayer.getCardPair();
            sb.append(pokerPlayer.getPlayerName()).append(" shows ").append("[ ").append(pc.getHighCard()).append(", ").append(pc.getLowCard()).append(" ]").append(lineSeparator);
        });
        initialPlayers.stream().filter(pokerPlayer -> pokerPlayer.getProfit() > 0).forEach(pokerPlayer -> {
            long collected = pokerPlayer.getTotalCollectedAmount();
            sb.append(pokerPlayer.getPlayerName()).append(" collected [ $").append(collected).append(" ]").append(lineSeparator);
        });
        sb.append(lineSeparator).append(lineSeparator).append(lineSeparator);
        return sb.toString();
    }

    private void appendMovesInfo(StringBuilder sb, MoveStatistics moveStatistics, Street street) {
        double callAmount = moveStatistics.callAmount();
        double stackStub = moveStatistics.stackStub();
        switch (moveStatistics.action()) {
            case F -> {
                sb.append("folds").append(lineSeparator);
            }
            case R -> {
                if (street == Street.PF) {
                    sb.append("raises").append(" [$").append(moveStatistics.decision().amount()).append("]").append(lineSeparator);
                } else if (callAmount == 0) {
                    sb.append("bets").append(" [$").append(moveStatistics.decision().amount()).append("]").append(lineSeparator);
                } else {
                    sb.append("raises").append(" [$").append(moveStatistics.decision().amount()).append("]").append(lineSeparator);
                }
            }
            case A -> {
                if (stackStub <= callAmount) {
                    sb.append("calls").append(" [$").append(moveStatistics.decision().amount()).append("]").append(lineSeparator);
                } else if (street == Street.PF) {
                    sb.append("raises").append(" [$").append(moveStatistics.decision().amount()).append("]").append(lineSeparator);
                } else if (callAmount == 0) {
                    sb.append("bets").append(" [$").append(moveStatistics.decision().amount()).append("]").append(lineSeparator);
                } else {
                    sb.append("raises").append(" [$").append(moveStatistics.decision().amount()).append("]").append(lineSeparator);
                }
            }
            case C -> {
                if (callAmount == 0) {
                    sb.append("checks").append(lineSeparator);
                } else {
                    sb.append("calls").append(" [$").append(moveStatistics.decision().amount()).append("]").append(lineSeparator);
                }
            }
        }
    }


    private PokerGame(PokerGame from) {
        this.handDate = from.handDate;
        this.bbAmount = from.bbAmount;
        this.sbAmount = from.sbAmount;
        this.boardSize = from.boardSize;
        this.handId = from.handId;
        this.competitionType = from.competitionType;
        this.comment = from.comment;

        this.playersByPosition = new EnumMap<>(PreflopPosition.class);

        from.playersByPosition.forEach((preflopPosition, pokerPlayer) -> {
            PokerPlayer copy = pokerPlayer.copy();
            this.playersByPosition.put(preflopPosition, copy);
        });

        from.decisionMakingPlayers.forEach(pokerPlayer -> this.decisionMakingPlayers.add(this.playersByPosition.get(pokerPlayer.getPreflopPosition())));
        from.allinPlayers.forEach(pokerPlayer -> this.allinPlayers.add(this.playersByPosition.get(pokerPlayer.getPreflopPosition())));
        from.allPlayersFilteringFolded.forEach(pokerPlayer -> this.allPlayersFilteringFolded.add(this.playersByPosition.get(pokerPlayer.getPreflopPosition())));

        this.totalInitialStack = this.playersByPosition.values().stream()
                .mapToDouble(PokerPlayer::getInitialStack)
                .sum();

        from.movesByStreet.forEach((street, moveStatistics) -> this.movesByStreet.put(street, new ArrayList<>(moveStatistics)));

        this.state = from.state;
        this.totalBet = from.totalBet;
        this.uncalledBet = from.uncalledBet;
        this.potSize = from.potSize;
        this.nextPlayer = from.nextPlayer;
        this.currentPlayer = from.currentPlayer;
        this.pfPotType = from.pfPotType;
        this.flPotType = from.flPotType;
        this.tnPotType = from.tnPotType;
        this.flCards = from.flCards;
        this.tnCard = from.tnCard;
        this.rvCard = from.rvCard;
        this.flBoardStatus = from.flBoardStatus;
        this.tnBoardStatus = from.tnBoardStatus;
        this.rvBoardStatus = from.rvBoardStatus;
        this.flAvgStack = from.flAvgStack;
        this.tnAvgStack = from.tnAvgStack;
        this.rvAvgStack = from.rvAvgStack;
        this.flInitialPotSize = from.flInitialPotSize;
        this.tnInitialPotSize = from.tnInitialPotSize;
        this.rvInitialPotSize = from.rvInitialPotSize;
    }


    public PokerGame copy() {
        return new PokerGame(this);
    }

    public BoardStatus getBoardStatus() {
        return switch (state.getStreet()) {
            case FL -> flBoardStatus;
            case TN -> tnBoardStatus;
            case RV -> rvBoardStatus;
            default -> null;
        };
    }

    public EnumSet<Card> getBoardCards() {
        return switch (state.getStreet()) {
            case FL -> flCards.cards();
            case TN -> flCards.plus(tnCard).cards();
            case RV -> flCards.plus(tnCard).plus(rvCard).cards();
            default -> null;
        };
    }
}
