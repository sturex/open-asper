package dev.asper.app.service;

public interface DatasetService {
    int createDatasetsAsync(String pokerGamesSchemaName, String pokerGamesTableName, String datasetsSchemaName);

    int createHoldemStatDatasetsAsync(String pokerGamesSchemaName, String pokerGamesTableName, String datasetsSchemaName);

}
