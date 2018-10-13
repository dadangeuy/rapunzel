package com.rizaldi.judgels.rapunzel.service;

import com.rizaldi.judgels.rapunzel.model.ScoreboardRow;
import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class ScoreboardService {
    private final JophielApiService jophiel;
    private final UrielApiService uriel;
    @Value("#{${uriel.pathJid}}")
    private Map<String, String> pathJid;
    @Value("${uriel.scoreboardSecret}")
    private String secret;
    @Value("${uriel.scoreboardType}")
    private String type;

    public ScoreboardService(JophielApiService jophiel, UrielApiService uriel) {
        this.jophiel = jophiel;
        this.uriel = uriel;
    }

    public Mono<List<String>> getProblemAliasMono(String contestPath) {
        return uriel.getContestMono(pathJid.get(contestPath), secret, type)
                .map(contest -> contest.getScoreboard().getState().getProblemAliases());
    }

    public Mono<List<ScoreboardRow>> getScoreboardRowsMono(String contestPath) {
        return uriel.getContestMono(pathJid.get(contestPath), secret, type)
                .flatMap(contest -> Flux.fromIterable(contest.getScoreboard().getContent().getEntries())
                        .parallel()
                        .runOn(Schedulers.elastic())
                        .flatMap(entry -> jophiel.getUserMono(entry.getContestantJid())
                                .map(user -> ScoreboardRow.from(entry, user)))
                        .collectSortedList(Comparator.comparingInt(ScoreboardRow::getRank)));
    }

    public Mono<String> getLastUpdateTimeMono(String contestPath) {
        return uriel.getContestMono(pathJid.get(contestPath), secret, type)
                .map(Contest::getLastUpdateTime);
    }
}
