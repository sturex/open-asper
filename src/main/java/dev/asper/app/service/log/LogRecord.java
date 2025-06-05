package dev.asper.app.service.log;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public record LogRecord(LogRecordCode logRecordCode, Map<String, String> values) {
    public static Supplier<LogRecord> supply(LogRecordCode logRecordCode) {
        return () -> new LogRecord(logRecordCode, Collections.emptyMap());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("mlPoolLogRecordCode").append(" -> ").append(logRecordCode).append("\n");
        values.forEach((s, s2) -> sb.append("\t").append(s).append(" -> ").append(s2).append("\n"));
        return sb.toString();
    }
    public static Supplier<LogRecord> supply(LogRecordCode logRecordCode, Map<String, String> values){
        return () -> new LogRecord(logRecordCode, values);
    }
}
