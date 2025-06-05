package dev.asper.advice;

import java.io.Serializable;

public record ActionVector(double f, double c, double a, double r) implements Serializable {

    public static final ActionVector fold = new ActionVector(1.0, 0, 0, 0);
    public static final ActionVector call = new ActionVector(0, 1.0, 0, 0);
    public static final ActionVector raise = new ActionVector(0, 0, 1.0, 0);
    public static final ActionVector allin = new ActionVector(0, 0, 0, 1.0);
    public static final ActionVector identity = new ActionVector(0.25, 0.25, 0.25, 0.25);

    public static final ActionVector stdCaller = new ActionVector(0.45, 0.45, 0.02, 0.08);
    public static final ActionVector pfRareSituationCaller = new ActionVector(0.70, 0.18, 0.02, 0.10);
    public static final ActionVector pfAllinOrFold = new ActionVector(0.70, 0.0, 0.3, 0.0);
    public static final ActionVector stdChecker = new ActionVector(0.0, 0.65, 0.01, 0.34);

    public static ActionVector from(double f, double c, double a, double r) {
        double total = f + c + a + r;
        return new ActionVector(f / total, c / total, a / total, r / total);
    }

    public String joinToCsv(String delimiter) {
        return f + delimiter + c + delimiter + a + delimiter + r + delimiter;
    }

    public String joinToCsv() {
        return joinToCsv(", ");
    }
}
