package dev.asper.poker.engine;

import dev.asper.clickhouse.OpponentType;
import dev.asper.common.feature.*;
import dev.asper.poker.card.Rank;
import dev.asper.poker.enums.*;
import dev.asper.poker.holding.*;

import java.util.EnumSet;
import java.util.List;

public enum PokerFeatures {
    ;

    // SERVICE FEATURES    
    public final static StringFeature<PokerGame> handId = new StringFeature<>(PokerFeatureName.HAND_ID, PokerGame::getHandId);
    public final static StringFeature<PokerGame> handDate = new StringFeature<>(PokerFeatureName.HAND_DATE, PokerGame::getHandDate);
    public final static StringFeature<PokerGame> pocketCards = new StringFeature<>(PokerFeatureName.POCKET_CARDS,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getCardPair().getStdName());
    public final static StringFeature<PokerGame> flCards = new StringFeature<>(PokerFeatureName.FL_CARDS, pokerGame -> pokerGame.getFlCards().asString());
    public final static StringFeature<PokerGame> tnCard = new StringFeature<>(PokerFeatureName.TN_CARD, pokerGame -> pokerGame.getTnCard().stdName());
    public final static StringFeature<PokerGame> rvCard = new StringFeature<>(PokerFeatureName.RV_CARD, pokerGame -> pokerGame.getRvCard().stdName());
    public final static CategoricalFeature<BoardSize, PokerGame> boardSize = new CategoricalFeature<>(PokerFeatureName.BOARD_SIZE, PokerGame::getBoardSize, EnumSet.allOf(BoardSize.class));
    public final static IntFeature<PokerGame> bbAmount = new IntFeature<>(PokerFeatureName.BB_AMOUNT, PokerGame::getBbAmount);
    public final static StringFeature<PokerGame> heroHand = new StringFeature<>(PokerFeatureName.HERO_HAND, pokerGame -> pokerGame.getState().getStreet() == Street.PF ?
            pokerGame.getNextPlayerOrThrow().getCardCell().stdName() :
            pokerGame.getNextPlayerOrThrow().getHolding(pokerGame.getState().getStreet()).comboStatus().comboExType().name());
    public final static CategoricalFeature<PreflopPosition, PokerGame> heroPosition = new CategoricalFeature<>(PokerFeatureName.HERO_POSITION,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getPreflopPosition().to6maxName(),
            EnumSet.allOf(PreflopPosition.class));
    public final static StringFeature<PokerGame> heroName = new StringFeature<>(PokerFeatureName.HERO_NAME,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getPlayerName());

    // LABEL FEATURES
    public final static CategoricalFeature<Action, PokerGame> heroAction = new CategoricalFeature<>(PokerFeatureName.HERO_ACTION,
            pokerGame -> pokerGame.lastMoveStatistics().action(),
            Action.allActions);
    public final static CategoricalFeature<Branch, PokerGame> heroBranch = new CategoricalFeature<>(PokerFeatureName.HERO_BRANCH,
            pokerGame -> pokerGame.lastMoveStatistics().outBranch(),
            EnumSet.allOf(Branch.class));
    public final static IntFeature<PokerGame> heroAmount = new IntFeature<>(PokerFeatureName.HERO_AMOUNT,
            pokerGame -> pokerGame.lastMoveStatistics().getAmount());
    public final static DoubleFeature<PokerGame> heroAmountBb = new DoubleFeature<>(PokerFeatureName.HERO_AMOUNT_BB,
            pokerGame -> (double) pokerGame.lastMoveStatistics().getAmount() / (double) pokerGame.getBbAmount());
    public final static DoubleFeature<PokerGame> heroAmountByPot = new DoubleFeature<>(PokerFeatureName.HERO_AMOUNT_BY_POT,
            pokerGame -> {
                MoveStatistics moveStatistics = pokerGame.lastMoveStatistics();
                return (double) moveStatistics.getAmount() / (double) moveStatistics.potSize();
            });
    public final static DoubleFeature<PokerGame> heroDiffAmountByPot = new DoubleFeature<>(PokerFeatureName.HERO_DIFF_AMOUNT_BY_POT,
            pokerGame -> {
                MoveStatistics moveStatistics = pokerGame.lastMoveStatistics();
                return (double) moveStatistics.diffAmount() / (double) moveStatistics.potSize();
            });
    public final static DoubleFeature<PokerGame> heroDiffAmountByStackStub = new DoubleFeature<>(PokerFeatureName.HERO_DIFF_AMOUNT_BY_STACK_STUB,
            pokerGame -> {
                MoveStatistics moveStatistics = pokerGame.lastMoveStatistics();
                return (double) moveStatistics.getAmount() / (double) moveStatistics.stackStub();
            });
    public final static DoubleFeature<PokerGame> heroDiffAmountBb = new DoubleFeature<>(PokerFeatureName.HERO_DIFF_AMOUNT_BB,
            pokerGame -> {
                MoveStatistics moveStatistics = pokerGame.lastMoveStatistics();
                return (double) moveStatistics.diffAmount() / (double) pokerGame.getBbAmount();
            });

