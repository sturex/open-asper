package dev.asper.advice;


import dev.asper.poker.enums.Action;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;

public enum ActionTweakStrategy {
    RAND_A_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            double f = actionMap.getOrDefault(Action.F, 0.);
            return random.nextDouble() < f ? Action.F : Action.A;
        }
    },
    RAND_R_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            double f = actionMap.getOrDefault(Action.F, 0.);
            return random.nextDouble() < f ? Action.F : Action.R;
        }
    },
    RAND_C_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            double f = actionMap.getOrDefault(Action.F, 0.);
            return random.nextDouble() < f ? Action.F : Action.C;
        }
    },
    MAX_MULT_A_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            ActionMap multiplied = actionMap.multiply(actionVector);
            return MAX_A_OR_F.apply(multiplied, ActionVector.identity);
        }
    },
    MAX_MULT_R_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            ActionMap multiplied = actionMap.multiply(actionVector);
            return MAX_R_OR_F.apply(multiplied, ActionVector.identity);
        }
    },
    MAX_MULT_C_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            ActionMap multiplied = actionMap.multiply(actionVector);
            return MAX_C_OR_F.apply(multiplied, ActionVector.identity);
        }
    },
    MAX_MULT_A_OR_C {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            ActionMap multiplied = actionMap.multiply(actionVector);
            return MAX_A_OR_C.apply(multiplied, ActionVector.identity);
        }
    },
    MAX_A_OR_C {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            double c = actionMap.getOrDefault(Action.C, 0.);
            return c > 0.5 ? Action.C : Action.A;
        }
    },
    MAX_A_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            double f = actionMap.getOrDefault(Action.F, 0.);
            return f > 0.5 ? Action.F : Action.A;
        }
    },
    MAX_R_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            double f = actionMap.getOrDefault(Action.F, 0.);
            return f > 0.5 ? Action.F : Action.R;
        }
    },
    MAX_C_OR_F {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            double f = actionMap.getOrDefault(Action.F, 0.);
            return f > 0.5 ? Action.F : Action.C;
        }
    },
    MAX_IDENTITY {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            return actionMap.entrySet().stream()
                    .max(Comparator.comparingDouble(Map.Entry::getValue))
                    .map(Map.Entry::getKey)
                    .orElseThrow();
        }
    },
    RAND_IDENTITY {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            double randomValue = random.nextDouble();
            double cumulative = 0;
            for (Map.Entry<Action, Double> entry : actionMap.entrySet()) {
                cumulative += entry.getValue();
                if (cumulative >= randomValue) {
                    return entry.getKey();
                }
            }
            return MAX_IDENTITY.apply(actionMap, actionVector);
        }
    },
    MAX_MULTIPLY {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            ActionMap multiplied = actionMap.multiply(actionVector);
            return MAX_IDENTITY.apply(multiplied, actionVector);
        }
    },
    RAND_MULTIPLY {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            ActionMap multiplied = actionMap.multiply(actionVector);
            return RAND_IDENTITY.apply(multiplied, actionVector);
        }
    },
    MAX_REPLACE {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            ActionMap replaced = ActionMap.from(actionVector);
            return MAX_IDENTITY.apply(replaced, actionVector);
        }
    },
    RAND_REPLACE {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            ActionMap replaced = ActionMap.from(actionVector);
            return RAND_IDENTITY.apply(replaced, actionVector);
        }
    },
    ALLIN {
        @Override
        public Action apply(ActionMap actionMap, ActionVector actionVector) {
            return Action.A;
        }
    };

    private final static Random random = new Random();

    public abstract Action apply(ActionMap actionMap, ActionVector actionVector);
}
