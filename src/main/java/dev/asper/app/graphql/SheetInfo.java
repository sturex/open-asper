package dev.asper.app.graphql;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SheetInfo {
    private int pageSize;
    private int pageNumber;
    private int totalPages;
    private int totalElements;
}
