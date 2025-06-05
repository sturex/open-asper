package dev.asper.app.service.log;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Service
public class LogServiceImpl implements LogService {

    private final Logger log = Logger.getLogger("AsperLogger");

    @Override
    public Instant log(Supplier<LogRecord> logRecordSupplier) {
        LogRecord logRecord = logRecordSupplier.get();
        log.severe(logRecord.toString());
        return Instant.now();
    }

    @Override
    public void log(Instant startTime, Units units, Supplier<LogRecord> logRecordSupplier) {
        LogRecord logRecord = logRecordSupplier.get();
        String elapsed = units == Units.SEC ? Duration.between(startTime, Instant.now()).toSeconds() + " seconds" : Duration.between(startTime, Instant.now()).toMillis() + " milliseconds";
        log.severe(logRecord.toString() + " Elapsed time: " + elapsed);
    }
}
