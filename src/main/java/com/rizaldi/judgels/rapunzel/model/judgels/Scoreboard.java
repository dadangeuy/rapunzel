package com.rizaldi.judgels.rapunzel.model.judgels;

import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class Scoreboard {
    @Key
    private State state;
    @Key
    private Content content;
}
