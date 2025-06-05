package dev.asper.app.config;


import com.clickhouse.jdbc.ClickHouseDataSource;
import dev.asper.clickhouse.ClickhouseProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.spark.sql.execution.datasources.jdbc.JDBCOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "clickhouse")
@Data
public class ClickhouseConfig {
    public static final int CLICKHOUSE_BATCH_SIZE = 100000;
    @NotNull
    private String jdbcUrl;
    @NotNull
    private String password;
    @NotNull
    private String user;
    @NotNull
    private String socketTimeout;
    @NotNull
    private String queryTimeout;
    @NotNull
    private String driverClass;

    public Map<String, String> sparkOptions() {
        Map<String, String> chOptions = new HashMap<>();
        chOptions.put("url", jdbcUrl);
        chOptions.put(JDBCOptions.JDBC_DRIVER_CLASS(), driverClass);
        chOptions.put("user", user);
        chOptions.put("password", password);
        chOptions.put("socket_timeout", socketTimeout);
        chOptions.put(JDBCOptions.JDBC_QUERY_TIMEOUT(), queryTimeout);
        return chOptions;
    }
    @Bean
    ClickHouseDataSource clickHouseDataSource() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        return new ClickHouseDataSource(jdbcUrl, properties);
    }

    @Bean
    public ClickhouseProperties clickhouseProperties() {
        return new ClickhouseProperties(jdbcUrl, password, user, socketTimeout, queryTimeout, driverClass);
    }
}
