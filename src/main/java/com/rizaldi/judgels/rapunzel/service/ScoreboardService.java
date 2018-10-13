package com.rizaldi.judgels.rapunzel.service;

import com.rizaldi.judgels.rapunzel.model.ScoreboardRow;
import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
import com.rizaldi.judgels.rapunzel.model.judgels.Entry;
import com.rizaldi.judgels.rapunzel.model.judgels.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class ScoreboardService {
    @Value("${uriel.containerJid}")
    private String containerJid;
    @Value("${uriel.scoreboardSecret}")
    private String secret;
    @Value("${uriel.scoreboardType}")
    private String type;
    private final JophielApiService jophiel;
    private final UrielApiService uriel;

    public ScoreboardService(JophielApiService jophiel, UrielApiService uriel) {
        this.jophiel = jophiel;
        this.uriel = uriel;
    }

    public List<String> getProblemAlias() throws IOException {
        return uriel.getContest(containerJid, secret, type)
                .getScoreboard()
                .getState()
                .getProblemAliases();
    }

    public Mono<List<String>> getProblemAliasMono() {
        return uriel.getContestMono(containerJid, secret, type)
                .map(contest -> contest.getScoreboard().getState().getProblemAliases());
    }

    public List<ScoreboardRow> getScoreboardRows() throws IOException, ExecutionException, InterruptedException {
        Contest contest = uriel.getContest(containerJid, secret, type);

        List<String> userJids = contest.getScoreboard().getState().getContestantJids();
        List<User> users = jophiel.getUsers(userJids);
        Map<String, User> userMap = mapUserByJid(users);

        List<ScoreboardRow> scoreboardRows = new ArrayList<>(userJids.size());
        for (Entry entry : contest.getScoreboard().getContent().getEntries()) {
            User user = userMap.get(entry.getContestantJid());
            ScoreboardRow row = ScoreboardRow.from(entry, user);
            scoreboardRows.add(row);
        }

        return scoreboardRows;
    }

    public Mono<List<ScoreboardRow>> getScoreboardRowsMono() {
        return uriel.getContestMono(containerJid, secret, type)
                .flatMap(contest -> {
                    Flux<Entry> entryFlux = Flux.fromIterable(contest.getScoreboard().getContent().getEntries());
                    Flux<ScoreboardRow> scoreboardRowFlux = entryFlux
                            .flatMap(entry -> jophiel.getUserMono(entry.getContestantJid())
                                    .map(user -> ScoreboardRow.from(entry, user)));

                    return scoreboardRowFlux.collectSortedList(Comparator.comparingInt(ScoreboardRow::getRank));
                });
    }

    public String getLastUpdateTime() throws IOException {
        return uriel.getContest(containerJid, secret, type)
                .getLastUpdateTime();
    }

    public Mono<String> getLastUpdateTimeMono() {
        return uriel.getContestMono(containerJid, secret, type)
                .map(Contest::getLastUpdateTime);
    }

    private Map<String, User> mapUserByJid(List<User> users) {
        Map<String, User> map = new HashMap<>(2 * users.size());
        users.forEach(user -> map.put(user.getJid(), user));
        return map;
    }
}
