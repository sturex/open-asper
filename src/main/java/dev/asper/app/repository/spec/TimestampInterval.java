package dev.asper.app.repository.spec;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TimestampInterval {
    private Timestamp begin;
    private Timestamp end;
}
