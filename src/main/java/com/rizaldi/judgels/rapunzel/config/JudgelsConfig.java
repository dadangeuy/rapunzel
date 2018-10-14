package com.rizaldi.judgels.rapunzel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "judgels")
@Data
public class JudgelsConfig {
    private JophielConfig jophiel;
    private UrielConfig uriel;

    @Data
    public static class JophielConfig {
        private String host;
    }

    @Data
    public static class UrielConfig {
        private String host;
        private String scoreboardSecret;
    }
}
