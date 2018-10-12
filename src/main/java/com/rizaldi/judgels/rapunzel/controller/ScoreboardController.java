package com.rizaldi.judgels.rapunzel.controller;

import com.rizaldi.judgels.rapunzel.model.ContestScoreboard;
import com.rizaldi.judgels.rapunzel.model.Scoreboard;
import com.rizaldi.judgels.rapunzel.service.JudgelsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class ScoreboardController {
    private final JudgelsService judgels;
    @Value("${rapunzel.title}")
    private String title;
    @Value("${rapunzel.logos}")
    private String[] logos;

    public ScoreboardController(JudgelsService judgels) {
        this.judgels = judgels;
    }

    @GetMapping("/")
    public RedirectView redirectRoot() {
        return new RedirectView("/scoreboard");
    }

    @GetMapping("/scoreboard")
    public ModelAndView viewScoreboard() throws IOException, ExecutionException, InterruptedException {
        ContestScoreboard contestScoreboard = judgels.getContestScoreboard();
        Scoreboard scoreboard = contestScoreboard.getScoreboard();
        Map<String, Object> model = new HashMap<>(8);
        model.put("title", title);
        model.put("logos", logos);
        model.put("problemAliases", scoreboard.getState().getProblemAliases());
        model.put("entries", scoreboard.getContent().getEntries());
        model.put("lastUpdateTime", contestScoreboard.getLastUpdateTime());
        return new ModelAndView("ScoreboardPage", model);
    }

    @GetMapping("/hello")
    public ModelAndView viewHello() {
        Map<String, Object> model = new HashMap<>(2);
        model.put("name", "Rapunzel");
        return new ModelAndView("HelloPage", model);
    }
}
