package dev.asper.app.component;

import java.io.Serial;

public class InsufficientDataException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -683940820475920571L;

    @Override
    public String getMessage() {
        return "Data is insufficient for training";
    }
}
