package com.rizaldi.judgels.rapunzel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "rapunzel.scoreboard")
@Data
public class ScoreboardConfig {
    private List<String> paths;
    private List<String> jids;
    private List<String> titles;
    private List<String> types;
    private String defaultPath;

    public String getJid(String path) {
        return jids.get(paths.indexOf(path));
    }

    public String getTitle(String path) {
        return titles.get(paths.indexOf(path));
    }

    public String getType(String path) {
        return types.get(paths.indexOf(path));
    }
}
