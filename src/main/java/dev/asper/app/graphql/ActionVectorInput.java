package dev.asper.app.graphql;

import dev.asper.advice.ActionMap;
import dev.asper.advice.ActionVector;

public record ActionVectorInput(double f, double c, double a, double r) {
    public static final ActionVectorInput fold = new ActionVectorInput(1.0, 0, 0, 0);
    public static final ActionVectorInput call = new ActionVectorInput(0, 1.0, 0, 0);
    public static final ActionVectorInput raise = new ActionVectorInput(0, 0, 1.0, 0);
    public static final ActionVectorInput allin = new ActionVectorInput(0, 0, 0, 1.0);

    public ActionMap toActionMap() {
        return ActionMap.from(f, c, a, r);
    }

    public ActionVector toActionVector() {
        return new ActionVector(f, c, a, r);
    }

    public static ActionVectorInput from(ActionVector actionVector) {
        return new ActionVectorInput(actionVector.f(), actionVector.c(), actionVector.a(), actionVector.r());
    }
}
