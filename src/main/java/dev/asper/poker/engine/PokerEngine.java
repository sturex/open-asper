package dev.asper.poker.engine;

import dev.asper.advice.Decision;
import dev.asper.common.fsm.HashMapStateMachine;
import dev.asper.poker.card.Card;
import dev.asper.poker.card.Cards;
import dev.asper.poker.enums.*;

import java.util.List;


public class PokerEngine extends HashMapStateMachine<PokerGameEventType, PokerSituation, PokerGame, PokerHand> {

    public PokerEngine() {
        this.addTransition(PokerSituation.BEGIN, PokerGameEventType.BEGIN, (pokerGame, idx, pokerHand) -> switch (pokerGame.getBoardSize()) {
                    case HEADS_UP -> PokerSituation.PF_HU_SB_FIRST;
                    case SIZE_3 -> PokerSituation.PF_FIRST_BTN;
                    case SIZE_4 -> PokerSituation.PF_FIRST_CO;
                    case SIZE_5 -> PokerSituation.PF_FIRST_VS_4_CALLERS;
                    case SIZE_6 -> PokerSituation.PF_FIRST_VS_5_CALLERS;
                    case SIZE_7 -> PokerSituation.PF_FIRST_VS_6_CALLERS;
                    case SIZE_8 -> PokerSituation.PF_FIRST_VS_7_CALLERS;
                    case SIZE_9 -> PokerSituation.PF_FIRST_VS_8_CALLERS;
                    case SIZE_10 -> PokerSituation.PF_FIRST_VS_9_CALLERS;
                }).addTransition(PokerSituation.PF_FIRST_VS_9_CALLERS, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_FIRST_VS_8_CALLERS;
                        case C -> PokerSituation.PF_FP_VS_FP_LIMP;
                        case R -> PokerSituation.PF_FP_VS_FP_OPEN_RAISER;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FIRST_VS_8_CALLERS, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_FIRST_VS_7_CALLERS;
                        case C -> PokerSituation.PF_FP_VS_FP_LIMP;
                        case R -> PokerSituation.PF_FP_VS_FP_OPEN_RAISER;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FIRST_VS_7_CALLERS, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_FIRST_VS_6_CALLERS;
                        case C -> PokerSituation.PF_FP_VS_FP_LIMP;
                        case R -> PokerSituation.PF_FP_VS_FP_OPEN_RAISER;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FIRST_VS_6_CALLERS, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_FIRST_VS_5_CALLERS;
                        case C -> PokerSituation.PF_FP_VS_FP_LIMP;
                        case R -> PokerSituation.PF_FP_VS_FP_OPEN_RAISER;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FIRST_VS_5_CALLERS, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_FIRST_VS_4_CALLERS;
                        case C -> PokerSituation.PF_FP_VS_FP_LIMP;
                        case R -> PokerSituation.PF_FP_VS_FP_OPEN_RAISER;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FIRST_VS_4_CALLERS, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_FIRST_CO;
                        case C -> PokerSituation.PF_FP_VS_FP_LIMP;
                        case R -> {
                            PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                    .map(MoveStatistics::preflopPosition)
                                    .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                            yield switch (r1Position.to6maxName()) {
                                case UTG1 -> PokerSituation.PF_CO_VS_UTG_OPEN_RAISER;
                                case MP1 -> PokerSituation.PF_CO_VS_MP_OPEN_RAISER;
                                default -> throw new IllegalStateException("Unexpected value: " + r1Position.to6maxName());
                            };
                        }
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FIRST_CO, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_FIRST_BTN;
                        case C -> PokerSituation.PF_FP_VS_FP_LIMP;
                        case R -> PokerSituation.PF_BTN_VS_CO_OPEN_RAISER;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FIRST_BTN, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_FIRST_SB;
                        case C -> PokerSituation.PF_SB_VS_FP_LIMP;
                        case R -> PokerSituation.PF_SB_VS_BTN_OPEN_RAISER;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FIRST_SB, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_COMPLETED;
                        case C -> PokerSituation.PF_BB_VS_FP_LIMP;
                        case R -> PokerSituation.PF_BB_VS_SB_OPEN_RAISER;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FP_VS_FP_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    return switch (action) {
                        case F -> switch (heroPosition) {
                            case CO -> {
                                PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                        .map(MoveStatistics::preflopPosition)
                                        .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                                yield switch (r1Position.to6maxName()) {
                                    case MP1 -> PokerSituation.PF_CO_VS_MP_OPEN_RAISER;
                                    case UTG1 -> PokerSituation.PF_CO_VS_UTG_OPEN_RAISER;
                                    default -> throw new IllegalStateException("Unexpected r1Position=" + r1Position);
                                };
                            }
                            default -> PokerSituation.PF_FP_VS_FP_OPEN_RAISER;
                        };
                        case R -> switch (heroPosition) {
                            case CO -> PokerSituation.PF_CO_VS_FP_COLD_3BET;
                            default -> PokerSituation.PF_FP_VS_FP_COLD_3BET;
                        };
                        case C -> switch (heroPosition) {
                            case CO -> PokerSituation.PF_CO_VS_FP_OPEN_RAISER_COLD_CALL;
                            default -> PokerSituation.PF_FP_VS_FP_OPEN_RAISER_COLD_CALL;
                        };
                        case A -> {
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_CO_VS_UTG_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER;
                        case R -> PokerSituation.PF_BTN_VS_CO_COLD_3BET;
                        case C -> PokerSituation.PF_BTN_VS_FP_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_CO_VS_MP_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_BTN_VS_MP_OPEN_RAISER;
                        case R -> PokerSituation.PF_BTN_VS_CO_COLD_3BET;
                        case C -> PokerSituation.PF_BTN_VS_FP_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_VS_UTG_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_SB_VS_UTG_OPEN_RAISER;
                        case R -> PokerSituation.PF_SB_VS_BTN_COLD_3BET;
                        case C -> PokerSituation.PF_SB_VS_FP_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_VS_MP_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_SB_VS_MP_OPEN_RAISER;
                        case R -> PokerSituation.PF_SB_VS_BTN_COLD_3BET;
                        case C -> PokerSituation.PF_SB_VS_FP_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_VS_CO_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();

                    return switch (action) {
                        case F -> PokerSituation.PF_SB_VS_CO_OPEN_RAISER;
                        case R -> PokerSituation.PF_SB_VS_BTN_COLD_3BET;
                        case C -> PokerSituation.PF_SB_VS_CO_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_BTN_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_BTN_COLD_3BET;
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_CO_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_CO_COLD_3BET;
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_FP_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_FP_COLD_3BET;
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_BTN_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                    .map(MoveStatistics::preflopPosition)
                                    .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                            yield switch (r1Position.to6maxName()) {
                                case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_3BET;
                                case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_FP_3BET;
                                case CO -> PokerSituation.PF_CO_OPEN_RAISER_VS_FP_3BET;
                                default -> yieldBrokenGame();
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_CO_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                    .map(MoveStatistics::preflopPosition)
                                    .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                            yield switch (r1Position.to6maxName()) {
                                case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_3BET;
                                case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_FP_3BET;
                                default -> yieldBrokenGame();
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_FP_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    return switch (action) {
                        case F -> {
                            PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                    .map(MoveStatistics::preflopPosition)
                                    .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                            yield switch (r1Position.to6maxName()) {
                                case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_3BET;
                                case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_FP_3BET;
                                default -> yieldBrokenGame();
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_UTG_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_UTG_OPEN_RAISER;
                        case R -> PokerSituation.PF_BB_VS_SB_COLD_3BET;
                        case C -> PokerSituation.PF_BB_VS_FP_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_MP_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_MP_OPEN_RAISER;
                        case R -> PokerSituation.PF_BB_VS_SB_COLD_3BET;
                        case C -> PokerSituation.PF_BB_VS_FP_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_CO_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_CO_OPEN_RAISER;
                        case R -> PokerSituation.PF_BB_VS_SB_COLD_3BET;
                        case C -> PokerSituation.PF_BB_VS_CO_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_BTN_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_BTN_OPEN_RAISER;
                        case R -> PokerSituation.PF_BB_VS_SB_COLD_3BET;
                        case C -> PokerSituation.PF_BB_VS_BTN_OPEN_RAISER_COLD_CALL;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_UTG_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_3BET;
                        case C -> PokerSituation.PF_COMPLETED;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_MP_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_3BET;
                        case C -> PokerSituation.PF_COMPLETED;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_CO_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_3BET;
                        case C -> PokerSituation.PF_COMPLETED;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_3BET;
                        case C -> PokerSituation.PF_COMPLETED;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_SB_OPEN_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_SB_OPEN_RAISER_VS_BB_3BET;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_OPEN_RAISER_VS_BB_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_ANY_VS_4BET_PLUS, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_MP_OPEN_RAISER_VS_FP_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_CO_OPEN_RAISER_VS_FP_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_ANY_VS_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_FP_SQUEEZE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_FP_SQUEEZE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_MP_OPEN_RAISER_VS_FP_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_FP_SQUEEZE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_FP_SQUEEZE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_SBBB_SQUEEZE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_COLD_CALLER_VS_FP_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_FP_SQUEEZE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_COLD_CALLER_VS_FP_SQUEEZE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FP_VS_FP_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            yield switch (heroPosition) {
                                case CO -> PokerSituation.PF_CO_VS_FP_COLD_3BET;
                                default -> PokerSituation.PF_FP_VS_FP_COLD_3BET;
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FP_VS_FP_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            yield switch (heroPosition) {
                                case CO -> PokerSituation.PF_CO_VS_FP_SQUEEZE;
                                default -> PokerSituation.PF_FP_VS_FP_SQUEEZE;
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_CO_VS_FP_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_BTN_VS_FP_SQUEEZE;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_VS_FP_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_SB_VS_FP_SQUEEZE;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_VS_CO_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_SB_VS_CO_SQUEEZE;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_CO_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_CO_SQUEEZE;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_SB_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            {
                                PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                        .map(MoveStatistics::preflopPosition)
                                        .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                                yield switch (r1Position.to6maxName()) {
                                    case CO -> PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                    case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                    case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                    default -> throw new IllegalStateException("Unexpected value: " + r1Position.to6maxName());
                                };
                            }
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_BTN_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            {
                                PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                        .map(MoveStatistics::preflopPosition)
                                        .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                                yield switch (r1Position.to6maxName()) {
                                    case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_SQUEEZE;
                                    case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_FP_SQUEEZE;
                                    default -> throw new IllegalStateException("Unexpected value: " + r1Position.to6maxName());
                                };
                            }
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_CO_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                    .map(MoveStatistics::preflopPosition)
                                    .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                            yield switch (r1Position.to6maxName()) {
                                case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_SQUEEZE;
                                case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_FP_SQUEEZE;
                                default -> throw new IllegalStateException("Unexpected value: " + r1Position.to6maxName());
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_FP_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                    .map(MoveStatistics::preflopPosition)
                                    .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                            yield switch (r1Position.to6maxName()) {
                                case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_SQUEEZE;
                                case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_FP_SQUEEZE;
                                default -> throw new IllegalStateException("Unexpected value: " + r1Position.to6maxName());
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_FP_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_FP_SQUEEZE;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_BTN_SQUEEZE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_BTN_SQUEEZE;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                            .map(MoveStatistics::preflopPosition)
                            .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                    PreflopPosition squeezerPosition = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R && m.idxDecisionType() == 1)
                            .map(MoveStatistics::preflopPosition)
                            .findFirst().orElseThrow(() -> new RuntimeException("Squeezer not found"));
                    return switch (action) {
                        case F, C -> {
                            if (r1Position == heroPosition) {
                                if (squeezerPosition.isBlindPosition()) {
                                    yield switch (r1Position.to6maxName()) {
                                        case CO -> PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                        case BTN -> PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                        case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                        case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                        default -> throw new IllegalStateException("Unexpected value: " + r1Position.to6maxName());
                                    };
                                } else {
                                    yield switch (r1Position.to6maxName()) {
                                        case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_FP_SQUEEZE;
                                        case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_FP_SQUEEZE;
                                        default -> throw new IllegalStateException("Unexpected value: " + r1Position.to6maxName());
                                    };
                                }
                            } else {
                                yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_COLD_SQUEEZE_CALL;
                            }
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_SQUEEZE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_CO_VS_FP_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_BTN_VS_FP_COLD_3BET;
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_VS_CO_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_SB_VS_CO_COLD_3BET;
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_VS_FP_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.PF_SB_VS_FP_COLD_3BET;
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_ANY_VS_COLD_4BET_PLUS, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_ANY_VS_COLD_3BET_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_SB_COLD_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> {
                            PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                    .map(MoveStatistics::preflopPosition)
                                    .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                            yield switch (r1Position.to6maxName()) {
                                case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_3BET;
                                case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_3BET;
                                case CO -> PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_3BET;
                                case BTN -> PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_3BET;
                                default -> yieldBrokenGame();
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COLD_4BET_PLUS;
                        case C -> PokerSituation.PF_ANY_VS_COLD_3BET_CALL;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FP_VS_FP_OPEN_RAISER_COLD_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    return switch (action) {
                        case F, C -> switch (heroPosition) {
                            case CO -> PokerSituation.PF_CO_VS_FP_OPEN_RAISER_COLD_CALL;
                            default -> PokerSituation.PF_FP_VS_FP_OPEN_RAISER_COLD_CALL;
                        };
                        case R -> switch (heroPosition) {
                            case CO -> PokerSituation.PF_CO_VS_FP_SQUEEZE;
                            default -> PokerSituation.PF_FP_VS_FP_SQUEEZE;
                        };
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_CO_VS_FP_OPEN_RAISER_COLD_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_BTN_VS_FP_OPEN_RAISER_COLD_CALL;
                        case R -> PokerSituation.PF_BTN_VS_CO_SQUEEZE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BTN_VS_FP_OPEN_RAISER_COLD_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_SB_VS_FP_OPEN_RAISER_COLD_CALL;
                        case R -> PokerSituation.PF_SB_VS_BTN_SQUEEZE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_FP_OPEN_RAISER_COLD_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_BB_VS_FP_OPEN_RAISER_COLD_CALL;
                        case R -> PokerSituation.PF_BB_VS_SB_SQUEEZE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_CO_OPEN_RAISER_COLD_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_BB_VS_CO_OPEN_RAISER_COLD_CALL;
                        case R -> PokerSituation.PF_BB_VS_SB_SQUEEZE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_BTN_OPEN_RAISER_COLD_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_BTN_OPEN_RAISER_VS_SBBB_SQUEEZE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_CO_OPEN_RAISER_COLD_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_CO_OPEN_RAISER_VS_SBBB_SQUEEZE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_FP_OPEN_RAISER_COLD_CALL, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> {
                            PreflopPosition r1Position = pokerGame.getPfMoveStats().stream().filter(m -> m.decision().action() == Action.R)
                                    .map(MoveStatistics::preflopPosition)
                                    .findFirst().orElseThrow(() -> new RuntimeException("First raiser not found"));
                            yield switch (r1Position.to6maxName()) {
                                case UTG1 -> PokerSituation.PF_UTG_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                case MP1 -> PokerSituation.PF_MP_OPEN_RAISER_VS_SBBB_SQUEEZE;
                                default -> throw new IllegalStateException("Unexpected value: " + r1Position.to6maxName());
                            };
                        }
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_VS_AI_SHOVE_UNSAFE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_VS_AI_SHOVE_SAFE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_VS_AI_SHOVE_SAFE;
                        }
                        case R, A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_HU_VS_AI_SHOVE_SAFE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    return PokerSituation.PF_COMPLETED;
                }).addTransition(PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    return PokerSituation.PF_COMPLETED;
                }).addTransition(PokerSituation.PF_AI_PUSH_SAFE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                        case C -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                        case R, A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_AI_PUSH_UNSAFE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                        case R, A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_VS_AI_SHOVE_SAFE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                        case C -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                        case R, A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_HU_SB_FIRST, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();

                    return switch (action) {
                        case F -> PokerSituation.PF_COMPLETED;
                        case C -> PokerSituation.PF_HU_BB_VS_SB_LIMP;
                        case R -> PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            yield hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition) ? PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE : PokerSituation.PF_HU_VS_AI_SHOVE_SAFE;
                        }
                    };
                }).addTransition(PokerSituation.PF_HU_BB_VS_SB_LIMP, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_HU_SB_LIMPER_VS_BB_BET;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            yield hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition) ? PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE : PokerSituation.PF_HU_VS_AI_SHOVE_SAFE;
                        }
                    };
                }).addTransition(PokerSituation.PF_HU_BB_VS_SB_OPEN_RAISE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_HU_SB_VS_BB_3BET;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                yield hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition) ? PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE : PokerSituation.PF_HU_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_HU_SB_LIMPER_VS_BB_BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_HU_BB_VS_SB_LIMP_RAISE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                yield hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition) ? PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE : PokerSituation.PF_HU_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_HU_BB_VS_SB_LIMP_RAISE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_HU_SB_VS_BB_3BET;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                yield hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition) ? PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE : PokerSituation.PF_HU_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_FP_LIMP, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        case A -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_COMPLETE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();

                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        case A -> {
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_HU_SB_VS_BB_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_HU_BB_VS_SB_4BET;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                yield hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition) ? PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE : PokerSituation.PF_HU_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_HU_BB_VS_SB_4BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> PokerSituation.PF_COMPLETED;
                        case R -> PokerSituation.PF_HU_SB_VS_BB_3BET;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                yield hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition) ? PokerSituation.PF_HU_VS_AI_SHOVE_UNSAFE : PokerSituation.PF_HU_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FP_VS_FP_LIMP, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    return switch (action) {
                        case F, C -> heroPosition == PreflopPosition.SB ? PokerSituation.PF_SB_VS_FP_LIMP : PokerSituation.PF_FP_VS_FP_LIMP;
                        case R -> heroPosition == PreflopPosition.SB ? PokerSituation.PF_SB_VS_FP_ISOLATE : PokerSituation.PF_FP_VS_FP_ISOLATE;
                        case A -> {
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_FP_LIMP, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_FP_LIMP;
                        case C -> PokerSituation.PF_BB_VS_COMPLETE;
                        case R -> PokerSituation.PF_BB_VS_SB_ISOLATE;
                        case A -> {
                            if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                            } else {
                                yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FP_VS_FP_ISOLATE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();

                    return switch (action) {
                        case F, C -> heroPosition == PreflopPosition.SB ? PokerSituation.PF_SB_VS_FP_ISOLATE : PokerSituation.PF_FP_VS_FP_ISOLATE;
                        case R -> heroPosition == PreflopPosition.SB ? PokerSituation.PF_SB_VS_FP_ISOLATE_3BET : PokerSituation.PF_FP_VS_FP_ISOLATE_3BET;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_SB_VS_FP_ISOLATE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();

                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_FP_ISOLATE;
                        case C -> PokerSituation.PF_BB_VS_SB_ISOLATE;
                        case R -> PokerSituation.PF_BB_VS_SB_ISOLATE_3BET;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_SB_ISOLATE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();

                    return switch (action) {
                        case F, C -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        case R -> PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_BB_VS_FP_ISOLATE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();

                    return switch (action) {
                        case F, C -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        case R -> PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        }
                        case C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_ISOLATED_LIMP_RAISER;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (pokerGame.calculateCallersCount() == 0) {
                                    yield PokerSituation.PF_COMPLETED;
                                } else {
                                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                    PreflopPosition heroPosition = hero.getPreflopPosition();
                                    if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                        yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                    } else {
                                        yield PokerSituation.PF_AI_PUSH_SAFE;
                                    }
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_FP_LIMPER_VS_FP_ISOLATE, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_FP_LIMPER_VS_FP_ISOLATE;
                        }
                        case R -> PokerSituation.PF_ANY_VS_ISOLATED_LIMP_RAISER;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.PF_ANY_VS_ISOLATED_LIMP_RAISER, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> {
                            yield pokerGame.calculateCallersCount() == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_ISOLATED_LIMP_RAISER;
                        }
                        case R -> PokerSituation.PF_ANY_VS_ISOLATED_LIMP_RAISER;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                })
                .addTransition(PokerSituation.PF_FP_VS_FP_ISOLATE_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            yield switch (heroPosition) {
                                case SB -> PokerSituation.PF_SB_VS_FP_ISOLATE_3BET;
                                case BB -> PokerSituation.PF_BB_VS_SB_ISOLATE_3BET;
                                default -> PokerSituation.PF_FP_VS_FP_ISOLATE_3BET;
                            };
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                })
                .addTransition(PokerSituation.PF_BB_VS_SB_ISOLATE_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    return switch (action) {
                        case F -> PokerSituation.PF_FP_LIMPER_VS_SBBB_ISOLATE;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                })
                .addTransition(PokerSituation.PF_SB_VS_FP_ISOLATE_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    return switch (action) {
                        case F -> PokerSituation.PF_BB_VS_FP_ISOLATE_3BET;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                })

                .addTransition(PokerSituation.PF_BB_VS_FP_ISOLATE_3BET, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                    PreflopPosition heroPosition = hero.getPreflopPosition();
                    return switch (action) {
                        case F -> PokerSituation.PF_FP_LIMPER_VS_FP_ISOLATE;
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case C -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_VS_AI_SHOVE_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_VS_AI_SHOVE_SAFE;
                                }
                            }
                        }
                    };
                })
                .addTransition(PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.pfMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F, C -> {
                            long callersCount = pokerGame.calculateCallersCount();
                            yield callersCount == 0 ? PokerSituation.PF_COMPLETED : PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        }
                        case R -> PokerSituation.PF_ANY_VS_COMPLEX_ISOLATE_POT;
                        case A -> {
                            if (pokerGame.calculateCallersCount() == 0) {
                                yield PokerSituation.PF_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (hero.getStackStub() <= pokerGame.computeCallAmount(heroPosition)) {
                                    yield PokerSituation.PF_AI_PUSH_UNSAFE;
                                } else {
                                    yield PokerSituation.PF_AI_PUSH_SAFE;
                                }
                            }
                        }
                    };
                })

                .addTransition(PokerSituation.GAME_BROKEN, PokerGameEventType.PF_MOVE, (pokerGame, idx, pokerHand) -> PokerSituation.GAME_BROKEN)
                .addTransition(PokerSituation.GAME_BROKEN, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> PokerSituation.GAME_BROKEN)
                .addTransition(PokerSituation.GAME_BROKEN, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> PokerSituation.GAME_BROKEN)
                .addTransition(PokerSituation.GAME_BROKEN, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> PokerSituation.GAME_BROKEN)
                .addTransition(PokerSituation.GAME_BROKEN, PokerGameEventType.FL_CARDS, (pokerGame, idx, pokerHand) -> PokerSituation.GAME_BROKEN)
                .addTransition(PokerSituation.GAME_BROKEN, PokerGameEventType.TN_CARD, (pokerGame, idx, pokerHand) -> PokerSituation.GAME_BROKEN)
                .addTransition(PokerSituation.GAME_BROKEN, PokerGameEventType.RV_CARD, (pokerGame, idx, pokerHand) -> PokerSituation.GAME_BROKEN)
                .addTransition(PokerSituation.GAME_BROKEN, PokerGameEventType.END, (pokerGame, idx, pokerHand) -> PokerSituation.GAME_BROKEN)

                .addTransition(PokerSituation.PF_COMPLETED, PokerGameEventType.FL_CARDS, (pokerGame, idx, pokerHand) -> {
                    Cards flCards = pokerHand.flCards();
                    pokerGame.setFlCards(flCards);
                    pokerGame.forEachPlayerFilteringFolded(pokerPlayer -> pokerPlayer.setFlCards(flCards));
                    pokerGame.computePfCompletedFeatures();
                    pokerGame.updateNextPlayer();
                    List<PokerPlayer> players = pokerGame.getPlayers();
                    int playersSize = players.size();
                    if (playersSize <= 1) {
                        return PokerSituation.FL_COMPLETED;
                    } else {
                        if (playersSize == 2) {
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            PokerPlayer opponent = players.stream().filter(pokerPlayer -> pokerPlayer.getPreflopPosition() != heroPosition).findFirst().orElseThrow(() -> new RuntimeException("Opponent not found"));
                            PreflopPosition oppPosition = opponent.getPreflopPosition();
                            PfPotType pfPotType = pokerGame.getPfPotType();
                            if (pokerGame.getBoardSize() == BoardSize.HEADS_UP) {
                                return PokerSituation.FL_HU_OOP_PFHU_BB_CHECKER;
                            } else return switch (pfPotType) {
                                case LIMP -> {
                                    if (heroPosition == PreflopPosition.SB && oppPosition == PreflopPosition.BB) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_SRP_SBCVSBB;
                                    } else if (heroPosition == PreflopPosition.BB && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_SRP_SBBBCVSFP;
                                    } else {
                                        yield yieldBrokenGame();
                                    }
                                }
                                case SRP -> {
                                    if (heroPosition == PreflopPosition.SB && oppPosition == PreflopPosition.BB) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_SRP_SBRVSBB;
                                    } else if (heroPosition.isBlindPosition() && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_SRP_SBBBCVSFP;
                                    } else if (heroPosition.isFreePosition() && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_SRP_FPRVSFP;
                                    } else {
                                        yield yieldBrokenGame();
                                    }
                                }
                                case MRP -> {
                                    if (heroPosition == PreflopPosition.SB && oppPosition == PreflopPosition.BB) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_SBVSBB;
                                    } else if (heroPosition.isBlindPosition() && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_SBBBVSFP;
                                    } else if (heroPosition.isFreePosition() && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_R1_FPVSFP;
                                    } else {
                                        yield yieldBrokenGame();
                                    }
                                }
                                case ISO -> {
                                    if (heroPosition.isBlindPosition() && oppPosition.isBlindPosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_SBVSBB;
                                    } else if (heroPosition.isBlindPosition() && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_SBBBVSFP;
                                    } else if (heroPosition.isFreePosition() && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_R1_FPVSFP;
                                    } else {
                                        yield yieldBrokenGame();
                                    }
                                }
                                case AIP -> {
                                    if (heroPosition == PreflopPosition.SB && oppPosition == PreflopPosition.BB) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_SBVSBB;
                                    } else if (heroPosition.isBlindPosition() && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_SBBBVSFP;
                                    } else if (heroPosition.isFreePosition() && oppPosition.isFreePosition()) {
                                        yield PokerSituation.FL_HU_OOP_CHECKER_MRP_R1_FPVSFP;
                                    } else {
                                        yield yieldBrokenGame();
                                    }
                                }
                                default -> throw new IllegalStateException("Unexpected value: " + pfPotType);
                            };
                        } else {
                            return PokerSituation.FL_MWP_BTW_CHECKER;
                        }
                    }
                }).addTransition(PokerSituation.FL_COMPLETED, PokerGameEventType.TN_CARD, (pokerGame, idx, pokerHand) -> {
                    Card tnCard = pokerHand.tnCard();
                    pokerGame.setTnCard(tnCard);
                    pokerGame.forEachPlayerFilteringFolded(pokerPlayer -> pokerPlayer.setTnCard(tnCard));
                    pokerGame.computeFlCompletedFeatures();
                    pokerGame.updateNextPlayer();
                    int playersSize = pokerGame.getPlayers().size();
                    if (playersSize <= 1) {
                        return PokerSituation.TN_COMPLETED;
                    } else if (playersSize == 2) {
                        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                        PreflopPosition heroPosition = hero.getPreflopPosition();
                        HeroActionType heroFlActionType = pokerGame.computeFlHeroActionType(heroPosition);
                        PostflopPotType flPotType = pokerGame.getFlPotType();
                        return switch (flPotType) {
                            case MXP -> PokerSituation.TN_HU_OOP_CHECKER_FLX;
                            case SRP, MRP, AIP -> heroFlActionType.isRaiser() ? PokerSituation.TN_HU_OOP_CHECKER_FLR : PokerSituation.TN_HU_OOP_CHECKER_FLC;
                            default -> throw new IllegalStateException("Unexpected value: " + flPotType);
                        };
                    } else {
                        return PokerSituation.TN_MWP_BTW_CHECKER;
                    }
                }).addTransition(PokerSituation.TN_COMPLETED, PokerGameEventType.RV_CARD, (pokerGame, idx, pokerHand) -> {
                    Card rvCard = pokerHand.rvCard();
                    pokerGame.setRvCard(rvCard);
                    pokerGame.forEachPlayerFilteringFolded(pokerPlayer -> pokerPlayer.setRvCard(rvCard));
                    pokerGame.computeTnCompletedFeatures();
                    pokerGame.updateNextPlayer();
                    int playersSize = pokerGame.getPlayers().size();
                    if (playersSize <= 1) {
                        return PokerSituation.RV_COMPLETED;
                    } else if (playersSize == 2) {
                        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                        HeroActionType heroTnActionType = pokerGame.computeTnHeroActionType(hero.getPreflopPosition());
                        PostflopPotType tnPotType = pokerGame.getTnPotType();
                        return switch (tnPotType) {
                            case MXP -> PokerSituation.RV_HU_OOP_CHECKER_TNX;
                            case SRP, MRP, AIP -> heroTnActionType.isRaiser() ? PokerSituation.RV_HU_OOP_CHECKER_TNR : PokerSituation.RV_HU_OOP_CHECKER_TNC;
                            default -> yieldBrokenGame();
                        };
                    } else {
                        return PokerSituation.RV_MWP_BTW_CHECKER;
                    }
                }).addTransition(PokerSituation.PF_COMPLETED, PokerGameEventType.END, (pokerGame, idx, pokerHand) -> PokerSituation.END).addTransition(PokerSituation.FL_COMPLETED, PokerGameEventType.END, (pokerGame, idx, pokerHand) -> PokerSituation.END).addTransition(PokerSituation.TN_COMPLETED, PokerGameEventType.END, (pokerGame, idx, pokerHand) -> PokerSituation.END).addTransition(PokerSituation.RV_COMPLETED, PokerGameEventType.END, (pokerGame, idx, pokerHand) -> PokerSituation.END)


                .addTransition(PokerSituation.FL_HU_OOP_CHECKER_SRP_SBBBCVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.FL_COMPLETED;
                        case C -> PokerSituation.FL_HU_IP_CHECKER_SRP_FPRVSSBBB;
                        case R, A -> PokerSituation.FL_HU_IP_CALLER_SRP_FPRVSSBBB;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CHECKER_SRP_FPRVSSBBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> PokerSituation.FL_HU_OOP_CALLER_SRP_SBBBCVSFP;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CALLER_SRP_SBBBCVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.FL_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_CALLER_SRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CALLER_SRP_FPRVSSBBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_CALLER_SRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CALLER_SRP_VS_RAISE, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.FL_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_CALLER_SRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CALLER_SRP_VS_RAISE, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_CALLER_SRP_VS_RAISE;
                    };
                })


                .addTransition(PokerSituation.FL_HU_OOP_CHECKER_SRP_FPRVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.FL_COMPLETED;
                        case C -> PokerSituation.FL_HU_IP_CHECKER_SRP_FPCVSFP;
                        case R, A -> PokerSituation.FL_HU_IP_CALLER_SRP_FPCVSFP;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CHECKER_SRP_FPCVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> PokerSituation.FL_HU_OOP_CALLER_SRP_FPRVSFP;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CALLER_SRP_FPRVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_CALLER_SRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CALLER_SRP_FPCVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_CALLER_SRP_VS_RAISE;
                    };
                })


                .addTransition(PokerSituation.FL_HU_OOP_CHECKER_MRP_SBVSBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.FL_COMPLETED;
                        case C -> PokerSituation.FL_HU_IP_CHECKER_BBVSSB;
                        case R, A -> PokerSituation.FL_HU_IP_CALLER_BBVSSB;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CHECKER_SRP_SBRVSBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.FL_COMPLETED;
                        case C -> PokerSituation.FL_HU_IP_CHECKER_BBVSSB;
                        case R, A -> PokerSituation.FL_HU_IP_CALLER_BBVSSB;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CHECKER_SRP_SBCVSBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.FL_COMPLETED;
                        case C -> PokerSituation.FL_HU_IP_CHECKER_BBVSSB;
                        case R, A -> PokerSituation.FL_HU_IP_CALLER_BBVSSB;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CHECKER_BBVSSB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> PokerSituation.FL_HU_OOP_CALLER_SBVSBB;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CALLER_SBVSBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_CALLER_BBVSSB_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CALLER_BBVSSB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_CALLER_SBVSBB_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CALLER_SBVSBB_VS_RAISE, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_CALLER_BBVSSB_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CALLER_BBVSSB_VS_RAISE, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_CALLER_SBVSBB_VS_RAISE;
                    };
                })


                .addTransition(PokerSituation.FL_HU_OOP_CHECKER_MRP_R1_FPVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.FL_COMPLETED;
                        case C -> PokerSituation.FL_HU_IP_CHECKER_MRP_FPVSFP;
                        case R, A -> PokerSituation.FL_HU_IP_CALLER_MRP_FPVSFP;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CHECKER_MRP_FPVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> PokerSituation.FL_HU_OOP_CALLER_MRP_FPVSFP;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CALLER_MRP_FPVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_CALLER_MRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CALLER_MRP_FPVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_CALLER_MRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CALLER_MRP_VS_RAISE, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_CALLER_MRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CALLER_MRP_VS_RAISE, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_CALLER_MRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CHECKER_MRP_SBBBVSFP, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.FL_COMPLETED;
                        case C -> PokerSituation.FL_HU_IP_CHECKER_MRP_FPVSSBBB;
                        case R, A -> PokerSituation.FL_HU_IP_CALLER_MRP_FPVSSBBB;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CHECKER_MRP_FPVSSBBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> PokerSituation.FL_HU_OOP_CALLER_MRP_SBBB;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_CALLER_MRP_SBBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_CALLER_MRP_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_CALLER_MRP_FPVSSBBB, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_CALLER_MRP_VS_RAISE;
                    };
                })
                .addTransition(PokerSituation.FL_HU_OOP_PFHU_BB_CHECKER, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case F -> PokerSituation.FL_COMPLETED;
                        case C -> PokerSituation.FL_HU_IP_PFHU_SB_CHECKER;
                        case R, A -> PokerSituation.FL_HU_IP_PFHU_SB_CALLER;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_PFHU_SB_CHECKER, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> PokerSituation.FL_HU_OOP_PFHU_BB_CALLER;
                    };
                }).addTransition(PokerSituation.FL_HU_OOP_PFHU_BB_CALLER, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_IP_PFHU_SB_CALLER;
                    };
                }).addTransition(PokerSituation.FL_HU_IP_PFHU_SB_CALLER, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.FL_COMPLETED : PokerSituation.FL_HU_OOP_PFHU_BB_CALLER;
                    };
                })
                .addTransition(PokerSituation.TN_HU_OOP_CHECKER_FLX, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> PokerSituation.TN_HU_IP_CHECKER;
                        case F -> PokerSituation.TN_COMPLETED;
                        case R, A -> PokerSituation.TN_HU_IP_CALLER;
                    };
                }).addTransition(PokerSituation.TN_HU_OOP_CHECKER_FLC, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> PokerSituation.TN_HU_IP_CHECKER;
                        case F -> PokerSituation.TN_COMPLETED;
                        case R, A -> PokerSituation.TN_HU_IP_CALLER;
                    };
                }).addTransition(PokerSituation.TN_HU_OOP_CHECKER_FLR, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> PokerSituation.TN_HU_IP_CHECKER;
                        case F -> PokerSituation.TN_COMPLETED;
                        case R, A -> PokerSituation.TN_HU_IP_CALLER;
                    };
                }).addTransition(PokerSituation.TN_HU_IP_CHECKER, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.TN_COMPLETED;
                        case R, A -> PokerSituation.TN_HU_OOP_CALLER;
                    };
                }).addTransition(PokerSituation.TN_HU_OOP_CALLER, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.TN_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.TN_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.TN_COMPLETED : PokerSituation.TN_HU_IP_CALLER_VS_RAISE;
                    };
                }).addTransition(PokerSituation.TN_HU_IP_CALLER, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.TN_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.TN_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.TN_COMPLETED : PokerSituation.TN_HU_OOP_CALLER_VS_RAISE;
                    };
                }).addTransition(PokerSituation.TN_HU_OOP_CALLER_VS_RAISE, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.TN_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.TN_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.TN_COMPLETED : PokerSituation.TN_HU_IP_CALLER_VS_RAISE;
                    };
                }).addTransition(PokerSituation.TN_HU_IP_CALLER_VS_RAISE, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.TN_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.TN_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.TN_COMPLETED : PokerSituation.TN_HU_OOP_CALLER_VS_RAISE;
                    };
                })


                .addTransition(PokerSituation.RV_HU_OOP_CHECKER_TNX, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> PokerSituation.RV_HU_IP_CHECKER;
                        case F -> PokerSituation.RV_COMPLETED;
                        case R, A -> PokerSituation.RV_HU_IP_CALLER;
                    };
                }).addTransition(PokerSituation.RV_HU_OOP_CHECKER_TNC, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> PokerSituation.RV_HU_IP_CHECKER;
                        case F -> PokerSituation.RV_COMPLETED;
                        case R, A -> PokerSituation.RV_HU_IP_CALLER;
                    };
                }).addTransition(PokerSituation.RV_HU_OOP_CHECKER_TNR, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> PokerSituation.RV_HU_IP_CHECKER;
                        case F -> PokerSituation.RV_COMPLETED;
                        case R, A -> PokerSituation.RV_HU_IP_CALLER;
                    };
                }).addTransition(PokerSituation.RV_HU_IP_CHECKER, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.RV_COMPLETED;
                        case R, A -> PokerSituation.RV_HU_OOP_CALLER;
                    };
                }).addTransition(PokerSituation.RV_HU_OOP_CALLER, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.RV_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.RV_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.RV_COMPLETED : PokerSituation.RV_HU_IP_CALLER_VS_RAISE;
                    };
                }).addTransition(PokerSituation.RV_HU_IP_CALLER, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.RV_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.RV_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.RV_COMPLETED : PokerSituation.RV_HU_OOP_CALLER_VS_RAISE;
                    };
                }).addTransition(PokerSituation.RV_HU_OOP_CALLER_VS_RAISE, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.RV_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.RV_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.RV_COMPLETED : PokerSituation.RV_HU_IP_CALLER_VS_RAISE;
                    };
                }).addTransition(PokerSituation.RV_HU_IP_CALLER_VS_RAISE, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    if (pokerGame.getPlayers().size() == 0) {
                        return PokerSituation.RV_COMPLETED;
                    } else return switch (action) {
                        case C, F -> PokerSituation.RV_COMPLETED;
                        case R, A -> pokerGame.calculateCallersCount() == 0 ? PokerSituation.RV_COMPLETED : PokerSituation.RV_HU_OOP_CALLER_VS_RAISE;
                    };
                }).addTransition(PokerSituation.FL_MWP_BTW_CHECKER, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case C -> {
                            int checksCount = (int) pokerGame.getMoveStatistics().stream().filter(s -> s.action() == Action.C).count();
                            int playersSize = pokerGame.getPlayers().size();
                            yield playersSize - checksCount == 1 ? PokerSituation.FL_MWP_LAST_CHECKER : PokerSituation.FL_MWP_BTW_CHECKER;
                        }
                        case F -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int playersSize = players.size();
                            PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                            PreflopPosition heroPosition = hero.getPreflopPosition();
                            if (playersSize == 2) {
                                PokerPlayer opponent = players.stream().filter(pokerPlayer -> pokerPlayer.getPreflopPosition() != heroPosition)
                                        .findFirst().orElseThrow(() -> new RuntimeException("Opponent not found"));
                                PreflopPosition oppPosition = opponent.getPreflopPosition();
                                if (heroPosition.isFreePosition() && oppPosition.isFreePosition()) {
                                    yield PokerSituation.FL_HU_IP_CHECKER_MRP_FPVSFP;
                                } else if (heroPosition.isFreePosition() && oppPosition.isBlindPosition()) {
                                    yield PokerSituation.FL_HU_IP_CHECKER_MRP_FPVSSBBB;
                                } else {
                                    yield yieldBrokenGame();
                                }
                            } else {
                                int checksCount = (int) pokerGame.getMoveStatistics().stream().filter(s -> s.action() == Action.C).count();
                                yield playersSize - checksCount == 1 ? PokerSituation.FL_MWP_LAST_CHECKER : PokerSituation.FL_MWP_BTW_CHECKER;
                            }
                        }
                        case R, A -> PokerSituation.FL_MWP_BTW_CALLER;
                    };
                }).addTransition(PokerSituation.FL_MWP_LAST_CHECKER, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R, A -> PokerSituation.FL_MWP_BTW_CALLER;
                    };
                }).addTransition(PokerSituation.FL_MWP_BTW_CALLER, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> {
                            long callersCount = pokerGame.calculateCallersCount();
                            yield callersCount == 1 ? PokerSituation.FL_MWP_LAST_CALLER : (callersCount > 1 ? PokerSituation.FL_MWP_BTW_CALLER : PokerSituation.GAME_BROKEN);
                        }
                        case F -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int size = players.size();
                            long callersCount = pokerGame.calculateCallersCount();
                            if (callersCount == 0) {
                                yield PokerSituation.FL_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (size == 1) {
                                    if (pokerGame.calculateCallersCount() == 0) {
                                        yield PokerSituation.FL_COMPLETED;
                                    } else if (heroPosition.isBlindPosition()) {
                                        yield PokerSituation.FL_HU_OOP_CALLER_SRP_SBBBCVSFP;
                                    } else {
                                        yield PokerSituation.FL_HU_IP_CALLER_MRP_FPVSFP;
                                    }
                                } else {
                                    if (callersCount == 1 && size == 2) {
                                        PokerPlayer opponent = players.stream().filter(pokerPlayer -> pokerPlayer.getPreflopPosition() != heroPosition).findFirst().orElseThrow(() -> new RuntimeException("Opponent not found"));
                                        PreflopPosition oppPosition = opponent.getPreflopPosition();
                                        if (heroPosition == PreflopPosition.SB && oppPosition == PreflopPosition.BB) {
                                            yield PokerSituation.FL_HU_OOP_CALLER_SBVSBB;
                                        } else if (heroPosition.isBlindPosition() && oppPosition.isFreePosition()) {
                                            yield PokerSituation.FL_HU_OOP_CALLER_SRP_SBBBCVSFP;
                                        } else if (heroPosition.isFreePosition() && oppPosition.isBlindPosition()) {
                                            yield PokerSituation.FL_HU_IP_CALLER_MRP_FPVSSBBB;
                                        } else if (heroPosition.isFreePosition() && oppPosition.isFreePosition()) {
                                            yield PokerSituation.FL_HU_IP_CALLER_MRP_FPVSFP;
                                        } else {
                                            yield yieldBrokenGame();
                                        }
                                    } else {
                                        if (callersCount == 1) {
                                            yield PokerSituation.FL_MWP_LAST_CALLER;
                                        } else {
                                            yield PokerSituation.FL_MWP_BTW_CALLER;
                                        }
                                    }
                                }
                            }
                        }
                        case R, A -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int size = players.size();
                            if (size == 1) {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (heroPosition.isBlindPosition()) {
                                    yield PokerSituation.FL_HU_OOP_CALLER_SRP_SBBBCVSFP;
                                } else {
                                    yield PokerSituation.FL_HU_IP_CALLER_MRP_FPVSFP;
                                }
                            } else {
                                long callersCount = pokerGame.calculateCallersCount();
                                if (callersCount == 0) {
                                    yield PokerSituation.FL_MWP_LAST_CALLER;
                                } else {
                                    yield PokerSituation.FL_MWP_BTW_CALLER;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.FL_MWP_LAST_CALLER, PokerGameEventType.FL_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.flMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.FL_COMPLETED;
                        case R -> PokerSituation.FL_MWP_BTW_CALLER;
                        case A -> {
                            long callersCount = pokerGame.calculateCallersCount();
                            if (callersCount == 0) {
                                yield PokerSituation.FL_COMPLETED;
                            } else if (callersCount == 1) {
                                yield PokerSituation.FL_MWP_LAST_CALLER;
                            } else {
                                yield PokerSituation.FL_MWP_BTW_CALLER;
                            }
                        }
                    };
                })


                .addTransition(PokerSituation.TN_MWP_BTW_CHECKER, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> {
                            int checksCount = (int) pokerGame.getMoveStatistics().stream().filter(s -> s.action() == Action.C).count();
                            int playersSize = pokerGame.getPlayers().size();
                            yield playersSize - checksCount == 1 ? PokerSituation.TN_MWP_LAST_CHECKER : PokerSituation.TN_MWP_BTW_CHECKER;
                        }
                        case F -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int playersSize = players.size();
                            if (playersSize == 2) {
                                yield PokerSituation.TN_HU_IP_CHECKER;
                            } else {
                                int checksCount = (int) pokerGame.getMoveStatistics().stream().filter(s -> s.action() == Action.C).count();
                                yield playersSize - checksCount == 1 ? PokerSituation.TN_MWP_LAST_CHECKER : PokerSituation.TN_MWP_BTW_CHECKER;
                            }
                        }
                        case R, A -> PokerSituation.TN_MWP_BTW_CALLER;
                    };
                }).addTransition(PokerSituation.TN_MWP_LAST_CHECKER, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.TN_COMPLETED;
                        case R, A -> PokerSituation.TN_MWP_BTW_CALLER;
                    };
                }).addTransition(PokerSituation.TN_MWP_BTW_CALLER, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C -> {
                            long callersCount = pokerGame.calculateCallersCount();
                            yield callersCount == 1 ? PokerSituation.TN_MWP_LAST_CALLER : (callersCount > 1 ? PokerSituation.TN_MWP_BTW_CALLER : PokerSituation.GAME_BROKEN);
                        }
                        case F -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int size = players.size();
                            long callersCount = pokerGame.calculateCallersCount();
                            if (callersCount == 0) {
                                yield PokerSituation.TN_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (callersCount == 1 && size == 2) {
                                    if (heroPosition.isBlindPosition()) {
                                        yield PokerSituation.TN_HU_OOP_CALLER;
                                    } else {
                                        yield PokerSituation.TN_HU_IP_CALLER;
                                    }
                                } else {
                                    if (callersCount == 1) {
                                        yield PokerSituation.TN_MWP_LAST_CALLER;
                                    } else {
                                        yield PokerSituation.TN_MWP_BTW_CALLER;
                                    }
                                }
                            }
                        }
                        case R, A -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int size = players.size();
                            if (size == 1) {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (heroPosition.isBlindPosition()) {
                                    yield PokerSituation.TN_HU_OOP_CALLER;
                                } else {
                                    yield PokerSituation.TN_HU_IP_CALLER;
                                }
                            } else {
                                long callersCount = pokerGame.calculateCallersCount();
                                if (callersCount == 0) {
                                    yield PokerSituation.TN_MWP_LAST_CALLER;
                                } else {
                                    yield PokerSituation.TN_MWP_BTW_CALLER;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.TN_MWP_LAST_CALLER, PokerGameEventType.TN_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.tnMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);

                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.TN_COMPLETED;
                        case R -> PokerSituation.TN_MWP_BTW_CALLER;
                        case A -> {
                            long callersCount = pokerGame.calculateCallersCount();
                            if (callersCount == 0) {
                                yield PokerSituation.TN_COMPLETED;
                            } else if (callersCount == 1) {
                                yield PokerSituation.TN_MWP_LAST_CALLER;
                            } else {
                                yield PokerSituation.TN_MWP_BTW_CALLER;
                            }
                        }
                    };
                })


                .addTransition(PokerSituation.RV_MWP_BTW_CHECKER, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case C -> {
                            int checksCount = (int) pokerGame.getMoveStatistics().stream().filter(s -> s.action() == Action.C).count();
                            int playersSize = pokerGame.getPlayers().size();
                            yield playersSize - checksCount == 1 ? PokerSituation.RV_MWP_LAST_CHECKER : PokerSituation.RV_MWP_BTW_CHECKER;
                        }
                        case F -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int playersSize = players.size();
                            if (playersSize == 2) {
                                yield PokerSituation.RV_HU_IP_CHECKER;
                            } else {
                                int checksCount = (int) pokerGame.getMoveStatistics().stream().filter(s -> s.action() == Action.C).count();
                                yield playersSize - checksCount == 1 ? PokerSituation.RV_MWP_LAST_CHECKER : PokerSituation.RV_MWP_BTW_CHECKER;
                            }
                        }
                        case R, A -> PokerSituation.RV_MWP_BTW_CALLER;
                    };
                }).addTransition(PokerSituation.RV_MWP_LAST_CHECKER, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.RV_COMPLETED;
                        case R, A -> PokerSituation.RV_MWP_BTW_CALLER;
                    };
                }).addTransition(PokerSituation.RV_MWP_BTW_CALLER, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case C -> {
                            long callersCount = pokerGame.calculateCallersCount();
                            yield callersCount == 1 ? PokerSituation.RV_MWP_LAST_CALLER : (callersCount > 1 ? PokerSituation.RV_MWP_BTW_CALLER : PokerSituation.GAME_BROKEN);
                        }
                        case F -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int size = players.size();
                            long callersCount = pokerGame.calculateCallersCount();
                            if (callersCount == 0) {
                                yield PokerSituation.RV_COMPLETED;
                            } else {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (callersCount == 1 && size == 2) {
                                    if (heroPosition.isBlindPosition()) {
                                        yield PokerSituation.RV_HU_OOP_CALLER;
                                    } else {
                                        yield PokerSituation.RV_HU_IP_CALLER;
                                    }
                                } else {
                                    if (callersCount == 1) {
                                        yield PokerSituation.RV_MWP_LAST_CALLER;
                                    } else {
                                        yield PokerSituation.RV_MWP_BTW_CALLER;
                                    }
                                }
                            }
                        }
                        case R, A -> {
                            List<PokerPlayer> players = pokerGame.getPlayers();
                            int size = players.size();
                            if (size == 1) {
                                PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
                                PreflopPosition heroPosition = hero.getPreflopPosition();
                                if (heroPosition.isBlindPosition()) {
                                    yield PokerSituation.RV_HU_OOP_CALLER;
                                } else {
                                    yield PokerSituation.RV_HU_IP_CALLER;
                                }
                            } else {
                                long callersCount = pokerGame.calculateCallersCount();
                                if (callersCount == 0) {
                                    yield PokerSituation.RV_MWP_LAST_CALLER;
                                } else {
                                    yield PokerSituation.RV_MWP_BTW_CALLER;
                                }
                            }
                        }
                    };
                }).addTransition(PokerSituation.RV_MWP_LAST_CALLER, PokerGameEventType.RV_MOVE, (pokerGame, idx, pokerHand) -> {
                    Move move = pokerHand.rvMoves().get(idx);
                    Decision decision = pokerGame.pushMove(move);
                    Action action = decision.action();
                    return switch (action) {
                        case C, F -> PokerSituation.RV_COMPLETED;
                        case R -> PokerSituation.RV_MWP_BTW_CALLER;
                        case A -> {
                            long callersCount = pokerGame.calculateCallersCount();
                            if (callersCount == 0) {
                                yield PokerSituation.RV_COMPLETED;
                            } else if (callersCount == 1) {
                                yield PokerSituation.RV_MWP_LAST_CALLER;
                            } else {
                                yield PokerSituation.RV_MWP_BTW_CALLER;
                            }
                        }
                    };
                });
    }

    private PokerSituation yieldBrokenGame() {
        return PokerSituation.GAME_BROKEN;
    }
}
