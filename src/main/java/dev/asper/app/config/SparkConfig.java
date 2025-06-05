package dev.asper.app.config;

import lombok.Data;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@ConfigurationProperties(prefix = "spark")
@Data
public class SparkConfig {
    private String logLevel;
    private String sparkMaster;
    private String sparkDriverHost;
    private String sparkDriverMemory;
    private String sparkExecutorMemory;
    private String sparkDriverCores;
    private String sparkExecutorCores;
    private String appName;
    private String sparkUiEnabled;
    private String sparkKryoRegistrationRequired;
    private String sparkBroadcastCompress;
    private String sparkCheckpointCompress;
    private String sparkEventLogEnabled;
    private String sparkEventLogDir;
    private String sparkExecutorInstances;

    @Bean
    public SparkSession sparkSession() {
        new File(sparkEventLogDir).mkdirs();
        SparkConf sparkConf = new SparkConf()
                .set("spark.master", sparkMaster)
                .set("spark.driver.host", sparkDriverHost)
                .set("spark.driver.memory", sparkDriverMemory)
                .set("spark.executor.instances", sparkExecutorInstances)
                .set("spark.executor.memory", sparkExecutorMemory)
                .set("spark.driver.cores", sparkDriverCores)
                .set("spark.executor.cores", sparkExecutorCores)
                .set("spark.ui.enabled", sparkUiEnabled)
                .set("spark.kryo.registrationRequired", sparkKryoRegistrationRequired)
                .set("spark.broadcast.compress", sparkBroadcastCompress)
                .set("spark.checkpoint.compress", sparkCheckpointCompress)
                .set("spark.eventLog.enabled", sparkEventLogEnabled)
                .set("spark.eventLog.dir", sparkEventLogDir);
        SparkSession sparkSession = SparkSession.builder()
                .appName(appName)
                .config(sparkConf)
                .getOrCreate();
        SparkContext sparkContext = SparkContext.getOrCreate();
        sparkContext.setLogLevel(logLevel);
        sparkContext.hadoopConfiguration().set("fs.file.impl", LocalFileSystem.class.getName());
        return sparkSession;
    }
}
