package dev.asper.poker.engine;

import dev.asper.common.feature.Feature;
import dev.asper.common.fsm.FeaturedState;
import dev.asper.poker.enums.Action;
import dev.asper.poker.enums.Street;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum PokerSituation implements FeaturedState<PokerGame> {

    BEGIN(Street.PF, Collections.emptyList(), Collections.emptyList(), Action.noneActions),
    END(Street.RV, Collections.emptyList(), Collections.emptyList(), noneActions()),
    FL_COMPLETED(Street.FL, Collections.emptyList(), Collections.emptyList(), noneActions()),
    FL_HU_IP_CALLER_BBVSSB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_CALLER_BBVSSB_VS_RAISE(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_CALLER_MRP_FPVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_CALLER_MRP_FPVSSBBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_CALLER_MRP_VS_RAISE(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_CALLER_SRP_FPCVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_CALLER_SRP_FPRVSSBBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_CALLER_SRP_VS_RAISE(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_CHECKER_BBVSSB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_IP_CHECKER_MRP_FPVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_IP_CHECKER_MRP_FPVSSBBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_IP_CHECKER_SRP_FPCVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_IP_CHECKER_SRP_FPRVSSBBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_IP_PFHU_SB_CALLER(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_IP_PFHU_SB_CHECKER(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_OOP_CALLER_MRP_FPVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_CALLER_MRP_SBBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_CALLER_MRP_VS_RAISE(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_CALLER_SBVSBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_CALLER_SBVSBB_VS_RAISE(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_CALLER_SRP_FPRVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_CALLER_SRP_SBBBCVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_CALLER_SRP_VS_RAISE(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_CHECKER_MRP_R1_FPVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_OOP_CHECKER_MRP_SBBBVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_OOP_CHECKER_MRP_SBVSBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_OOP_CHECKER_SRP_FPRVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_OOP_CHECKER_SRP_SBBBCVSFP(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_OOP_CHECKER_SRP_SBCVSBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_OOP_CHECKER_SRP_SBRVSBB(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_HU_OOP_PFHU_BB_CALLER(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_HU_OOP_PFHU_BB_CHECKER(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_MWP_BTW_CALLER(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_MWP_BTW_CHECKER(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    FL_MWP_LAST_CALLER(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.callerActions),
    FL_MWP_LAST_CHECKER(Street.FL, PokerFeatures.flFeatures, PokerFeatures.flServiceFeatures, Action.checkerActions),
    GAME_BROKEN(Street.RV, Collections.emptyList(), Collections.emptyList(), noneActions()),
    PF_AI_PUSH_SAFE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_AI_PUSH_UNSAFE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.allinOrFoldActions),
    PF_ANY_VS_4BET_PLUS(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_ANY_VS_COLD_3BET_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_ANY_VS_COLD_4BET_PLUS(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_ANY_VS_COLD_SQUEEZE_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_ANY_VS_COMPLEX_ISOLATE_POT(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_ANY_VS_COMPLEX_SQUEEZE_POT(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_ANY_VS_ISOLATED_LIMP_RAISER(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_BB_VS_BTN_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_BTN_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_BTN_OPEN_RAISER_COLD_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_BTN_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_COMPLETE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.checkerActions),
    PF_BB_VS_CO_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_CO_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_CO_OPEN_RAISER_COLD_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_CO_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_FP_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BB_VS_BTN_COLD_3BET;
        }
    },
    PF_BB_VS_FP_ISOLATE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_BB_VS_FP_ISOLATE_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_BB_VS_FP_LIMP(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.checkerActions),
    PF_BB_VS_FP_OPEN_RAISER_COLD_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BB_VS_FP_COLD_3BET;
        }
    },
    PF_BB_VS_FP_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BB_VS_FP_COLD_3BET;
        }
    },
    PF_BB_VS_MP_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_SB_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_SB_ISOLATE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BB_VS_SB_OPEN_RAISER;
        }
    },
    PF_BB_VS_SB_ISOLATE_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BB_VS_SB_COLD_3BET;
        }
    },
    PF_BB_VS_SB_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BB_VS_SB_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BB_VS_SB_COLD_3BET;
        }
    },
    PF_BB_VS_UTG_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_OPEN_RAISER_VS_SBBB_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_OPEN_RAISER_VS_SBBB_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_3BET;
        }
    },
    PF_BTN_VS_CO_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_VS_CO_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_VS_CO_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_VS_FP_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_VS_FP_OPEN_RAISER_COLD_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_VS_FP_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_VS_MP_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_BTN_VS_UTG_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_COLD_CALLER_VS_FP_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_3BET;
        }
    },
    PF_COLD_CALLER_VS_SBBB_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_3BET;
        }
    },
    PF_COMPLETED(Street.PF, Collections.emptyList(), Collections.emptyList(), noneActions()),
    PF_CO_OPEN_RAISER_VS_FP_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_3BET;
        }
    },
    PF_CO_OPEN_RAISER_VS_SBBB_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_CO_OPEN_RAISER_VS_SBBB_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_3BET;
        }
    },
    PF_CO_VS_FP_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_CO_VS_FP_OPEN_RAISER_COLD_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_CO_VS_MP_OPEN_RAISER;
        }
    },
    PF_CO_VS_FP_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_CO_VS_FP_COLD_3BET;
        }
    },
    PF_CO_VS_MP_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_CO_VS_UTG_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FIRST_BTN(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FIRST_CO(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FIRST_SB(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FIRST_VS_4_CALLERS(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FIRST_VS_5_CALLERS(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FIRST_VS_6_CALLERS(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FIRST_VS_7_CALLERS(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FIRST_VS_8_CALLERS(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_FIRST_VS_7_CALLERS;
        }
    },
    PF_FIRST_VS_9_CALLERS(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_FIRST_VS_7_CALLERS;
        }
    },
    PF_FP_LIMPER_VS_FP_ISOLATE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
        }
    },
    PF_FP_LIMPER_VS_SBBB_ISOLATE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FP_VS_FP_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_FP_VS_FP_ISOLATE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_FP_VS_FP_ISOLATE_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_COLD_3BET;
        }
    },
    PF_FP_VS_FP_LIMP(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.checkerActions),
    PF_FP_VS_FP_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_FP_VS_FP_OPEN_RAISER_COLD_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_FP_VS_FP_OPEN_RAISER;
        }
    },
    PF_FP_VS_FP_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_SQUEEZE;
        }
    },
    PF_HU_BB_VS_SB_4BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE;
        }
    },
    PF_HU_BB_VS_SB_LIMP(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.checkerActions),
    PF_HU_BB_VS_SB_LIMP_RAISE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_HU_BB_VS_SB_OPEN_RAISE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_HU_SB_FIRST(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_HU_SB_LIMPER_VS_BB_BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_HU_SB_VS_BB_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_HU_VS_AI_SHOVE_SAFE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_HU_VS_AI_SHOVE_UNSAFE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.allinOrFoldActions),
    PF_MP_OPEN_RAISER_VS_FP_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_MP_OPEN_RAISER_VS_FP_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_MP_OPEN_RAISER_VS_FP_3BET;
        }
    },
    PF_MP_OPEN_RAISER_VS_SBBB_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_MP_OPEN_RAISER_VS_SBBB_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_3BET;
        }
    },
    PF_SB_OPEN_RAISER_VS_BB_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_BTN_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_BTN_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_BTN_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_CO_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_CO_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_CO_OPEN_RAISER_COLD_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_SB_VS_CO_OPEN_RAISER;
        }
    },
    PF_SB_VS_CO_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_FP_COLD_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_FP_ISOLATE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_FP_ISOLATE_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_SB_VS_CO_COLD_3BET;
        }
    },
    PF_SB_VS_FP_LIMP(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.checkerActions),
    PF_SB_VS_FP_OPEN_RAISER_COLD_CALL(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_SB_VS_CO_OPEN_RAISER;
        }
    },
    PF_SB_VS_FP_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_MP_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SB_VS_UTG_OPEN_RAISER(Street.PF, PokerFeatures.pfFeaturesOppAware, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_SQUEEZE_POT(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_BTN_VS_CO_SQUEEZE;
        }
    },
    PF_UTG_OPEN_RAISER_VS_FP_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_UTG_OPEN_RAISER_VS_FP_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions) {
        @Override
        public PokerSituation fallback() {
            return PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_3BET;
        }
    },
    PF_UTG_OPEN_RAISER_VS_SBBB_3BET(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_UTG_OPEN_RAISER_VS_SBBB_SQUEEZE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_VS_AI_SHOVE_SAFE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.callerActions),
    PF_VS_AI_SHOVE_UNSAFE(Street.PF, PokerFeatures.pfFeatures, PokerFeatures.pfServiceFeatures, Action.allinOrFoldActions),
    RV_COMPLETED(Street.RV, Collections.emptyList(), Collections.emptyList(), noneActions()),
    RV_HU_IP_CALLER(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.callerActions),
    RV_HU_IP_CALLER_VS_RAISE(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.callerActions),
    RV_HU_IP_CHECKER(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.checkerActions),
    RV_HU_OOP_CALLER(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.callerActions),
    RV_HU_OOP_CALLER_VS_RAISE(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.callerActions),
    RV_HU_OOP_CHECKER_TNC(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.checkerActions),
    RV_HU_OOP_CHECKER_TNR(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.checkerActions),
    RV_HU_OOP_CHECKER_TNX(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.checkerActions),
    RV_MWP_BTW_CALLER(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.callerActions),
    RV_MWP_BTW_CHECKER(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.checkerActions),
    RV_MWP_LAST_CALLER(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.callerActions),
    RV_MWP_LAST_CHECKER(Street.RV, PokerFeatures.rvFeatures, PokerFeatures.rvServiceFeatures, Action.checkerActions),
    TN_COMPLETED(Street.TN, Collections.emptyList(), Collections.emptyList(), noneActions()),
    TN_HU_IP_CALLER(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.callerActions),
    TN_HU_IP_CALLER_VS_RAISE(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.callerActions),
    TN_HU_IP_CHECKER(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.checkerActions),
    TN_HU_OOP_CALLER(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.callerActions),
    TN_HU_OOP_CALLER_VS_RAISE(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.callerActions),
    TN_HU_OOP_CHECKER_FLC(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.checkerActions),
    TN_HU_OOP_CHECKER_FLR(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.checkerActions),
    TN_HU_OOP_CHECKER_FLX(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.checkerActions),
    TN_MWP_BTW_CALLER(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.callerActions),
    TN_MWP_BTW_CHECKER(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.checkerActions),
    TN_MWP_LAST_CALLER(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.callerActions),
    TN_MWP_LAST_CHECKER(Street.TN, PokerFeatures.tnFeatures, PokerFeatures.tnServiceFeatures, Action.checkerActions);

    private final static EnumSet<Action> noneActions = EnumSet.noneOf(Action.class);
    public final static EnumSet<PokerSituation> flCheckerSituationsHuIp = EnumSet.of(
            FL_HU_IP_CHECKER_BBVSSB,
            FL_HU_IP_CHECKER_MRP_FPVSFP,
            FL_HU_IP_CHECKER_MRP_FPVSSBBB,
            FL_HU_IP_CHECKER_SRP_FPCVSFP,
            FL_HU_IP_CHECKER_SRP_FPRVSSBBB,
            FL_HU_IP_PFHU_SB_CHECKER
    );
    public final static EnumSet<PokerSituation> flCheckerSituationsHuOop = EnumSet.of(
            FL_HU_OOP_CHECKER_MRP_R1_FPVSFP,
            FL_HU_OOP_CHECKER_MRP_SBBBVSFP,
            FL_HU_OOP_CHECKER_MRP_SBVSBB,
            FL_HU_OOP_CHECKER_SRP_FPRVSFP,
            FL_HU_OOP_CHECKER_SRP_SBBBCVSFP,
            FL_HU_OOP_CHECKER_SRP_SBCVSBB,
            FL_HU_OOP_CHECKER_SRP_SBRVSBB,
            FL_HU_OOP_PFHU_BB_CHECKER
    );
    public final static EnumSet<PokerSituation> flCallerSituationsHuIpVsBet = EnumSet.of(
            FL_HU_IP_CALLER_BBVSSB,
            FL_HU_IP_CALLER_MRP_FPVSFP,
            FL_HU_IP_CALLER_MRP_FPVSSBBB,
            FL_HU_IP_CALLER_SRP_FPCVSFP,
            FL_HU_IP_CALLER_SRP_FPRVSSBBB
    );
    public final static EnumSet<PokerSituation> flCallerSituationsHuIpVsRaise = EnumSet.of(
            FL_HU_IP_CALLER_BBVSSB_VS_RAISE,
            FL_HU_IP_CALLER_MRP_VS_RAISE,
            FL_HU_IP_CALLER_SRP_VS_RAISE
    );
    public final static EnumSet<PokerSituation> flCallerSituationsHuOopVsBet = EnumSet.of(
            FL_HU_OOP_CALLER_MRP_FPVSFP,
            FL_HU_OOP_CALLER_MRP_SBBB,
            FL_HU_OOP_CALLER_SBVSBB,
            FL_HU_OOP_CALLER_SRP_FPRVSFP,
            FL_HU_OOP_CALLER_SRP_SBBBCVSFP
    );
    public final static EnumSet<PokerSituation> flCallerSituationsHuOopVsRaise = EnumSet.of(
            FL_HU_OOP_CALLER_MRP_VS_RAISE,
            FL_HU_OOP_CALLER_SBVSBB_VS_RAISE,
            FL_HU_OOP_CALLER_SRP_VS_RAISE
    );
    public final static EnumSet<PokerSituation> postflopSituations = EnumSet.of(
            FL_HU_IP_CALLER_BBVSSB,
            FL_HU_IP_CALLER_BBVSSB_VS_RAISE,
            FL_HU_IP_CALLER_MRP_FPVSFP,
            FL_HU_IP_CALLER_MRP_FPVSSBBB,
            FL_HU_IP_CALLER_MRP_VS_RAISE,
            FL_HU_IP_CALLER_SRP_FPCVSFP,
            FL_HU_IP_CALLER_SRP_FPRVSSBBB,
            FL_HU_IP_CALLER_SRP_VS_RAISE,
            FL_HU_IP_CHECKER_BBVSSB,
            FL_HU_IP_CHECKER_MRP_FPVSFP,
            FL_HU_IP_CHECKER_MRP_FPVSSBBB,
            FL_HU_IP_CHECKER_SRP_FPCVSFP,
            FL_HU_IP_CHECKER_SRP_FPRVSSBBB,
            FL_HU_IP_PFHU_SB_CALLER,
            FL_HU_IP_PFHU_SB_CHECKER,
            FL_HU_OOP_CALLER_MRP_FPVSFP,
            FL_HU_OOP_CALLER_MRP_SBBB,
            FL_HU_OOP_CALLER_MRP_VS_RAISE,
            FL_HU_OOP_CALLER_SBVSBB,
            FL_HU_OOP_CALLER_SBVSBB_VS_RAISE,
            FL_HU_OOP_CALLER_SRP_FPRVSFP,
            FL_HU_OOP_CALLER_SRP_SBBBCVSFP,
            FL_HU_OOP_CALLER_SRP_VS_RAISE,
            FL_HU_OOP_CHECKER_MRP_R1_FPVSFP,
            FL_HU_OOP_CHECKER_MRP_SBBBVSFP,
            FL_HU_OOP_CHECKER_MRP_SBVSBB,
            FL_HU_OOP_CHECKER_SRP_FPRVSFP,
            FL_HU_OOP_CHECKER_SRP_SBBBCVSFP,
            FL_HU_OOP_CHECKER_SRP_SBCVSBB,
            FL_HU_OOP_CHECKER_SRP_SBRVSBB,
            FL_HU_OOP_PFHU_BB_CALLER,
            FL_HU_OOP_PFHU_BB_CHECKER,
            FL_MWP_BTW_CALLER,
            FL_MWP_BTW_CHECKER,
            FL_MWP_LAST_CALLER,
            FL_MWP_LAST_CHECKER,
            RV_HU_IP_CALLER,
            RV_HU_IP_CALLER_VS_RAISE,
            RV_HU_IP_CHECKER,
            RV_HU_OOP_CALLER,
            RV_HU_OOP_CALLER_VS_RAISE,
            RV_HU_OOP_CHECKER_TNC,
            RV_HU_OOP_CHECKER_TNR,
            RV_HU_OOP_CHECKER_TNX,
            RV_MWP_BTW_CALLER,
            RV_MWP_BTW_CHECKER,
            RV_MWP_LAST_CALLER,
            RV_MWP_LAST_CHECKER,
            TN_HU_IP_CALLER,
            TN_HU_IP_CALLER_VS_RAISE,
            TN_HU_IP_CHECKER,
            TN_HU_OOP_CALLER,
            TN_HU_OOP_CALLER_VS_RAISE,
            TN_HU_OOP_CHECKER_FLC,
            TN_HU_OOP_CHECKER_FLR,
            TN_HU_OOP_CHECKER_FLX,
            TN_MWP_BTW_CALLER,
            TN_MWP_BTW_CHECKER,
            TN_MWP_LAST_CALLER,
            TN_MWP_LAST_CHECKER
    );
    private final PokerSituation fallback = this;

    private static EnumSet<Action> noneActions() {
        return noneActions;
    }


    public static final EnumSet<PokerSituation> intermediateSituations = EnumSet.of(BEGIN, END, PF_COMPLETED, FL_COMPLETED, TN_COMPLETED, RV_COMPLETED, GAME_BROKEN);
    public static final EnumSet<PokerSituation> mainVpipSituations = EnumSet.of(
            PF_FIRST_BTN,
            PF_FIRST_CO,
            PF_FIRST_SB,
            PF_FIRST_VS_4_CALLERS,
            PF_FIRST_VS_5_CALLERS,
            PF_FIRST_VS_6_CALLERS,
            PF_FIRST_VS_7_CALLERS,
            PF_FIRST_VS_8_CALLERS,
            PF_FIRST_VS_9_CALLERS,
            PF_BB_VS_BTN_OPEN_RAISER,
            PF_BB_VS_CO_OPEN_RAISER,
            PF_BB_VS_MP_OPEN_RAISER,
            PF_BB_VS_SB_OPEN_RAISER,
            PF_BB_VS_UTG_OPEN_RAISER,
            PF_BTN_VS_CO_OPEN_RAISER,
            PF_BTN_VS_MP_OPEN_RAISER,
            PF_BTN_VS_UTG_OPEN_RAISER,
            PF_CO_VS_MP_OPEN_RAISER,
            PF_CO_VS_UTG_OPEN_RAISER,
            PF_FP_VS_FP_OPEN_RAISER,
            PF_SB_VS_BTN_OPEN_RAISER,
            PF_SB_VS_CO_OPEN_RAISER,
            PF_SB_VS_MP_OPEN_RAISER,
            PF_SB_VS_UTG_OPEN_RAISER,
            PF_BB_VS_FP_LIMP,
            PF_FP_VS_FP_LIMP,
            PF_SB_VS_FP_LIMP,
            PF_HU_BB_VS_SB_LIMP,
            PF_HU_BB_VS_SB_OPEN_RAISE,
            PF_HU_SB_FIRST,
            PF_HU_VS_AI_SHOVE_SAFE,
            PF_HU_VS_AI_SHOVE_UNSAFE,
            PF_VS_AI_SHOVE_SAFE,
            PF_VS_AI_SHOVE_UNSAFE
    );

    public static final EnumSet<PokerSituation> pfFirstSituations = EnumSet.of(
            PF_FIRST_BTN,
            PF_FIRST_CO,
            PF_FIRST_SB,
            PF_FIRST_VS_4_CALLERS,
            PF_FIRST_VS_5_CALLERS,
            PF_FIRST_VS_6_CALLERS,
            PF_FIRST_VS_7_CALLERS,
            PF_FIRST_VS_8_CALLERS,
            PF_FIRST_VS_9_CALLERS
    );

    public static final EnumSet<PokerSituation> vsOpenRaiserSituations = EnumSet.of(
            PF_BB_VS_BTN_OPEN_RAISER,
            PF_BB_VS_CO_OPEN_RAISER,
            PF_BB_VS_MP_OPEN_RAISER,
            PF_BB_VS_SB_OPEN_RAISER,
            PF_BB_VS_UTG_OPEN_RAISER,
            PF_BTN_VS_CO_OPEN_RAISER,
            PF_BTN_VS_MP_OPEN_RAISER,
            PF_BTN_VS_UTG_OPEN_RAISER,
            PF_CO_VS_MP_OPEN_RAISER,
            PF_CO_VS_UTG_OPEN_RAISER,
            PF_FP_VS_FP_OPEN_RAISER,
            PF_SB_VS_BTN_OPEN_RAISER,
            PF_SB_VS_CO_OPEN_RAISER,
            PF_SB_VS_MP_OPEN_RAISER,
            PF_SB_VS_UTG_OPEN_RAISER
    );

    public static final EnumSet<PokerSituation> oppAwareSituations = Stream.of(pfFirstSituations, vsOpenRaiserSituations)
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(PokerSituation.class)));
    public static final EnumSet<PokerSituation> allSituations = Arrays.stream(PokerSituation.values())
            .filter(pokerSituation -> !intermediateSituations.contains(pokerSituation))
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(PokerSituation.class)));

    private final Street street;
    private final List<Feature<?, PokerGame>> features;
    private final List<Feature<?, PokerGame>> serviceFeatures;
    private final EnumSet<Action> actionAllowedActions;

    PokerSituation(Street street, List<Feature<?, PokerGame>> features, List<Feature<?, PokerGame>> serviceFeatures, EnumSet<Action> actionAllowedActions) {
        this.street = street;
        this.features = features;
        this.serviceFeatures = serviceFeatures;
        this.actionAllowedActions = actionAllowedActions;
    }

    @Override
    public List<Feature<?, PokerGame>> getServiceFeatures() {
        return serviceFeatures;
    }

    @Override
    public List<Feature<?, PokerGame>> getFeatures() {
        return features;
    }

    @Override
    public List<Feature<?, PokerGame>> getLabels() {
        return PokerFeatures.labels;
    }

    public Street getStreet() {
        return street;
    }

    public EnumSet<Action> actionAllowedActions() {
        return actionAllowedActions;
    }

    public PokerSituation fallback() {
        return fallback;
    }
}
