package com.rizaldi.judgels.rapunzel.model;

import com.rizaldi.judgels.rapunzel.model.judgels.Entry;
import com.rizaldi.judgels.rapunzel.model.judgels.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ScoreboardRow {
    private final int rank;
    private final String logo;
    private final String contestant;
    private final int totalAccepted;
    private final int totalPenalty;
    private final List<Integer> attempts;
    private final List<Integer> penalties;
    private final List<Integer> states;

    public static ScoreboardRow from(Entry entry, User user) {
        return ScoreboardRow.builder()
                .rank(entry.getRank())
                .logo(entry.getImageURL())
                .contestant(user.getName() == null ? "(hidden)" : user.getName())
                .totalAccepted(entry.getTotalAccepted())
                .totalPenalty(entry.getTotalPenalties())
                .attempts(entry.getAttemptsList())
                .penalties(entry.getPenaltyList())
                .states(entry.getProblemStateList())
                .build();
    }
}
