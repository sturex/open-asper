package dev.asper.poker.enums;


import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerPlayer;
import dev.asper.poker.engine.PokerSituation;

public enum SpecialSpot {
    BARREL_RIVER_AFT_DELAY_TURN_IP,
    BET_TURN_AS_CALLER_CHECKER_PF_IP,
    BET_VS_DOUBLECHECK_IP,
    CONTBET_FLOP_RAISER_IP_SRP,
    CONTBET_FLOP_RAISER_OOP,
    CONTBET_FLOP_RAISER_SBVSBB_OOP,
    CONTBET_TURN_RAISER_IP_SRP,
    DELAY_CBET_TURN_OOP,
    DELAY_CONTBET_TURN_IP,
    FLOAT_RIVER_IP,
    FLOAT_TURN_IP,
    FL_IP_CALLER_VS_RAISE,
    FL_OOP_CALLER_VS_RAISE,
    IP_CALLER_VS_BET_40PLUS_PERC_POT,
    IP_CALLER_VS_BET_40_PERC_POT,
    MWP_CONTBET_FLOP_OOP_IP,
    MWP_CONTBET_FLOP_OOP_OOP,
    MWP_DELAY_CONTBET_TURN_VS_ANY,
    MWP_PROBE_BET_TURN_BB_VS_IP_OOP,
    NONE,
    OOP_CALLER_VS_BET_40PLUS_PERC_POT,
    OOP_CALLER_VS_BET_40_PERC_POT,
    PROBE_RIVER_OOP,
    RAISE_FLOP_BBVSSB_IP,
    RAISE_PROBE_TURN_IP,
    RV_IP_CALLER_VS_RAISE,
    RV_OOP_CALLER_VS_RAISE,
    TN_IP_CALLER_VS_RAISE,
    TN_OOP_CALLER_VS_RAISE,
    TN_IP_CALLER_VS_BET,
    TN_OOP_CALLER_VS_BET,
    RV_IP_CALLER_VS_BET,
    RV_OOP_CALLER_VS_BET,
    XXB_OOP;

