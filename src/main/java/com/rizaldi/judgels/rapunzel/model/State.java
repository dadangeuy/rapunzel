package com.rizaldi.judgels.rapunzel.model;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class State {
    @Key
    private List<String> problemJids;
    @Key
    private List<String> problemAliases;
    @Key
    private List<String> contestantJids;
}
