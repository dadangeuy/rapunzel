package com.rizaldi.judgels.rapunzel.service;

import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
import com.rizaldi.judgels.rapunzel.model.judgels.Entry;
import com.rizaldi.judgels.rapunzel.model.judgels.Scoreboard;
import com.rizaldi.judgels.rapunzel.model.judgels.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class JudgelsService {
    @Value("${uriel.containerJid}")
    private String containerJid;
    @Value("${uriel.scoreboardSecret}")
    private String secret;
    @Value("${uriel.scoreboardType}")
    private String type;
    private final JophielApiService jophiel;
    private final UrielApiService uriel;

    public JudgelsService(JophielApiService jophiel, UrielApiService uriel) {
        this.jophiel = jophiel;
        this.uriel = uriel;
    }

    public Contest getContestScoreboard() throws IOException, ExecutionException, InterruptedException {
        // fetch scoreboard
        Contest contest = uriel.getContest(containerJid, secret, type);
        Scoreboard scoreboard = contest.getScoreboard();
        List<Entry> entries = scoreboard.getContent().getEntries();

        // fetch user data
        List<String> contestantJids = scoreboard.getState().getContestantJids();
        List<User> users = jophiel.getUsers(contestantJids);
        Map<String, User> userMap = mapUserByJid(users);

        // combine scoreboard & user data
        for (Entry entry : entries) {
            User user = userMap.get(entry.getContestantJid());
            String name = user.getName() == null ? "(hidden name)" : user.getName();
            entry.setContestantName(name);
        }
        return contest;
    }

    private Map<String, User> mapUserByJid(List<User> users) {
        Map<String, User> map = new HashMap<>(2 * users.size());
        users.forEach(user -> map.put(user.getJid(), user));
        return map;
    }
}
