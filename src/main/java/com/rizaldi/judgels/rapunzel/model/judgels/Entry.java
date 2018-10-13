package com.rizaldi.judgels.rapunzel.model.judgels;

import lombok.Data;

import java.util.List;

@Data
public class Entry {
    private int rank;
    private String contestantJid;
    private String imageURL;
    private int totalAccepted;
    private int totalPenalties;
    private int lastAcceptedPenalty;
    private List<Integer> attemptsList;
    private List<Integer> penaltyList;
    private List<Integer> problemStateList;
}
