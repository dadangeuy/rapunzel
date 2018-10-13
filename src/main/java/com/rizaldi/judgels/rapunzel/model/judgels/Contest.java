package com.rizaldi.judgels.rapunzel.model.judgels;

import com.google.api.client.util.Key;
import lombok.Builder;
import lombok.Data;

@Data
public class Contest {
    @Key
    private int id;
    @Key
    private String contestJid;
    @Key
    private String type;
    @Key
    private Scoreboard scoreboard;
    @Key
    private String lastUpdateTime;

    @Builder
    @Data
    public static class RequestBody {
        private String containerJid;
        private String secret;
        private String type;
    }
}
