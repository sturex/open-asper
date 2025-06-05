package dev.asper.poker.engine.spot;

import dev.asper.advice.ActionMap;
import dev.asper.advice.ActionTweak;
import dev.asper.advice.ActionVector;
import dev.asper.advice.AmountTweak;
import dev.asper.poker.engine.EffStackType;
import dev.asper.poker.engine.PokerGame;
import dev.asper.poker.engine.PokerPlayer;
import dev.asper.poker.engine.PokerSituation;
import dev.asper.spark.FeatureSchema;

public enum PreflopSpot implements Spot {

    PF_FIRST_VS_9_CALLERS_BB_20_40(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_40_PLUS(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),

    PF_FIRST_VS_8_CALLERS_BB_20_40(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_40_PLUS(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),

    PF_FIRST_VS_7_CALLERS_BB_20_40(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_40_PLUS(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),

    PF_FIRST_VS_6_CALLERS_BB_20_40(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_40_PLUS(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),

    PF_FIRST_VS_5_CALLERS_BB_20_40(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_40_PLUS(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),

    PF_FIRST_VS_4_CALLERS_BB_20_40(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_40_PLUS(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),

    PF_FIRST_CO_BB_20_40(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),
    PF_FIRST_CO_BB_40_PLUS(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),

    PF_FIRST_BTN_BB_20_40(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),
    PF_FIRST_BTN_BB_40_PLUS(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 2.0),

    PF_FIRST_SB_BB_20_40(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 0.0, 0.0, 1)), 3.0),
    PF_FIRST_SB_BB_40_PLUS(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, 3.0),

    PF_FP_VS_FP_OPEN_RAISER_BB_20_40(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_CO_VS_UTG_OPEN_RAISER_BB_20_40(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_BTN_VS_UTG_OPEN_RAISER_BB_20_40(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_SB_VS_UTG_OPEN_RAISER_BB_20_40(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_BB_VS_UTG_OPEN_RAISER_BB_20_40(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_CO_VS_MP_OPEN_RAISER_BB_20_40(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_BTN_VS_MP_OPEN_RAISER_BB_20_40(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_SB_VS_MP_OPEN_RAISER_BB_20_40(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_BB_VS_MP_OPEN_RAISER_BB_20_40(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_BTN_VS_CO_OPEN_RAISER_BB_20_40(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_SB_VS_CO_OPEN_RAISER_BB_20_40(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_BB_VS_CO_OPEN_RAISER_BB_20_40(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_SB_VS_BTN_OPEN_RAISER_BB_20_40(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_BB_VS_BTN_OPEN_RAISER_BB_20_40(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_BB_VS_SB_OPEN_RAISER_BB_20_40(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_40_PLUS(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),


    PF_FP_VS_FP_LIMP_BB_20_40(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_40_PLUS(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 1, 1, 1)), 4.0),


    PF_SB_VS_FP_LIMP_BB_20_40(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_40_PLUS(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(1, 1, 1, 1)), 3.5),


    PF_BB_VS_FP_LIMP_BB_20_40(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultiply(ActionVector.from(0.0, 1, 1, 1)), 3.0),
    PF_BB_VS_FP_LIMP_BB_40_PLUS(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultiply(ActionVector.from(0.0, 1, 1, 1)), 3.0),


    PF_VS_AI_SHOVE_SAFE_BB_20_40(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_40_PLUS(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 0.5),


    PF_VS_AI_SHOVE_UNSAFE_BB_20_40(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_40_PLUS(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 0.5),

    PF_BB_VS_BTN_SQUEEZE(PokerSituation.PF_BB_VS_BTN_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_COMPLETE(PokerSituation.PF_BB_VS_COMPLETE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultiply(ActionVector.from(0.0, 1.0, 1.0, 1)), 5.0),
    PF_BB_VS_CO_COLD_3BET(PokerSituation.PF_BB_VS_CO_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.pfRareSituationCaller, 5.0),
    PF_BB_VS_CO_SQUEEZE(PokerSituation.PF_BB_VS_CO_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_FP_COLD_3BET(PokerSituation.PF_BB_VS_FP_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_FP_ISOLATE(PokerSituation.PF_BB_VS_FP_ISOLATE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_FP_ISOLATE_3BET(PokerSituation.PF_BB_VS_FP_ISOLATE_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_FP_OPEN_RAISER_COLD_CALL(PokerSituation.PF_BB_VS_FP_OPEN_RAISER_COLD_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_FP_SQUEEZE(PokerSituation.PF_BB_VS_FP_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_CO_OPEN_RAISER_COLD_CALL(PokerSituation.PF_BB_VS_CO_OPEN_RAISER_COLD_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_SB_COLD_3BET(PokerSituation.PF_BB_VS_SB_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_SB_ISOLATE(PokerSituation.PF_BB_VS_SB_ISOLATE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_SB_ISOLATE_3BET(PokerSituation.PF_BB_VS_SB_ISOLATE_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_SB_SQUEEZE(PokerSituation.PF_BB_VS_SB_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_COLD_CALL(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER_COLD_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_OPEN_RAISER_VS_SBBB_3BET(PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_OPEN_RAISER_VS_SBBB_SQUEEZE(PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_VS_CO_COLD_3BET(PokerSituation.PF_BTN_VS_CO_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_VS_CO_SQUEEZE(PokerSituation.PF_BTN_VS_CO_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_VS_FP_COLD_3BET(PokerSituation.PF_BTN_VS_FP_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_VS_FP_OPEN_RAISER_COLD_CALL(PokerSituation.PF_BTN_VS_FP_OPEN_RAISER_COLD_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BTN_VS_FP_SQUEEZE(PokerSituation.PF_BTN_VS_FP_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_COLD_CALLER_VS_FP_SQUEEZE(PokerSituation.PF_COLD_CALLER_VS_FP_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_COLD_CALLER_VS_SBBB_SQUEEZE(PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_CO_OPEN_RAISER_VS_FP_3BET(PokerSituation.PF_CO_OPEN_RAISER_VS_FP_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_CO_OPEN_RAISER_VS_SBBB_3BET(PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_CO_OPEN_RAISER_VS_SBBB_SQUEEZE(PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_CO_VS_FP_COLD_3BET(PokerSituation.PF_CO_VS_FP_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_CO_VS_FP_OPEN_RAISER_COLD_CALL(PokerSituation.PF_CO_VS_FP_OPEN_RAISER_COLD_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_CO_VS_FP_SQUEEZE(PokerSituation.PF_CO_VS_FP_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_FP_LIMPER_VS_FP_ISOLATE(PokerSituation.PF_FP_LIMPER_VS_FP_ISOLATE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_FP_LIMPER_VS_SBBB_ISOLATE(PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_FP_VS_FP_COLD_3BET(PokerSituation.PF_FP_VS_FP_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_FP_VS_FP_ISOLATE(PokerSituation.PF_FP_VS_FP_ISOLATE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_FP_VS_FP_ISOLATE_3BET(PokerSituation.PF_FP_VS_FP_ISOLATE_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_FP_VS_FP_OPEN_RAISER_COLD_CALL(PokerSituation.PF_FP_VS_FP_OPEN_RAISER_COLD_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_FP_VS_FP_SQUEEZE(PokerSituation.PF_FP_VS_FP_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_MP_OPEN_RAISER_VS_FP_3BET(PokerSituation.PF_MP_OPEN_RAISER_VS_FP_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_MP_OPEN_RAISER_VS_FP_SQUEEZE(PokerSituation.PF_MP_OPEN_RAISER_VS_FP_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_MP_OPEN_RAISER_VS_SBBB_3BET(PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_MP_OPEN_RAISER_VS_SBBB_SQUEEZE(PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_OPEN_RAISER_VS_BB_3BET(PokerSituation.PF_SB_OPEN_RAISER_VS_BB_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_CO_OPEN_RAISER_COLD_CALL(PokerSituation.PF_SB_VS_CO_OPEN_RAISER_COLD_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_BTN_COLD_3BET(PokerSituation.PF_SB_VS_BTN_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_BTN_SQUEEZE(PokerSituation.PF_SB_VS_BTN_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_CO_COLD_3BET(PokerSituation.PF_SB_VS_CO_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_CO_SQUEEZE(PokerSituation.PF_SB_VS_CO_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_FP_COLD_3BET(PokerSituation.PF_SB_VS_FP_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_FP_ISOLATE(PokerSituation.PF_SB_VS_FP_ISOLATE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_FP_ISOLATE_3BET(PokerSituation.PF_SB_VS_FP_ISOLATE_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_FP_OPEN_RAISER_COLD_CALL(PokerSituation.PF_SB_VS_FP_OPEN_RAISER_COLD_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_SB_VS_FP_SQUEEZE(PokerSituation.PF_SB_VS_FP_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_UTG_OPEN_RAISER_VS_FP_3BET(PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_UTG_OPEN_RAISER_VS_FP_SQUEEZE(PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_UTG_OPEN_RAISER_VS_SBBB_3BET(PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_UTG_OPEN_RAISER_VS_SBBB_SQUEEZE(PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_SQUEEZE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),


    PF_FIRST_VS_9_CALLERS_BB_0_2(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_2_4(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_4_6(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_6_8(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_8_10(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_10_12(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_12_14(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_14_16(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_16_18(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.0),
    PF_FIRST_VS_9_CALLERS_BB_18_20(PokerSituation.PF_FIRST_VS_9_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_0_2(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_2_4(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_4_6(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_6_8(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_8_10(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_10_12(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_12_14(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_14_16(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_16_18(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.0),
    PF_FIRST_VS_8_CALLERS_BB_18_20(PokerSituation.PF_FIRST_VS_8_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_0_2(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_2_4(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_4_6(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_6_8(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_8_10(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_10_12(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_12_14(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_14_16(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_16_18(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.0),
    PF_FIRST_VS_7_CALLERS_BB_18_20(PokerSituation.PF_FIRST_VS_7_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_0_2(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_2_4(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_4_6(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_6_8(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_8_10(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_10_12(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_12_14(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_14_16(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_16_18(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.0),
    PF_FIRST_VS_6_CALLERS_BB_18_20(PokerSituation.PF_FIRST_VS_6_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_0_2(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_2_4(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_4_6(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_6_8(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_8_10(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_10_12(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_12_14(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_14_16(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_16_18(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.0),
    PF_FIRST_VS_5_CALLERS_BB_18_20(PokerSituation.PF_FIRST_VS_5_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_0_2(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_2_4(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_4_6(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_6_8(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_8_10(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_10_12(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_12_14(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_14_16(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_16_18(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.0),
    PF_FIRST_VS_4_CALLERS_BB_18_20(PokerSituation.PF_FIRST_VS_4_CALLERS, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_0_2(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_2_4(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_4_6(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_6_8(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_8_10(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_10_12(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_12_14(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_14_16(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_16_18(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.0),
    PF_FIRST_CO_BB_18_20(PokerSituation.PF_FIRST_CO, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_0_2(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_2_4(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_4_6(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_6_8(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_8_10(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_10_12(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_12_14(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_14_16(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_16_18(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.0),
    PF_FIRST_BTN_BB_18_20(PokerSituation.PF_FIRST_BTN, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.0),
    PF_FIRST_SB_BB_0_2(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_2_4(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_4_6(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_6_8(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_8_10(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_10_12(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_12_14(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_14_16(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_16_18(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 3.0),
    PF_FIRST_SB_BB_18_20(PokerSituation.PF_FIRST_SB, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 3.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_0_2(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_2_4(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_4_6(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_6_8(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_8_10(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_10_12(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_12_14(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_14_16(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_16_18(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_FP_VS_FP_OPEN_RAISER_BB_18_20(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_0_2(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_2_4(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_4_6(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_6_8(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_8_10(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_10_12(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_12_14(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_14_16(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_16_18(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_CO_VS_UTG_OPEN_RAISER_BB_18_20(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_0_2(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_2_4(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_4_6(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_6_8(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_8_10(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_10_12(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_12_14(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_14_16(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_16_18(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_BTN_VS_UTG_OPEN_RAISER_BB_18_20(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_0_2(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_2_4(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_4_6(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_6_8(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_8_10(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_10_12(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_12_14(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_14_16(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_16_18(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_SB_VS_UTG_OPEN_RAISER_BB_18_20(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_0_2(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_2_4(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_4_6(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_6_8(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_8_10(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_10_12(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_12_14(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_14_16(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_16_18(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_BB_VS_UTG_OPEN_RAISER_BB_18_20(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_0_2(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_2_4(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_4_6(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_6_8(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_8_10(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_10_12(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_12_14(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_14_16(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_16_18(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_CO_VS_MP_OPEN_RAISER_BB_18_20(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_0_2(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_2_4(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_4_6(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_6_8(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_8_10(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_10_12(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_12_14(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_14_16(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_16_18(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_BTN_VS_MP_OPEN_RAISER_BB_18_20(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_0_2(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_2_4(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_4_6(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_6_8(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_8_10(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_10_12(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_12_14(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_14_16(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_16_18(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_SB_VS_MP_OPEN_RAISER_BB_18_20(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_0_2(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_2_4(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_4_6(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_6_8(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_8_10(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_10_12(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_12_14(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_14_16(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_16_18(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_BB_VS_MP_OPEN_RAISER_BB_18_20(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_0_2(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_2_4(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_4_6(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_6_8(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_8_10(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_10_12(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_12_14(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_14_16(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_16_18(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_BTN_VS_CO_OPEN_RAISER_BB_18_20(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_0_2(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_2_4(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_4_6(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_6_8(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_8_10(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_10_12(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_12_14(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_14_16(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_16_18(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_SB_VS_CO_OPEN_RAISER_BB_18_20(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_0_2(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_2_4(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_4_6(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_6_8(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_8_10(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_10_12(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_12_14(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_14_16(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_16_18(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_BB_VS_CO_OPEN_RAISER_BB_18_20(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_0_2(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_2_4(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_4_6(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_6_8(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_8_10(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_10_12(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_12_14(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_14_16(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_16_18(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_SB_VS_BTN_OPEN_RAISER_BB_18_20(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_0_2(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_2_4(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_4_6(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_6_8(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_8_10(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_10_12(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_12_14(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_14_16(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_16_18(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_BB_VS_BTN_OPEN_RAISER_BB_18_20(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_0_2(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_2_4(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_4_6(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_6_8(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_8_10(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_10_12(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_12_14(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_14_16(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_16_18(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_BB_VS_SB_OPEN_RAISER_BB_18_20(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),

    PF_FP_VS_FP_LIMP_BB_0_2(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_2_4(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_4_6(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_6_8(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_8_10(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_10_12(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_12_14(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_14_16(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_16_18(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 4.0),
    PF_FP_VS_FP_LIMP_BB_18_20(PokerSituation.PF_FP_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 4.0),
    PF_SB_VS_FP_LIMP_BB_0_2(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_2_4(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_4_6(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_6_8(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_8_10(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_10_12(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_12_14(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_14_16(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_16_18(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 3.5),
    PF_SB_VS_FP_LIMP_BB_18_20(PokerSituation.PF_SB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 3.5),
    PF_BB_VS_FP_LIMP_BB_0_2(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.05, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_2_4(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.1, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_4_6(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.2, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_6_8(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.3, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_8_10(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.4, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_10_12(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.5, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_12_14(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.6, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_14_16(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.7, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_16_18(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.8, 1, 1)), 4.0),
    PF_BB_VS_FP_LIMP_BB_18_20(PokerSituation.PF_BB_VS_FP_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.9, 1, 1)), 4.0),
    PF_VS_AI_SHOVE_SAFE_BB_0_2(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_2_4(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_4_6(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_6_8(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_8_10(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_10_12(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_12_14(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_14_16(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_16_18(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_SAFE_BB_18_20(PokerSituation.PF_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_0_2(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_2_4(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_4_6(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_6_8(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_8_10(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_10_12(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_12_14(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_14_16(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_16_18(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 0.5),
    PF_VS_AI_SHOVE_UNSAFE_BB_18_20(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 0.5),


    PF_AI_PUSH_SAFE(PokerSituation.PF_AI_PUSH_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 1.0),
    PF_AI_PUSH_UNSAFE(PokerSituation.PF_AI_PUSH_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 1.0),
    PF_ANY_VS_COLD_3BET_CALL(PokerSituation.PF_ANY_VS_COLD_3BET_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_ANY_VS_COLD_4BET_PLUS(PokerSituation.PF_ANY_VS_COLD_4BET_PLUS, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_ANY_VS_COLD_SQUEEZE_CALL(PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_ANY_VS_COMPLEX_ISOLATE_POT(PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_ANY_VS_COMPLEX_SQUEEZE_POT(PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_ANY_VS_ISOLATED_LIMP_RAISER(PokerSituation.PF_ANY_VS_ISOLATED_LIMP_RAISER, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_BB_VS_BTN_COLD_3BET(PokerSituation.PF_BB_VS_BTN_COLD_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_ANY_VS_4BET_PLUS(PokerSituation.PF_ANY_VS_4BET_PLUS, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_HU_BB_VS_SB_4BET(PokerSituation.PF_HU_BB_VS_SB_4BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_HU_SB_LIMPER_VS_BB_BET(PokerSituation.PF_HU_SB_LIMPER_VS_BB_BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_HU_SB_VS_BB_3BET(PokerSituation.PF_HU_SB_VS_BB_3BET, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_HU_BB_VS_SB_LIMP_BB_0_2(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.05, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_2_4(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.1, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_4_6(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.2, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_6_8(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.3, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_8_10(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.4, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_10_12(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.5, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_12_14(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.6, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_14_16(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.7, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_16_18(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.8, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_18_20(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultipliedAllinOrC(ActionVector.from(0, 0.9, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_20_40(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultiply(ActionVector.from(0.0, 1, 1, 1)), 6.0),
    PF_HU_BB_VS_SB_LIMP_BB_40_PLUS(PokerSituation.PF_HU_BB_VS_SB_LIMP, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdChecker, ActionTweak.maxByMultiply(ActionVector.from(0.0, 1, 1, 1)), 6.0),

    PF_HU_BB_VS_SB_LIMP_RAISE(PokerSituation.PF_HU_BB_VS_SB_LIMP_RAISE, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, 6.0),

    PF_HU_BB_VS_SB_OPEN_RAISE_BB_0_2(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_2_4(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_4_6(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_6_8(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_8_10(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_10_12(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_12_14(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_14_16(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_16_18(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_18_20(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_20_40(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),
    PF_HU_BB_VS_SB_OPEN_RAISE_BB_40_PLUS(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BB, ActionVector.stdCaller, 5.0),

    PF_HU_SB_FIRST_BB_0_2(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_2_4(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_4_6(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_6_8(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_8_10(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_10_12(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_12_14(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_14_16(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_16_18(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_18_20(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 2.5),
    PF_HU_SB_FIRST_BB_20_40(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, 2.5),
    PF_HU_SB_FIRST_BB_40_PLUS(PokerSituation.PF_HU_SB_FIRST, FeatureSchema.ACTION, FeatureSchema.AMOUNT_BB, ActionVector.stdCaller, 2.5),

    PF_HU_VS_AI_SHOVE_SAFE_BB_0_2(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_2_4(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_4_6(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_6_8(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_8_10(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_10_12(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_12_14(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_14_16(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_16_18(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_18_20(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_20_40(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 0.5),
    PF_HU_VS_AI_SHOVE_SAFE_BB_40_PLUS(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 0.5),

    PF_HU_VS_AI_SHOVE_UNSAFE_BB_0_2(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.05, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_2_4(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.1, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_4_6(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.2, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_6_8(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.3, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_8_10(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.4, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_10_12(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.5, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_12_14(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.6, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_14_16(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.7, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_16_18(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.8, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_18_20(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, ActionTweak.maxByMultipliedAllinOrFold(ActionVector.from(0.9, 1, 1, 1)), 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_20_40(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 0.5),
    PF_HU_VS_AI_SHOVE_UNSAFE_BB_40_PLUS(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, FeatureSchema.ACTION, FeatureSchema.DIFF_AMOUNT_BY_POT, ActionVector.stdCaller, 0.5);

    private final PokerSituation pokerSituation;
    private final FeatureSchema actionFeatureSchema;
    private final FeatureSchema amountFeatureSchema;
    private final ActionMap defaultActionMap;
    private final ActionVector defaultActionVector;
    private final ActionTweak actionTweak;
    private final AmountTweak amountTweak;
    private final double defaultAmount;

    PreflopSpot(PokerSituation pokerSituation, FeatureSchema actionFeatureSchema, FeatureSchema amountFeatureSchema, ActionVector defaultActionVector, double defaultAmount) {
        this(pokerSituation, actionFeatureSchema, amountFeatureSchema, defaultActionVector, ActionTweak.maxByMultiply, defaultAmount);
    }

    PreflopSpot(PokerSituation pokerSituation, FeatureSchema actionFeatureSchema, FeatureSchema amountFeatureSchema, ActionVector defaultActionVector, ActionTweak actionTweak, double defaultAmount) {
        this.pokerSituation = pokerSituation;
        this.actionFeatureSchema = actionFeatureSchema;
        this.amountFeatureSchema = amountFeatureSchema;
        this.defaultActionVector = defaultActionVector;
        this.defaultActionMap = ActionMap.from(defaultActionVector);
        this.actionTweak = actionTweak;
        this.defaultAmount = defaultAmount;
        this.amountTweak = AmountTweak.identity;
    }

    PreflopSpot(PokerSituation pokerSituation, FeatureSchema actionFeatureSchema, FeatureSchema amountFeatureSchema, ActionVector defaultActionVector, ActionTweak actionTweak, AmountTweak amountTweak, double defaultAmount) {
        this.pokerSituation = pokerSituation;
        this.actionFeatureSchema = actionFeatureSchema;
        this.amountFeatureSchema = amountFeatureSchema;
        this.defaultActionVector = defaultActionVector;
        this.defaultActionMap = ActionMap.from(defaultActionVector);
        this.actionTweak = actionTweak;
        this.amountTweak = amountTweak;
        this.defaultAmount = defaultAmount;
    }

    public static PreflopSpot from(PokerGame pokerGame) {
        PokerSituation pokerSituation = pokerGame.getState();
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        return from(pokerSituation, pokerGame.getBbAmount(), hero.getStackStub());
    }

    public static PreflopSpot from(PokerSituation pokerSituation, int bbAmount, int stackStub) {
        if (PokerSituation.mainVpipSituations.contains(pokerSituation)) {
            EffStackType effStackType = EffStackType.computePrecise((double) stackStub / (double) bbAmount);
            return preflopSpot(pokerSituation, effStackType);
        } else {
            return preflopSpot(pokerSituation);
        }
    }

    public static PreflopSpot preflopSpot(PokerSituation pokerSituation, EffStackType effStackType) {
        return PreflopSpot.valueOf(preflopSpotStr(pokerSituation, effStackType));
    }

    public static PreflopSpot preflopSpot(PokerSituation pokerSituation) {
        return PreflopSpot.valueOf(preflopSpotStr(pokerSituation));
    }

    public static String preflopSpotStr(PokerSituation pokerSituation) {
        return pokerSituation.name();
    }

    public static String preflopSpotStr(PokerSituation pokerSituation, EffStackType effStackType) {
        return pokerSituation + "_" + effStackType;
    }

    @Override
    public PokerSituation pokerSituation() {
        return pokerSituation;
    }

    @Override
    public FeatureSchema actionFeatureSchema() {
        return actionFeatureSchema;
    }

    @Override
    public FeatureSchema amountFeatureSchema() {
        return amountFeatureSchema;
    }

    @Override
    public ActionMap defaultActionMap() {
        return isShortStack() ? ActionMap.pfAllinOrFold : defaultActionMap;
    }

    @Override
    public ActionVector defaultActionVector() {
        return defaultActionVector;
    }

    @Override
    public double defaultDiffAmountByPot() {
        return 0.3;
    }

    @Override
    public ActionTweak actionTweak() {
        return actionTweak;
    }

    @Override
    public AmountTweak amountTweak() {
        return amountTweak;
    }

    @Override
    public double defaultAmount() {
        return defaultAmount;
    }

    public boolean isShortStack() {
        return EffStackType.pfShortStacks.stream().anyMatch(effStackType -> this.name().contains(effStackType.name()));
    }

    public boolean isForceFallback() {
        return pokerSituation == PokerSituation.PF_FIRST_VS_9_CALLERS || pokerSituation == PokerSituation.PF_FIRST_VS_8_CALLERS;
    }

}
