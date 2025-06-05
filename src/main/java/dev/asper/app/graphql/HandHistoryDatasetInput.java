package dev.asper.app.graphql;

import java.util.List;

public record HandHistoryDatasetInput(String schemaName, String tableName, List<String> playerNames, boolean isEnabled) {
}
