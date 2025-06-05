package dev.asper.app.graphql;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Pagination {
    @Positive
    private int pageSize;
    @PositiveOrZero
    private int pageNumber;
}
