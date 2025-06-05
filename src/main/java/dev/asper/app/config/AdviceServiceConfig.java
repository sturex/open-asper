package dev.asper.app.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties("advice-service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdviceServiceConfig {
    private List<String> solutionNames;
}
