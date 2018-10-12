package com.rizaldi.judgels.rapunzel.model;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class ContestScoreboard {
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
}
