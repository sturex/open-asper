package dev.asper.app.service.log;

import java.time.Instant;
import java.util.Map;
import java.util.function.Supplier;

public interface LogService {

    default void log(LogRecordCode logRecordCode, Exception e) {
        log(logRecordCode, Map.of("error", e.getMessage() != null ? e.getMessage() : e.getClass().getName()));
    }

    enum Units {
        SEC, MSEC
    }

    Instant log(Supplier<LogRecord> logRecordSupplier);

    default Instant log(LogRecordCode logRecordCode, Map<String, String> values) {
        return log(LogRecord.supply(logRecordCode, values));
    }

    void log(Instant startTime, Units units, Supplier<LogRecord> logRecordSupplier);

    default void log(Instant startTime, LogRecordCode logRecordCode, Map<String, String> values) {
        log(startTime, Units.SEC, LogRecord.supply(logRecordCode, values));
    }

    default void log(Instant startTime, Units units, LogRecordCode logRecordCode, Map<String, String> values) {
        log(startTime, units, LogRecord.supply(logRecordCode, values));
    }

    default void log(Instant startTime, Supplier<LogRecord> logRecordSupplier) {
        log(startTime, Units.SEC, logRecordSupplier);
    }

}
