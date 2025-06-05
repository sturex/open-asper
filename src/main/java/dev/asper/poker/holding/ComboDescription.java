package dev.asper.poker.holding;

public record ComboDescription(String description, double value) implements NormalizedNumeric {

    @Override
    public double normalizedNumeric() {
        return value;
    }
}
