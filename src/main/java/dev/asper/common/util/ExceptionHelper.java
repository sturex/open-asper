package dev.asper.common.util;

import java.util.function.Supplier;

public enum ExceptionHelper {
    ;

    public static void throwIf(boolean expression, Supplier<RuntimeException> exceptionSupplier) {
        if (expression) {
            throw exceptionSupplier.get();
        }
    }

    public static void throwIf(boolean expression, String message) {
        throwIf(expression, () -> new RuntimeException(message));
    }
}
