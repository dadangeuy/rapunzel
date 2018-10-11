package com.rizaldi.judgels.rapunzel.model;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Contest {
    @Key
    private int id;
    @Key
    private Scoreboard scoreboard;
}
