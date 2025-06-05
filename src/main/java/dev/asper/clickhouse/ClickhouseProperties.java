package dev.asper.clickhouse;

import org.apache.spark.sql.execution.datasources.jdbc.JDBCOptions;

import java.util.HashMap;
import java.util.Map;


public final class ClickhouseProperties {
    private final String jdbcUrl;
    private final String password;
    private final String user;
    private final String socketTimeout;
    private final String queryTimeout;
    private final String driverClass;
    private final Map<String, String> options = new HashMap<>();

    public ClickhouseProperties(String jdbcUrl, String password, String user, String socketTimeout, String queryTimeout, String driverClass) {
        this.jdbcUrl = jdbcUrl;
        this.password = password;
        this.user = user;
        this.socketTimeout = socketTimeout;
        this.queryTimeout = queryTimeout;
        this.driverClass = driverClass;
        options.put("url", jdbcUrl);
        options.put(JDBCOptions.JDBC_DRIVER_CLASS(), driverClass);
        options.put("user", user);
        options.put("password", password);
        options.put("socket_timeout", socketTimeout);
        options.put(JDBCOptions.JDBC_QUERY_TIMEOUT(), queryTimeout);
    }

    public Map<String, String> options() {
        return options;
    }

    public String jdbcUrl() {
        return jdbcUrl;
    }

    public String password() {
        return password;
    }

    public String user() {
        return user;
    }

    public String socketTimeout() {
        return socketTimeout;
    }

    public String queryTimeout() {
        return queryTimeout;
    }

    public String driverClass() {
        return driverClass;
    }

}
