package dev.asper.app.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("data-storage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatasetSchemasConfig {
    private String holdemStatSchemaName;
    private String datasetSchemaName;
    private String playersSchemaName;
    private String playersTableName;
}
