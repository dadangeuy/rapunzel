package com.rizaldi.judgels.rapunzel.service;

import com.rizaldi.judgels.rapunzel.model.ScoreboardRow;
import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
import com.rizaldi.judgels.rapunzel.model.judgels.Entry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
public class ScoreboardService {
    private final JophielApiService jophiel;
    private final UrielApiService uriel;
    @Value("${uriel.containerJid}")
    private String containerJid;
    @Value("${uriel.scoreboardSecret}")
    private String secret;
    @Value("${uriel.scoreboardType}")
    private String type;

    public ScoreboardService(JophielApiService jophiel, UrielApiService uriel) {
        this.jophiel = jophiel;
        this.uriel = uriel;
    }

    public Mono<List<String>> getProblemAliasMono() {
        return uriel.getContestMono(containerJid, secret, type)
                .map(contest -> contest.getScoreboard().getState().getProblemAliases());
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

    public Mono<String> getLastUpdateTimeMono() {
        return uriel.getContestMono(containerJid, secret, type)
                .map(Contest::getLastUpdateTime);
    }
}
