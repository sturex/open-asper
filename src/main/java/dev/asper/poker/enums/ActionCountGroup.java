package dev.asper.poker.enums;

public enum ActionCountGroup {
    NONE, ONE, TWO, THREE_PLUS;

    public static ActionCountGroup ofInteger(int counter) {
        if (counter == 0) {
            return NONE;
        } else if (counter == 1) {
            return ONE;
        } else if (counter == 2) {
            return TWO;
        } else if (counter >= 3) {
            return THREE_PLUS;
        } else {
            throw new IllegalArgumentException("Illegal argument for ActionCountGroup: " + counter);
        }
    }

}
