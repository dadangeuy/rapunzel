package com.rizaldi.judgels.rapunzel.model.judgels;

import com.google.api.client.util.Key;
import lombok.Data;

import java.util.List;

@Data
public class Entry {
    @Key
    private int rank;
    @Key
    private String contestantJid;
    private String contestantName;
    @Key
    private String imageURL;
    @Key
    private int totalAccepted;
    @Key
    private int totalPenalties;
    @Key
    private int lastAcceptedPenalty;
    @Key
    private List<Integer> attemptsList;
    @Key
    private List<Integer> penaltyList;
    @Key
    private List<Integer> problemStateList;
}
