package dev.asper.common.feature;

import org.springframework.security.core.parameters.P;

import java.util.Objects;
import java.util.function.Function;

public class Descriptor<T> {
    private final Enum<?> name;
    private final T value;
    private final Function<T, Object> transformationFunction;

    public Descriptor(Enum<?> name, T value, Function<T, Object> transformationFunction) {
        this.transformationFunction = transformationFunction;
        this.name = name;
        this.value = value;
    }

    public Descriptor(Enum<?> name, T value) {
        this(name, value, t -> t);
    }

    public Enum<?> name() {
        return name;
    }

    public T value() {
        return value;
    }

    public Object transformedValue() {
        return transformationFunction.apply(value);
    }

    @Override
    public String toString() {
        return name.name() + ": " + value;
    }

    public boolean isValid() {
        if (value instanceof Double d) {
            return !Double.isNaN(d) && !Double.isInfinite(d);
        } else {
            return true;
        }
    }

}