    // REGULAR FEATURES    
    public final static CategoricalFeature<OpponentType, PokerGame> oppType = new CategoricalFeature<>(PokerFeatureName.OPP_TYPE,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getOpponentType(),
            EnumSet.allOf(OpponentType.class));
    public final static DoubleFeature<PokerGame> totalPotSizeBb = new DoubleFeature<>(PokerFeatureName.TOTAL_POT_SIZE_BB,
            pokerGame -> (double) pokerGame.getPotSize() / (double) pokerGame.getBbAmount());
    public final static CategoricalFeature<Branch, PokerGame> prevBranch = new CategoricalFeature<>(PokerFeatureName.PREV_BRANCH,
            pokerGame -> {
                List<MoveStatistics> moveStatistics = pokerGame.getMoveStatistics();
                return moveStatistics.isEmpty() ? Branch.NONE : moveStatistics.get(moveStatistics.size() - 1).outBranch();
            },
            EnumSet.allOf(Branch.class));
    public final static DoubleFeature<PokerGame> heroCallAmountByStackStub = new DoubleFeature<>(PokerFeatureName.HERO_CALL_AMOUNT_BY_STACK_STUB,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) pokerGame.computeCallAmount(hero.getPreflopPosition()) / (double) hero.getStackStub();
            });
    public final static DoubleFeature<PokerGame> heroCallAmountBb = new DoubleFeature<>(PokerFeatureName.HERO_CALL_AMOUNT_BB,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) pokerGame.computeCallAmount(hero.getPreflopPosition()) / (double) pokerGame.getBbAmount();
            });
    public final static DoubleFeature<PokerGame> heroOppsInvestedAmountByInitialStack = new DoubleFeature<>(PokerFeatureName.HERO_OPPS_INVESTED_AMOUNT_BY_INITIAL_STACK,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) (pokerGame.getPotSize() - hero.getTotalInvestedAmount()) / (double) hero.getInitialStack();
            });
    public final static DoubleFeature<PokerGame> heroOppsInvestedAmountBb = new DoubleFeature<>(PokerFeatureName.HERO_OPPS_INVESTED_AMOUNT_BB,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) (pokerGame.getPotSize() - hero.getTotalInvestedAmount()) / (double) pokerGame.getBbAmount();
            });
    public final static DoubleFeature<PokerGame> totalPotSizeByHeroInitialStack = new DoubleFeature<>(PokerFeatureName.TOTAL_POT_SIZE_BY_HERO_INITIAL_STACK,
            pokerGame -> (double) pokerGame.getPotSize() / (double) pokerGame.getNextPlayerOrThrow().getInitialStack());
    public final static DoubleFeature<PokerGame> heroInvestmentsByTotalPot = new DoubleFeature<>(PokerFeatureName.HERO_INVESTMENTS_BY_TOTAL_POT,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getTotalInvestedAmount() / (double) pokerGame.getPotSize();
            });
    public final static DoubleFeature<PokerGame> heroInvestmentsBb = new DoubleFeature<>(PokerFeatureName.HERO_INVESTMENTS_BB,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getTotalInvestedAmount() / (double) pokerGame.getBbAmount();
            });
    public final static DoubleFeature<PokerGame> heroStackStubByTotalPot = new DoubleFeature<>(PokerFeatureName.HERO_STACK_STUB_BY_TOTAL_POT,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getStackStub() / (double) pokerGame.getPotSize();
            });
    public final static DoubleFeature<PokerGame> heroStackStubBb = new DoubleFeature<>(PokerFeatureName.HERO_STACK_STUB_BB,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getStackStub() / (double) pokerGame.getBbAmount();
            });
    public final static DoubleFeature<PokerGame> heroStackStubByInitialStack = new DoubleFeature<>(PokerFeatureName.HERO_STACK_STUB_BY_INITIAL_STACK,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getStackStub() / (double) hero.getInitialStack();
            });
    public final static DoubleFeature<PokerGame> heroInitialStackBb = new DoubleFeature<>(PokerFeatureName.HERO_INITIAL_STACK_BB,
            pokerGame -> (double) pokerGame.getNextPlayerOrThrow().getInitialStack() / (double) pokerGame.getBbAmount());
    public final static DoubleFeature<PokerGame> maxStackStubBbExceptHero = new DoubleFeature<>(PokerFeatureName.MAX_EFF_STACK_BB_EXCEPT_HERO,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                PreflopPosition preflopPosition = hero.getPreflopPosition();
                int maxStackStubExceptHero = pokerGame.getPlayers().stream()
                        .filter(p -> p.getPreflopPosition() != preflopPosition)
                        .mapToInt(PokerPlayer::getStackStub)
                        .max().orElse(hero.getStackStub());
                return (double) maxStackStubExceptHero / (double) pokerGame.getBbAmount();
            });
    public final static CategoricalFeature<EffStackType, PokerGame> maxEffStackType = new CategoricalFeature<>(PokerFeatureName.MAX_EFF_STACK_TYPE,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                PreflopPosition preflopPosition = hero.getPreflopPosition();
                int maxStackStubExceptHero = pokerGame.getPlayers().stream()
                        .filter(p -> p.getPreflopPosition() != preflopPosition)
                        .mapToInt(PokerPlayer::getStackStub)
                        .max().orElse(hero.getStackStub());
                return EffStackType.computeRough((double) maxStackStubExceptHero / (double) pokerGame.getBbAmount());
            },
            EnumSet.allOf(EffStackType.class));
    public final static DoubleFeature<PokerGame> heroEffStackBb = new DoubleFeature<>(PokerFeatureName.HERO_EFF_STACK_BB,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getStackStub() / (double) pokerGame.getBbAmount();
            });
    public final static CategoricalFeature<EffStackType, PokerGame> heroEffStackType = new CategoricalFeature<>(PokerFeatureName.HERO_EFF_STACK_TYPE,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return EffStackType.computeRough((double) hero.getStackStub() / (double) pokerGame.getBbAmount());
            },
            EnumSet.allOf(EffStackType.class));

    public final static DoubleFeature<PokerGame> heroStackStubByFlAvgStack = new DoubleFeature<>(PokerFeatureName.HERO_STACK_STUB_BY_FL_AVG_STACK,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getStackStub() / pokerGame.getFlAvgStack();
            });
    public final static DoubleFeature<PokerGame> heroStackStubByTnAvgStack = new DoubleFeature<>(PokerFeatureName.HERO_STACK_STUB_BY_TN_AVG_STACK,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getStackStub() / pokerGame.getTnAvgStack();
            });
    public final static DoubleFeature<PokerGame> heroStackStubByRvAvgStack = new DoubleFeature<>(PokerFeatureName.HERO_STACK_STUB_BY_RV_AVG_STACK,
            pokerGame -> {
                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                return (double) hero.getStackStub() / pokerGame.getRvAvgStack();
            });

    public final static DoubleFeature<PokerGame> flInitialPotSizeByTotalInitialStack = new DoubleFeature<>(PokerFeatureName.FL_INITIAL_POT_SIZE_BY_TOTAL_INITIAL_STACK,
            pokerGame -> pokerGame.getFlInitialPotSize() / pokerGame.getTotalInitialStack());
    public final static DoubleFeature<PokerGame> tnInitialPotSizeByTotalInitialStack = new DoubleFeature<>(PokerFeatureName.TN_INITIAL_POT_SIZE_BY_TOTAL_INITIAL_STACK,
            pokerGame -> pokerGame.getTnInitialPotSize() / pokerGame.getTotalInitialStack());
    public final static DoubleFeature<PokerGame> rvInitialPotSizeByTotalInitialStack = new DoubleFeature<>(PokerFeatureName.RV_INITIAL_POT_SIZE_BY_TOTAL_INITIAL_STACK,
            pokerGame -> pokerGame.getRvInitialPotSize() / pokerGame.getTotalInitialStack());
    public final static DoubleFeature<PokerGame> flInitialPotSizeByTnInitialPotSize = new DoubleFeature<>(PokerFeatureName.FL_INITIAL_POT_SIZE_BY_TN_INITIAL_POT_SIZE,
            pokerGame -> pokerGame.getFlInitialPotSize() / pokerGame.getTnInitialPotSize());
    public final static DoubleFeature<PokerGame> tnInitialPotSizeByRvInitialPotSize = new DoubleFeature<>(PokerFeatureName.TN_INITIAL_POT_SIZE_BY_RV_INITIAL_POT_SIZE,
            pokerGame -> pokerGame.getTnInitialPotSize() / pokerGame.getRvInitialPotSize());

    public final static StringFeature<PokerGame> pocketCardsName = new StringFeature<>(PokerFeatureName.POCKET_CARDS_NAME,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getCardCell().stdName());
    public final static DoubleFeature<PokerGame> pocketCardsNameNum = new DoubleFeature<>(PokerFeatureName.POCKET_CARDS_NAME_NUM,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getCardCell().normalizedNumeric());

    public final static CategoricalFeature<BoardFlush, PokerGame> flHoldingBoardFlush = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_BOARD_FLUSH,
            pokerGame -> pokerGame.getFlBoardStatus().boardFlush(),
            EnumSet.allOf(BoardFlush.class));
    public final static CategoricalFeature<BoardStraight, PokerGame> flHoldingBoardStraight = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_BOARD_STRAIGHT,
            pokerGame -> pokerGame.getFlBoardStatus().boardStraight(),
            EnumSet.allOf(BoardStraight.class));
    public final static CategoricalFeature<BoardPair, PokerGame> flHoldingBoardPair = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_BOARD_PAIR,
            pokerGame -> pokerGame.getFlBoardStatus().boardPair(),
            EnumSet.allOf(BoardPair.class));

    public final static CategoricalFeature<BoardFlush, PokerGame> tnHoldingBoardFlush = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_BOARD_FLUSH,
            pokerGame -> pokerGame.getTnBoardStatus().boardFlush(),
            EnumSet.allOf(BoardFlush.class));
    public final static CategoricalFeature<BoardStraight, PokerGame> tnHoldingBoardStraight = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_BOARD_STRAIGHT,
            pokerGame -> pokerGame.getTnBoardStatus().boardStraight(),
            EnumSet.allOf(BoardStraight.class));
    public final static CategoricalFeature<BoardPair, PokerGame> tnHoldingBoardPair = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_BOARD_PAIR,
            pokerGame -> pokerGame.getTnBoardStatus().boardPair(),
            EnumSet.allOf(BoardPair.class));

    public final static CategoricalFeature<BoardFlush, PokerGame> rvHoldingBoardFlush = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_BOARD_FLUSH,
            pokerGame -> pokerGame.getRvBoardStatus().boardFlush(),
            EnumSet.allOf(BoardFlush.class));
    public final static CategoricalFeature<BoardStraight, PokerGame> rvHoldingBoardStraight = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_BOARD_STRAIGHT,
            pokerGame -> pokerGame.getRvBoardStatus().boardStraight(),
            EnumSet.allOf(BoardStraight.class));
    public final static CategoricalFeature<BoardPair, PokerGame> rvHoldingBoardPair = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_BOARD_PAIR,
            pokerGame -> pokerGame.getRvBoardStatus().boardPair(),
            EnumSet.allOf(BoardPair.class));


    public final static CategoricalFeature<Flush, PokerGame> flHoldingFlush = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_FLUSH,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().flushStatus().flush(),
            EnumSet.allOf(Flush.class));
    public final static CategoricalFeature<Handicap, PokerGame> flHoldingFlushHandicap = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_FLUSH_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().flushStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> flHoldingFlushRank = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_FLUSH_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().flushStatus().rank(),
            EnumSet.allOf(Rank.class));
    public final static CategoricalFeature<Straight, PokerGame> flHoldingStraight = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_STRAIGHT,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().straightStatus().straight(),
            EnumSet.allOf(Straight.class));
    public final static CategoricalFeature<Handicap, PokerGame> flHoldingStraightHandicap = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_STRAIGHT_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().straightStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> flHoldingStraightRank = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_STRAIGHT_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().straightStatus().rank(),
            EnumSet.allOf(Rank.class));
    public final static CategoricalFeature<ComboExType, PokerGame> flHoldingComboExType = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_COMBO_EX_TYPE,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().comboStatus().comboExType(),
            EnumSet.allOf(ComboExType.class));
    public final static CategoricalFeature<Handicap, PokerGame> flHoldingComboHandicap = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_COMBO_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().comboStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> flHoldingComboRank = new CategoricalFeature<>(PokerFeatureName.FL_HOLDING_COMBO_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getFlHolding().comboStatus().comboRank(),
            EnumSet.allOf(Rank.class));


    public final static CategoricalFeature<Flush, PokerGame> tnHoldingFlush = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_FLUSH,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().flushStatus().flush(),
            EnumSet.allOf(Flush.class));
    public final static CategoricalFeature<Handicap, PokerGame> tnHoldingFlushHandicap = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_FLUSH_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().flushStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> tnHoldingFlushRank = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_FLUSH_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().flushStatus().rank(),
            EnumSet.allOf(Rank.class));
    public final static CategoricalFeature<Straight, PokerGame> tnHoldingStraight = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_STRAIGHT,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().straightStatus().straight(),
            EnumSet.allOf(Straight.class));
    public final static CategoricalFeature<Handicap, PokerGame> tnHoldingStraightHandicap = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_STRAIGHT_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().straightStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> tnHoldingStraightRank = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_STRAIGHT_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().straightStatus().rank(),
            EnumSet.allOf(Rank.class));
    public final static CategoricalFeature<ComboExType, PokerGame> tnHoldingComboExType = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_COMBO_EX_TYPE,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().comboStatus().comboExType(),
            EnumSet.allOf(ComboExType.class));
    public final static CategoricalFeature<Handicap, PokerGame> tnHoldingComboHandicap = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_COMBO_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().comboStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> tnHoldingComboRank = new CategoricalFeature<>(PokerFeatureName.TN_HOLDING_COMBO_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getTnHolding().comboStatus().comboRank(),
            EnumSet.allOf(Rank.class));


    public final static CategoricalFeature<Flush, PokerGame> rvHoldingFlush = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_FLUSH,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().flushStatus().flush(),
            EnumSet.allOf(Flush.class));
    public final static CategoricalFeature<Handicap, PokerGame> rvHoldingFlushHandicap = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_FLUSH_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().flushStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> rvHoldingFlushRank = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_FLUSH_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().flushStatus().rank(),
            EnumSet.allOf(Rank.class));
    public final static CategoricalFeature<Straight, PokerGame> rvHoldingStraight = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_STRAIGHT,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().straightStatus().straight(),
            EnumSet.allOf(Straight.class));
    public final static CategoricalFeature<Handicap, PokerGame> rvHoldingStraightHandicap = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_STRAIGHT_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().straightStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> rvHoldingStraightRank = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_STRAIGHT_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().straightStatus().rank(),
            EnumSet.allOf(Rank.class));
    public final static CategoricalFeature<ComboExType, PokerGame> rvHoldingComboExType = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_COMBO_EX_TYPE,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().comboStatus().comboExType(),
            EnumSet.allOf(ComboExType.class));
    public final static CategoricalFeature<Handicap, PokerGame> rvHoldingComboHandicap = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_COMBO_HANDICAP,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().comboStatus().handicap(),
            EnumSet.allOf(Handicap.class));
    public final static CategoricalFeature<Rank, PokerGame> rvHoldingComboRank = new CategoricalFeature<>(PokerFeatureName.RV_HOLDING_COMBO_RANK,
            pokerGame -> pokerGame.getNextPlayerOrThrow().getRvHolding().comboStatus().comboRank(),
            EnumSet.allOf(Rank.class));

    public final static CategoricalFeature<PfPotType, PokerGame> pfPotType = new CategoricalFeature<>(PokerFeatureName.PF_POT_TYPE,
            PokerGame::getPfPotType,
            EnumSet.allOf(PfPotType.class));
    public final static CategoricalFeature<PostflopPotType, PokerGame> flPotType = new CategoricalFeature<>(PokerFeatureName.FL_POT_TYPE,
            PokerGame::getFlPotType,
            EnumSet.allOf(PostflopPotType.class));
    public final static CategoricalFeature<PostflopPotType, PokerGame> tnPotType = new CategoricalFeature<>(PokerFeatureName.TN_POT_TYPE,
            PokerGame::getTnPotType,
            EnumSet.allOf(PostflopPotType.class));

    public final static CategoricalFeature<HeroActionType, PokerGame> pfHeroActionType = new CategoricalFeature<>(PokerFeatureName.PF_HERO_ACTION_TYPE,
            pokerGame -> pokerGame.computePfHeroActionType(pokerGame.getNextPlayerOrThrow().getPreflopPosition()),
            EnumSet.allOf(HeroActionType.class));
    public final static CategoricalFeature<HeroActionType, PokerGame> flHeroActionType = new CategoricalFeature<>(PokerFeatureName.FL_HERO_ACTION_TYPE,
            pokerGame -> pokerGame.computeFlHeroActionType(pokerGame.getNextPlayerOrThrow().getPreflopPosition()),
            EnumSet.allOf(HeroActionType.class));
    public final static CategoricalFeature<HeroActionType, PokerGame> tnHeroActionType = new CategoricalFeature<>(PokerFeatureName.TN_HERO_ACTION_TYPE,
            pokerGame -> pokerGame.computeTnHeroActionType(pokerGame.getNextPlayerOrThrow().getPreflopPosition()),
            EnumSet.allOf(HeroActionType.class));

    public final static List<Feature<?, PokerGame>> rvServiceFeatures = List.of(
            PokerFeatures.handId,
            PokerFeatures.handDate,
            PokerFeatures.heroName,
            PokerFeatures.boardSize,
            PokerFeatures.bbAmount,
            PokerFeatures.heroPosition,
            PokerFeatures.pocketCards,
            PokerFeatures.flCards,
            PokerFeatures.tnCard,
            PokerFeatures.rvCard,
            PokerFeatures.heroHand
    );

    public final static List<Feature<?, PokerGame>> tnServiceFeatures = List.of(
            PokerFeatures.handId,
            PokerFeatures.handDate,
            PokerFeatures.heroName,
            PokerFeatures.boardSize,
            PokerFeatures.bbAmount,
            PokerFeatures.heroPosition,
            PokerFeatures.pocketCards,
            PokerFeatures.flCards,
            PokerFeatures.tnCard,
            PokerFeatures.heroHand
    );

    public final static List<Feature<?, PokerGame>> flServiceFeatures = List.of(
            PokerFeatures.handId,
            PokerFeatures.handDate,
            PokerFeatures.heroName,
            PokerFeatures.boardSize,
            PokerFeatures.bbAmount,
            PokerFeatures.heroPosition,
            PokerFeatures.pocketCards,
            PokerFeatures.flCards,
            PokerFeatures.heroHand
    );

    public final static List<Feature<?, PokerGame>> pfServiceFeatures = List.of(
            PokerFeatures.handId,
            PokerFeatures.handDate,
            PokerFeatures.heroName,
            PokerFeatures.boardSize,
            PokerFeatures.bbAmount,
            PokerFeatures.heroPosition,
            PokerFeatures.pocketCards,
            PokerFeatures.heroHand
    );

    public final static List<Feature<?, PokerGame>> labels = List.of(
            PokerFeatures.heroAction,
            PokerFeatures.heroBranch,
            PokerFeatures.heroAmount,
            PokerFeatures.heroAmountBb,
            PokerFeatures.heroAmountByPot,
            PokerFeatures.heroDiffAmountByPot,
            PokerFeatures.heroDiffAmountByStackStub,
            PokerFeatures.heroDiffAmountBb
    );
    public final static List<Feature<?, PokerGame>> pfFeatures = List.of(
            PokerFeatures.totalPotSizeBb,
            PokerFeatures.prevBranch,
            PokerFeatures.heroCallAmountByStackStub,
            PokerFeatures.heroCallAmountBb,
            PokerFeatures.heroOppsInvestedAmountByInitialStack,
            PokerFeatures.heroOppsInvestedAmountBb,
            PokerFeatures.totalPotSizeByHeroInitialStack,
            PokerFeatures.heroInvestmentsByTotalPot,
            PokerFeatures.heroInvestmentsBb,
            PokerFeatures.heroStackStubByTotalPot,
            PokerFeatures.heroStackStubBb,
            PokerFeatures.heroStackStubByInitialStack,
            PokerFeatures.heroInitialStackBb,
            PokerFeatures.maxStackStubBbExceptHero,
            PokerFeatures.maxEffStackType,
            PokerFeatures.heroEffStackBb,
            PokerFeatures.heroEffStackType,
            PokerFeatures.pocketCardsName,
            PokerFeatures.pocketCardsNameNum
    );
    public final static List<Feature<?, PokerGame>> pfFeaturesOppAware = List.of(
            PokerFeatures.totalPotSizeBb,
            PokerFeatures.prevBranch,
            PokerFeatures.heroCallAmountByStackStub,
            PokerFeatures.heroCallAmountBb,
            PokerFeatures.heroOppsInvestedAmountByInitialStack,
            PokerFeatures.heroOppsInvestedAmountBb,
            PokerFeatures.totalPotSizeByHeroInitialStack,
            PokerFeatures.heroInvestmentsByTotalPot,
            PokerFeatures.heroInvestmentsBb,
            PokerFeatures.heroStackStubByTotalPot,
            PokerFeatures.heroStackStubBb,
            PokerFeatures.heroStackStubByInitialStack,
            PokerFeatures.heroInitialStackBb,
            PokerFeatures.maxStackStubBbExceptHero,
            PokerFeatures.maxEffStackType,
            PokerFeatures.heroEffStackBb,
            PokerFeatures.heroEffStackType,
            PokerFeatures.pocketCardsName,
            PokerFeatures.pocketCardsNameNum,
            PokerFeatures.oppType
    );
    public final static List<Feature<?, PokerGame>> flFeatures = List.of(
            PokerFeatures.totalPotSizeBb,
            PokerFeatures.prevBranch,
            PokerFeatures.heroCallAmountByStackStub,
            PokerFeatures.heroCallAmountBb,
            PokerFeatures.heroOppsInvestedAmountByInitialStack,
            PokerFeatures.heroOppsInvestedAmountBb,
            PokerFeatures.totalPotSizeByHeroInitialStack,
            PokerFeatures.heroInvestmentsByTotalPot,
            PokerFeatures.heroInvestmentsBb,
            PokerFeatures.heroStackStubByTotalPot,
            PokerFeatures.heroStackStubBb,
            PokerFeatures.heroStackStubByInitialStack,
            PokerFeatures.heroInitialStackBb,
            PokerFeatures.maxStackStubBbExceptHero,
            PokerFeatures.maxEffStackType,
            PokerFeatures.heroEffStackBb,
            PokerFeatures.heroEffStackType,
            PokerFeatures.pfPotType,
            PokerFeatures.pfHeroActionType,

            PokerFeatures.heroStackStubByFlAvgStack,
            PokerFeatures.flInitialPotSizeByTotalInitialStack,
            PokerFeatures.flHoldingBoardFlush,
            PokerFeatures.flHoldingBoardStraight,
            PokerFeatures.flHoldingBoardPair,
            PokerFeatures.flHoldingFlush,
            PokerFeatures.flHoldingFlushHandicap,
            PokerFeatures.flHoldingFlushRank,
            PokerFeatures.flHoldingStraight,
            PokerFeatures.flHoldingStraightHandicap,
            PokerFeatures.flHoldingStraightRank,
            PokerFeatures.flHoldingComboExType,
            PokerFeatures.flHoldingComboHandicap,
            PokerFeatures.flHoldingComboRank
    );
    public final static List<Feature<?, PokerGame>> tnFeatures = List.of(
            PokerFeatures.totalPotSizeBb,
            PokerFeatures.prevBranch,
            PokerFeatures.heroCallAmountByStackStub,
            PokerFeatures.heroCallAmountBb,
            PokerFeatures.heroOppsInvestedAmountByInitialStack,
            PokerFeatures.heroOppsInvestedAmountBb,
            PokerFeatures.totalPotSizeByHeroInitialStack,
            PokerFeatures.heroInvestmentsByTotalPot,
            PokerFeatures.heroInvestmentsBb,
            PokerFeatures.heroStackStubByTotalPot,
            PokerFeatures.heroStackStubBb,
            PokerFeatures.heroStackStubByInitialStack,
            PokerFeatures.heroInitialStackBb,
            PokerFeatures.maxStackStubBbExceptHero,
            PokerFeatures.maxEffStackType,
            PokerFeatures.heroEffStackBb,
            PokerFeatures.heroEffStackType,
            PokerFeatures.pfPotType,
            PokerFeatures.flPotType,
            PokerFeatures.pfHeroActionType,
            PokerFeatures.flHeroActionType,
            PokerFeatures.flHoldingComboExType,

            PokerFeatures.heroStackStubByFlAvgStack,
            PokerFeatures.heroStackStubByTnAvgStack,
            PokerFeatures.flInitialPotSizeByTotalInitialStack,
            PokerFeatures.tnInitialPotSizeByTotalInitialStack,
            PokerFeatures.flInitialPotSizeByTnInitialPotSize,
            PokerFeatures.tnHoldingBoardFlush,
            PokerFeatures.tnHoldingBoardStraight,
            PokerFeatures.tnHoldingBoardPair,
            PokerFeatures.tnHoldingFlush,
            PokerFeatures.tnHoldingFlushHandicap,
            PokerFeatures.tnHoldingFlushRank,
            PokerFeatures.tnHoldingStraight,
            PokerFeatures.tnHoldingStraightHandicap,
            PokerFeatures.tnHoldingStraightRank,
            PokerFeatures.tnHoldingComboExType,
            PokerFeatures.tnHoldingComboHandicap,
            PokerFeatures.tnHoldingComboRank
    );
    public final static List<Feature<?, PokerGame>> rvFeatures = List.of(
            PokerFeatures.totalPotSizeBb,
            PokerFeatures.prevBranch,
            PokerFeatures.heroCallAmountByStackStub,
            PokerFeatures.heroCallAmountBb,
            PokerFeatures.heroOppsInvestedAmountByInitialStack,
            PokerFeatures.heroOppsInvestedAmountBb,
            PokerFeatures.totalPotSizeByHeroInitialStack,
            PokerFeatures.heroInvestmentsByTotalPot,
            PokerFeatures.heroInvestmentsBb,
            PokerFeatures.heroStackStubByTotalPot,
            PokerFeatures.heroStackStubBb,
            PokerFeatures.heroStackStubByInitialStack,
            PokerFeatures.heroInitialStackBb,
            PokerFeatures.maxStackStubBbExceptHero,
            PokerFeatures.maxEffStackType,
            PokerFeatures.heroEffStackBb,
            PokerFeatures.heroEffStackType,
            PokerFeatures.pfPotType,
            PokerFeatures.flPotType,
            PokerFeatures.tnPotType,
            PokerFeatures.pfHeroActionType,
            PokerFeatures.flHeroActionType,
            PokerFeatures.tnHeroActionType,
            PokerFeatures.flHoldingComboExType,
            PokerFeatures.tnHoldingComboExType,

            PokerFeatures.heroStackStubByFlAvgStack,
            PokerFeatures.heroStackStubByTnAvgStack,
            PokerFeatures.heroStackStubByRvAvgStack,
            PokerFeatures.flInitialPotSizeByTotalInitialStack,
            PokerFeatures.tnInitialPotSizeByTotalInitialStack,
            PokerFeatures.rvInitialPotSizeByTotalInitialStack,
            PokerFeatures.flInitialPotSizeByTnInitialPotSize,
            PokerFeatures.tnInitialPotSizeByRvInitialPotSize,
            PokerFeatures.rvHoldingBoardFlush,
            PokerFeatures.rvHoldingBoardStraight,
            PokerFeatures.rvHoldingBoardPair,
            PokerFeatures.rvHoldingFlush,
            PokerFeatures.rvHoldingFlushHandicap,
            PokerFeatures.rvHoldingFlushRank,
            PokerFeatures.rvHoldingStraight,
            PokerFeatures.rvHoldingStraightHandicap,
            PokerFeatures.rvHoldingStraightRank,
            PokerFeatures.rvHoldingComboExType,
            PokerFeatures.rvHoldingComboHandicap,
            PokerFeatures.rvHoldingComboRank

    );

}
