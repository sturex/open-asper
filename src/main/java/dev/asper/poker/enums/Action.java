package dev.asper.poker.enums;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

public enum Action {
    F("F"),
    C("C"),
    A("A"),
    R("R");

    public static final EnumSet<Action> allActions = Arrays.stream(Action.values()).collect(Collectors.toCollection(() -> EnumSet.noneOf(Action.class)));
    public static final EnumSet<Action> callerActions = allActions;
    public static final EnumSet<Action> checkerActions = EnumSet.of(C, A, R);
    public static final EnumSet<Action> allinOrFoldActions = EnumSet.of(A, F);
    public static final EnumSet<Action> noneActions = EnumSet.noneOf(Action.class);
    public static final EnumSet<Action> rAction = EnumSet.of(Action.R);
    private final String letter;

    Action(String letter) {
        this.letter = letter;
    }

    public String letter() {
        return letter;
    }
}
