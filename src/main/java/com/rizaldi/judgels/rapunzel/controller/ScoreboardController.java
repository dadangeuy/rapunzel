package com.rizaldi.judgels.rapunzel.controller;

import com.rizaldi.judgels.rapunzel.service.ScoreboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.RedirectView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class ScoreboardController {
    private final RedirectView rootView = new RedirectView("scoreboard");
    private final ScoreboardService scoreboard;
    @Value("${rapunzel.title}")
    private String title;
    @Value("${rapunzel.logos}")
    private String[] logos;

    public ScoreboardController(ScoreboardService scoreboard) {
        this.scoreboard = scoreboard;
    }

    @GetMapping("/")
    public RedirectView viewRoot() {
        return rootView;
    }

    @GetMapping("/scoreboard")
    public String viewScoreboard(Model model) throws IOException, ExecutionException, InterruptedException {
        model.addAttribute("title", title);
        model.addAttribute("logos", logos);

        model.addAttribute("aliases", scoreboard.getProblemAlias());
        model.addAttribute("rows", scoreboard.getScoreboardRows());
        model.addAttribute("lastUpdateTime", scoreboard.getLastUpdateTime());

        return "ScoreboardPage";
    }

    @GetMapping("/hello")
    public String viewHello(Model model) {
        model.addAttribute("name", "Rapunzel");

        return "HelloPage";
    }
}
