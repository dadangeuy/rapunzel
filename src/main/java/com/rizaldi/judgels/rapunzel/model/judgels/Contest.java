package com.rizaldi.judgels.rapunzel.model.judgels;

import com.google.api.client.util.Key;
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
}
