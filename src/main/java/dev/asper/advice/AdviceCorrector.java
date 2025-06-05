package dev.asper.advice;

import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerPlayer;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.poker.enums.Action;
import dev.asper.poker.enums.Street;

import java.util.Comparator;

public enum AdviceCorrector {
    ;
    private final static double stackPercentageAllin = 0.20;
    private final static double stackStubByPot = 0.8;
    private final static double raiseStepSizeBb = 0.5;
    private final static double callAmountByPotOnLastDecision = 0.20;
    private final static int stackBbAllin = 2;

    public static Advice correct(Advice advice, PokerGame pokerGame) {
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        int potSize = pokerGame.getPotSize();
        int callAmount = pokerGame.computeCallAmount(hero.getPreflopPosition());
        int stackStub = hero.getStackStub();
        PokerSituation pokerSituation = pokerGame.getState();
        int totalInvestedAmount = hero.getTotalInvestedAmount();
        Street street = pokerSituation.getStreet();
        return switch (advice.action()) {
            case F -> correctFold(pokerGame, advice, street, callAmount, potSize, stackStub, totalInvestedAmount);
            case C -> {
                if (callAmount != 0) {
                    yield correctCall(pokerGame, advice, callAmount, stackStub);
                } else {
                    yield advice;
                }
            }
            case R -> {
                if (callAmount != 0) {
                    yield correctRaise(pokerGame, advice, callAmount, stackStub);
                } else {
                    yield correctBet(pokerGame, advice, street, callAmount, potSize, stackStub, totalInvestedAmount);
                }
            }
            case A -> advice;
        };

    }

    private static Advice correctBet(PokerGame pokerGame, Advice advice, Street street, int callAmount, int potSize, int stackStub, int totalInvestedAmount) {
        int raiseStep = (int) (pokerGame.getBbAmount() * raiseStepSizeBb);
        int roundedByStepAmount = roundByStepNotLess(advice.amount(), raiseStep, pokerGame.getBbAmount());
        int maxStackStub = pokerGame.getPlayers().stream().map(PokerPlayer::getStackStub).max(Comparator.comparingInt(Integer::intValue)).orElse(0);
        if (roundedByStepAmount >= maxStackStub) {
            return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: BET was converted to BET_ALLIN due to original advice amount greater than max players stack stub");
        } else if (roundedByStepAmount >= stackStub) {
            return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: BET was converted to BET_ALLIN due to original advice amount greater than hero stack stub");
        } else if ((double) (stackStub - roundedByStepAmount) / stackStub < stackPercentageAllin) {
            return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: BET was converted to BET_ALLIN due to funds that will left after action would be less than " + stackPercentageAllin * 100 + "% percent.");
        } else if ((double) stackStub / pokerGame.getPotSize() < stackStubByPot) {
            return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: BET was converted to BET_ALLIN due to funds by pot lower than " + stackStubByPot);
        } else {
            return new Advice(Action.R, roundedByStepAmount, advice.message() + " CORRECTION: BET size was adjusted with bet step=" + raiseStep);
        }
    }

    private static Advice correctRaise(PokerGame pokerGame, Advice advice, int callAmount, int stackStub) {
        int playerSize = pokerGame.getPlayers().size();
        if (playerSize == 1) {
            if (callAmount >= stackStub) {
                return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: RAISE was converted to CALL_ALLIN due to last standing decision making player and funds lower than call amount.");
            } else {
                return new Advice(Action.C, callAmount, advice.message() + " CORRECTION: RAISE was converted to CALL due to last standing decision making player and funds greater than call amount");
            }
        } else {
            int maxStackStub = pokerGame.getPlayers().stream().map(PokerPlayer::getStackStub).max(Comparator.comparingInt(value -> value)).orElse(0);
            int raiseStep = (int) (pokerGame.getBbAmount() * raiseStepSizeBb);
            int roundedByStepAmount = roundByStepNotLess(advice.amount(), raiseStep, pokerGame.getBbAmount());
            if (callAmount >= stackStub) {
                return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: RAISE was converted to CALL_ALLIN due to funds lower than call amount");
            } else if (roundedByStepAmount >= maxStackStub) {
                return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: RAISE was converted to RAISE_ALLIN due to original advice amount greater than max players stack stub");
            } else if (roundedByStepAmount >= stackStub) {
                return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: RAISE was converted to RAISE_ALLIN due to original advice amount greater than hero stack stub");
            } else if ((double) (stackStub - roundedByStepAmount) / stackStub < stackPercentageAllin) {
                return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: RAISE was converted to RAISE_ALLIN due to funds that will left after making action would be less than " + stackPercentageAllin * 100 + "% percent.");
            } else if ((double) stackStub / pokerGame.getPotSize() < stackStubByPot) {
                return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: RAISE was converted to RAISE_ALLIN due to funds by pot lower than " + stackStubByPot);
            } else {
                return new Advice(Action.R, roundedByStepAmount, advice.message() + " CORRECTION: RAISE size was adjusted with raise step=" + raiseStep);
            }
        }
    }

    private static Advice correctCall(PokerGame pokerGame, Advice advice, int callAmount, int stackStub) {
        if (callAmount >= stackStub) {
            return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: CALL was converted to CALL_ALLIN due to funds lower than call amount.");
        } else if (stackStub < stackBbAllin * pokerGame.getBbAmount()) {
            if (pokerGame.getPlayers().size() > 1) {
                return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: CALL was converted to RAISE_ALLIN due to funds lower than " + stackBbAllin + " big blinds.");
            } else {
                return advice;
            }
        } else {
            return advice;
        }
    }

    private static Advice correctFold(PokerGame pokerGame, Advice advice, Street street, int callAmount, int potSize, int stackStub, int totalInvestedAmount) {
        if (street == Street.PF &&
                callAmount != 0 &&
                totalInvestedAmount != 0 &&
                (double) callAmount / potSize < callAmountByPotOnLastDecision) {
            if (callAmount >= stackStub) {
                return new Advice(Action.A, stackStub, advice.message() + " CORRECTION: FOLD was converted to CALL_ALLIN due to call amount by pot less than " + callAmountByPotOnLastDecision + " percent and funds lower than call amount.");
            } else {
                return new Advice(Action.C, callAmount, advice.message() + " CORRECTION: FOLD was converted to CALL due to call amount by pot less than " + callAmountByPotOnLastDecision);
            }
        } else {
            return advice;
        }
    }

    public static int roundByStepNotLess(double input, int step, long minValue) {
        return (int) Math.max(minValue, Math.round(input / step) * step);
    }
}
