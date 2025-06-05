package dev.asper.advice;

import dev.asper.poker.enums.Action;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.stream.Collectors;

public class ActionMap extends EnumMap<Action, Double> implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 8882006899652481603L;
    public static ActionMap fold = from(ActionVector.fold);
    public static ActionMap call = from(ActionVector.call);
    public static ActionMap raise = from(ActionVector.raise);
    public static ActionMap allin = from(ActionVector.allin);
    public static ActionMap identity = from(ActionVector.identity);
    public static ActionMap pfAllinOrFold = ActionMap.from(ActionVector.pfAllinOrFold);

    private ActionMap() {
        super(Action.class);
    }

    private ActionMap(EnumMap<Action, ? extends Double> m) {
        super(m);
    }

    public static ActionMap from(ActionVector actionVector) {
        return from(actionVector.f(),
                actionVector.c(),
                actionVector.a(),
                actionVector.r());
    }

    public static ActionMap from(double f, double c, double a, double r, double minValueThreshold) {
        ActionMap actionMap = new ActionMap();
        double total = f + c + a + r;
        if (total == 0.0) {
            return fold;
        } else {
            double fv = f / total;
            double cv = c / total;
            double av = a / total;
            double rv = r / total;
            actionMap.put(Action.F, fv > minValueThreshold ? fv : 0.0);
            actionMap.put(Action.C, cv > minValueThreshold ? cv : 0.0);
            actionMap.put(Action.A, av > minValueThreshold ? av : 0.0);
            actionMap.put(Action.R, rv > minValueThreshold ? rv : 0.0);
            return actionMap;
        }
    }

    public static ActionMap from(double f, double c, double a, double r) {
        return from(f, c, a, r, 0.0);
    }

    public static ActionMap from(Collection<Action> actions) {
        EnumMap<Action, Long> map = actions.stream()
                .collect(Collectors.groupingBy(action -> action, () -> new EnumMap<>(Action.class), Collectors.counting()));
        return from(map.getOrDefault(Action.F, 0L),
                map.getOrDefault(Action.C, 0L),
                map.getOrDefault(Action.A, 0L),
                map.getOrDefault(Action.R, 0L));
    }

    public static ActionMap from(EnumMap<Action, Double> src) {
        return new ActionMap(src);
    }

    public ActionVector toActionVector() {
        return new ActionVector(
                getOrDefault(Action.F, 0.0),
                getOrDefault(Action.C, 0.0),
                getOrDefault(Action.A, 0.0),
                getOrDefault(Action.R, 0.0));
    }

    public ActionMap multiply(ActionMap actionMap) {
        return from(
                getOrDefault(Action.F, 0.0) * actionMap.getOrDefault(Action.F, 0.0),
                getOrDefault(Action.C, 0.0) * actionMap.getOrDefault(Action.C, 0.0),
                getOrDefault(Action.A, 0.0) * actionMap.getOrDefault(Action.A, 0.0),
                getOrDefault(Action.R, 0.0) * actionMap.getOrDefault(Action.R, 0.0));
    }

    public ActionMap multiply(ActionVector actionVector) {
        return from(
                getOrDefault(Action.F, 0.0) * actionVector.f(),
                getOrDefault(Action.C, 0.0) * actionVector.c(),
                getOrDefault(Action.A, 0.0) * actionVector.a(),
                getOrDefault(Action.R, 0.0) * actionVector.r());
    }

    public static ActionMap average(Collection<ActionMap> actionMaps) {
        double f = 0;
        double c = 0;
        double a = 0;
        double r = 0;
        for (ActionMap actionMap : actionMaps) {
            f += actionMap.getOrDefault(Action.F, 0.0);
            c += actionMap.getOrDefault(Action.C, 0.0);
            a += actionMap.getOrDefault(Action.A, 0.0);
            r += actionMap.getOrDefault(Action.R, 0.0);
        }
        return from(f, c, a, r);
    }

    @Override
    public ActionMap clone() {
        return (ActionMap) super.clone();
    }
}
