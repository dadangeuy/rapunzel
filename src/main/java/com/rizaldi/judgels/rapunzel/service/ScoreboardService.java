package com.rizaldi.judgels.rapunzel.service;

import com.rizaldi.judgels.rapunzel.config.ScoreboardConfig;
import com.rizaldi.judgels.rapunzel.model.ScoreboardRow;
import com.rizaldi.judgels.rapunzel.model.judgels.Contest;
import com.rizaldi.judgels.rapunzel.model.judgels.Entry;
import com.rizaldi.judgels.rapunzel.model.judgels.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ScoreboardService {
    private static final ExecutorService requestExecutor = Executors.newFixedThreadPool(32);
    private final ScoreboardConfig config;
    private final JophielApiService jophiel;
    private final UrielApiService uriel;
    private final ImageProviderService imageProvider;

    public ScoreboardService(ScoreboardConfig config, JophielApiService jophiel, UrielApiService uriel, ImageProviderService imageProvider) {
        this.config = config;
        this.jophiel = jophiel;
        this.uriel = uriel;
        this.imageProvider = imageProvider;
    }

    public List<String> getProblemAlias(String contestPath) throws IOException {
        return uriel.getContest(config.getJid(contestPath), config.getType(contestPath))
                .getScoreboard().getState().getProblemAliases();
    }

    public List<ScoreboardRow> getScoreboardRows(String contestPath) throws IOException, ExecutionException, InterruptedException {
        Contest contest = uriel.getContest(config.getJid(contestPath), config.getType(contestPath));
        List<Entry> entries = contest.getScoreboard().getContent().getEntries();
        List<User> users = getUsers(entries);
        List<ScoreboardRow> scoreboardRows = new ArrayList<>(entries.size());
        for (int i = 0; i < entries.size(); i++) {
            ScoreboardRow row = ScoreboardRow.from(entries.get(i), users.get(i));
            scoreboardRows.add(row);
        }
        imageProvider.update(scoreboardRows);
        return scoreboardRows;
    }

    private List<User> getUsers(List<Entry> entries) throws ExecutionException, InterruptedException {
        List<Callable<User>> callables = new ArrayList<>(entries.size());
        for (Entry entry : entries) {
            Callable<User> callable = () -> jophiel.getUser(entry.getContestantJid());
            callables.add(callable);
        }
        return invokeAll(callables);
    }

    private <T> List<T> invokeAll(List<Callable<T>> callables) throws InterruptedException, ExecutionException {
        List<Future<T>> futures = requestExecutor.invokeAll(callables);
        List<T> results = new ArrayList<>(futures.size());
        for (Future<T> future : futures) results.add(future.get());
        return results;
    }

    public String getLastUpdateTime(String contestPath) throws IOException {
        return uriel.getContest(config.getJid(contestPath), config.getType(contestPath))
                .getLastUpdateTime();
    }
}
