package com.rizaldi.judgels.rapunzel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "judgels.jophiel")
@Data
public class JophielConfig {
    private String host;
}
