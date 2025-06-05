package dev.asper.app.graphql;

import dev.asper.app.entity.Model;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ModelSheet {
    private List<Model> models;
    private SheetInfo sheetInfo;
}
