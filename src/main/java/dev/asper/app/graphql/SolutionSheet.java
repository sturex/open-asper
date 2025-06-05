package dev.asper.app.graphql;

import dev.asper.app.entity.SolutionInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SolutionSheet {
    private List<SolutionInfo> solutionInfos;
    private SheetInfo sheetInfo;
}
