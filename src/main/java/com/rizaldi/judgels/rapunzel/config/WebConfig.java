package com.rizaldi.judgels.rapunzel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "rapunzel.web")
@Data
public class WebConfig {
    private List<String> logos;
    private String icon;
}