    public static SpecialSpot from(PokerGame pokerGame) {
        PokerSituation pokerSituation = pokerGame.getState();
        Street currentStreet = pokerSituation.getStreet();
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        PreflopPosition heroPosition = hero.getPreflopPosition();

        switch (currentStreet) {
            case FL -> {
                HeroActionType pfHeroActionType = pokerGame.computePfHeroActionType(heroPosition);
                if (PokerSituation.flCheckerSituationsHuIp.contains(pokerSituation) && pfHeroActionType.isRaiser()) {
                    return CONTBET_FLOP_RAISER_IP_SRP;
                } else if (pokerSituation == PokerSituation.FL_HU_OOP_CHECKER_SRP_SBRVSBB || pokerSituation == PokerSituation.FL_HU_OOP_CHECKER_MRP_SBVSBB && pfHeroActionType.isRaiser()) {
                    return CONTBET_FLOP_RAISER_SBVSBB_OOP;
                } else if (PokerSituation.flCheckerSituationsHuOop.contains(pokerSituation) && pfHeroActionType.isRaiser()) {
                    return CONTBET_FLOP_RAISER_OOP;
                } else if (pokerSituation == PokerSituation.FL_HU_IP_CALLER_BBVSSB) {
                    return RAISE_FLOP_BBVSSB_IP;
                } else if (pokerSituation == PokerSituation.FL_MWP_BTW_CHECKER && pfHeroActionType.isRaiser()) {
                    return MWP_CONTBET_FLOP_OOP_IP;
                } else if (pokerSituation == PokerSituation.FL_MWP_LAST_CHECKER && pfHeroActionType.isRaiser()) {
                    return MWP_CONTBET_FLOP_OOP_OOP;
                } else if (PokerSituation.flCallerSituationsHuIpVsRaise.contains(pokerSituation)) {
                    return FL_IP_CALLER_VS_RAISE;
                } else if (PokerSituation.flCallerSituationsHuOopVsRaise.contains(pokerSituation)) {
                    return FL_OOP_CALLER_VS_RAISE;
                } else if (PokerSituation.flCallerSituationsHuIpVsBet.contains(pokerSituation)) {
                    long callAmount = pokerGame.computeCallAmount(heroPosition);
                    long potSizeBeforeOppBet = pokerGame.getPotSize() - callAmount;
                    return (double) callAmount / (double) potSizeBeforeOppBet < 0.4 ? IP_CALLER_VS_BET_40_PERC_POT : IP_CALLER_VS_BET_40PLUS_PERC_POT;
                } else if (PokerSituation.flCallerSituationsHuOopVsBet.contains(pokerSituation)) {
                    long callAmount = pokerGame.computeCallAmount(heroPosition);
                    long potSizeBeforeOppBet = pokerGame.getPotSize() - callAmount;
                    return (double) callAmount / (double) potSizeBeforeOppBet < 0.4 ? OOP_CALLER_VS_BET_40_PERC_POT : OOP_CALLER_VS_BET_40PLUS_PERC_POT;
                }
            }
            case TN -> {
                HeroActionType pfHeroActionType = pokerGame.computePfHeroActionType(heroPosition);
                HeroActionType flHeroActionType = pokerGame.computeFlHeroActionType(heroPosition);
                if (pokerSituation == PokerSituation.TN_HU_IP_CHECKER) {
                    if (pfHeroActionType.isRaiser()) {
                        if (flHeroActionType.isRaiser()) {
                            return CONTBET_TURN_RAISER_IP_SRP;
                        } else if (flHeroActionType == HeroActionType.C) {
                            return DELAY_CONTBET_TURN_IP;
                        } else if (flHeroActionType == HeroActionType.CC) {
                            return FLOAT_TURN_IP;
                        }
                    } else if (pfHeroActionType == HeroActionType.C) {
                        if (flHeroActionType == HeroActionType.C) {
                            return BET_TURN_AS_CALLER_CHECKER_PF_IP;
                        }
                    }
                } else if (pokerSituation == PokerSituation.TN_HU_OOP_CHECKER_FLX && pfHeroActionType.isRaiser()) {
                    return DELAY_CBET_TURN_OOP;
                } else if (pokerSituation == PokerSituation.TN_HU_IP_CALLER) {
                    if (pfHeroActionType.isRaiser() && flHeroActionType == HeroActionType.C) {
                        return RAISE_PROBE_TURN_IP;
                    } else {
                        return TN_IP_CALLER_VS_BET;
                    }
                } else if (pokerSituation == PokerSituation.TN_HU_OOP_CALLER) {
                    return TN_OOP_CALLER_VS_BET;
                } else if (pokerSituation == PokerSituation.TN_HU_OOP_CALLER_VS_RAISE) {
                    return TN_OOP_CALLER_VS_RAISE;
                } else if (pokerSituation == PokerSituation.TN_MWP_BTW_CHECKER && flHeroActionType == HeroActionType.C && pfHeroActionType.isRaiser()) {
                    return MWP_DELAY_CONTBET_TURN_VS_ANY;
                } else if (pokerSituation == PokerSituation.TN_MWP_BTW_CHECKER && pokerGame.getFlMoveStats().size() == pokerGame.getPlayers().size()
                        && heroPosition == PreflopPosition.BB && !pokerGame.getTnMoveStats().isEmpty()) {
                    return MWP_PROBE_BET_TURN_BB_VS_IP_OOP;
                }
            }
            case RV -> {
                HeroActionType pfHeroActionType = pokerGame.computePfHeroActionType(heroPosition);
                HeroActionType flHeroActionType = pokerGame.computeFlHeroActionType(heroPosition);
                HeroActionType tnHeroActionType = pokerGame.computeTnHeroActionType(heroPosition);
                if (pokerSituation == PokerSituation.RV_HU_IP_CHECKER) {
                    if (tnHeroActionType == HeroActionType.CC) {
                        return FLOAT_RIVER_IP;
                    } else if (tnHeroActionType == HeroActionType.C) {
                        return BET_VS_DOUBLECHECK_IP;
                    }
                } else if (pokerSituation == PokerSituation.RV_HU_OOP_CHECKER_TNX) {
                    if (flHeroActionType == HeroActionType.C) {
                        return XXB_OOP;
                    } else if (flHeroActionType == HeroActionType.CC) {
                        return PROBE_RIVER_OOP;
                    }
                } else if (pokerSituation == PokerSituation.RV_HU_OOP_CHECKER_TNR &&
                        pfHeroActionType.isRaiser() &&
                        flHeroActionType == HeroActionType.C) {
                    return BARREL_RIVER_AFT_DELAY_TURN_IP;
                } else if (pokerSituation == PokerSituation.RV_HU_IP_CALLER) {
                    return RV_IP_CALLER_VS_BET;
                } else if (pokerSituation == PokerSituation.RV_HU_IP_CALLER_VS_RAISE) {
                    return RV_IP_CALLER_VS_RAISE;
                } else if (pokerSituation == PokerSituation.RV_HU_OOP_CALLER) {
                    return RV_OOP_CALLER_VS_BET;
                } else if (pokerSituation == PokerSituation.RV_HU_OOP_CALLER_VS_RAISE) {
                    return RV_OOP_CALLER_VS_RAISE;
                }
            }
        }
        return NONE;
    }
}