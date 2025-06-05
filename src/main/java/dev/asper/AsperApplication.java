package dev.asper;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalTime;
import java.time.ZoneId;

@EntityScan(basePackages = "dev.asper.app.entity")
@EnableJpaRepositories(basePackages = "dev.asper.app.repository")
@ConfigurationPropertiesScan(basePackages = "dev.asper.app.config")
@EnableJpaAuditing
@EnableConfigurationProperties
@SpringBootApplication
@Slf4j
public class AsperApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsperApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.error("Application Started");
    }

}
