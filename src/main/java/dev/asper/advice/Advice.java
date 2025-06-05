package dev.asper.advice;


import dev.asper.poker.enums.Action;

public record Advice(Action action, int amount, String message) {

    public Advice(Action action, int amount) {
        this(action, amount, "");
    }

    public static Advice fAdvice(String message) {
        return new Advice(Action.F, 0, message);
    }

    public static Advice fAdvice() {
        return new Advice(Action.F, 0, "");
    }

    public static Advice cAdvice(String message, int callAmount) {
        return new Advice(Action.C, callAmount, message);
    }

    public static Advice cAdvice(int callAmount) {
        return new Advice(Action.C, callAmount, "");
    }

    public static Advice aAdvice(String message, int stackStub) {
        return new Advice(Action.A, stackStub, message);
    }

    public static Advice aAdvice(int stackStub) {
        return new Advice(Action.A, stackStub, "");
    }

    public static Advice rAdvice(int amount, String message) {
        return new Advice(Action.R, amount, message);
    }

    public static Advice rAdvice(int amount) {
        return new Advice(Action.R, amount, "");
    }

    @Override
    public String toString() {
        return action + (action == Action.R ? String.valueOf(amount) : "") + ", message: " + message;
    }

    public Decision toDecision() {
        return new Decision(action, amount);
    }
}
