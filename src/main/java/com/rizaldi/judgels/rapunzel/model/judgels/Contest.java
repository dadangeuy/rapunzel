package com.rizaldi.judgels.rapunzel.model.judgels;

import lombok.Builder;
import lombok.Data;

@Data
public class Contest {
    private int id;
    private String contestJid;
    private String type;
    private Scoreboard scoreboard;
    private String lastUpdateTime;

    @Builder
    @Data
    public static class RequestBody {
        private String containerJid;
        private String secret;
        private String type;
    }
}
