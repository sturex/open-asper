package dev.asper.advice;

import dev.asper.poker.enums.Action;

import java.io.Serializable;

public record ActionTweak(ActionTweakStrategy actionTweakStrategy, ActionVector actionVector) implements Serializable {
    public static final ActionTweak allin = new ActionTweak(ActionTweakStrategy.ALLIN, ActionVector.identity);
    public static ActionTweak maxByIdentity = new ActionTweak(ActionTweakStrategy.MAX_IDENTITY, ActionVector.identity);
    public static ActionTweak randByMultiply = new ActionTweak(ActionTweakStrategy.RAND_MULTIPLY, ActionVector.identity);
    public static ActionTweak maxByMultiply = new ActionTweak(ActionTweakStrategy.MAX_MULTIPLY, ActionVector.identity);
    public static ActionTweak randomByIdentity = new ActionTweak(ActionTweakStrategy.RAND_IDENTITY, ActionVector.identity);
    public static ActionTweak maxByAllinOrFold = new ActionTweak(ActionTweakStrategy.MAX_A_OR_F, ActionVector.identity);

    public static ActionTweak maxByMultipliedAllinOrFold(ActionVector actionVector) {
        return new ActionTweak(ActionTweakStrategy.MAX_MULT_A_OR_F, actionVector);
    }
    public static ActionTweak maxByMultipliedAllinOrC(ActionVector actionVector) {
        return new ActionTweak(ActionTweakStrategy.MAX_MULT_A_OR_C, actionVector);
    }

    public static ActionTweak maxByMultiply(ActionVector actionVector) {
        return new ActionTweak(ActionTweakStrategy.MAX_MULTIPLY, actionVector);
    }

    public static ActionTweak randByMultiply(ActionVector actionVector) {
        return new ActionTweak(ActionTweakStrategy.RAND_MULTIPLY, actionVector);
    }

    public Action apply(ActionMap actionMap) {
        return actionTweakStrategy.apply(actionMap, actionVector);
    }

    @Override
    public String toString() {
        return actionTweakStrategy + ", " + actionVector;
    }
}
