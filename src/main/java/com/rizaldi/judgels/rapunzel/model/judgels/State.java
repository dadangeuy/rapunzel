package com.rizaldi.judgels.rapunzel.model.judgels;

import lombok.Data;

import java.util.List;

@Data
public class State {
    private List<String> problemJids;
    private List<String> problemAliases;
    private List<String> contestantJids;
}
