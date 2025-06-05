package dev.asper.app.component;

import java.io.Serial;

public class PipelineModelRecoverException extends Exception {
    @Serial
    private static final long serialVersionUID = -1376197509100354412L;
    private final Exception causeException;

    public PipelineModelRecoverException(Exception causeException) {
        this.causeException = causeException;
    }

    public Exception getCauseException() {
        return causeException;
    }

    @Override
    public String getMessage() {
        return "Model body is probably null";
    }
}
