package com.rizaldi.judgels.rapunzel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "judgels.uriel")
@Data
public class UrielConfig {
    private String host;
    private String scoreboardSecret;
}
