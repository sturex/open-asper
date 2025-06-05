package dev.asper.advice;

import java.io.Serializable;

public record WeightedAmount(double weight, double amount) implements Serializable {
}
