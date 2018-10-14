package com.rizaldi.judgels.rapunzel.service;

import com.rizaldi.judgels.rapunzel.config.ScoreboardConfig;
import com.rizaldi.judgels.rapunzel.model.ScoreboardRow;
import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Comparator;
import java.util.List;

@Service
public class ScoreboardService {
    private final ScoreboardConfig config;
    private final JophielApiService jophiel;
    private final UrielApiService uriel;

    public ScoreboardService(ScoreboardConfig config, JophielApiService jophiel, UrielApiService uriel) {
        this.config = config;
        this.jophiel = jophiel;
        this.uriel = uriel;
    }

    public Mono<List<String>> getProblemAliasMono(String contestPath) {
        return uriel.getContestMono(config.getJid(contestPath), config.getType(contestPath))
                .map(contest -> contest.getScoreboard().getState().getProblemAliases());
    }

    public Mono<List<ScoreboardRow>> getScoreboardRowsMono(String contestPath) {
        return uriel.getContestMono(config.getJid(contestPath), config.getType(contestPath))
                .map(contest -> contest.getScoreboard().getContent().getEntries())
                .flatMap(entries -> Flux.fromIterable(entries)
                        .parallel()
                        .runOn(Schedulers.elastic())
                        .flatMap(entry -> jophiel.getUserMono(entry.getContestantJid())
                                .map(user -> ScoreboardRow.from(entry, user)))
                        .collectSortedList(Comparator.comparingInt(ScoreboardRow::getRank)));
    }

    public Mono<String> getLastUpdateTimeMono(String contestPath) {
        return uriel.getContestMono(config.getJid(contestPath), config.getType(contestPath))
                .map(Contest::getLastUpdateTime);
    }
}
