package com.rizaldi.judgels.rapunzel.controller;

import com.rizaldi.judgels.rapunzel.service.ScoreboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.RedirectView;
import org.springframework.web.reactive.result.view.View;

@Controller
public class ScoreboardController {
    private final ScoreboardService scoreboard;
    @Value("${rapunzel.title}")
    private String title;
    @Value("${rapunzel.logos}")
    private String[] logos;

    public ScoreboardController(ScoreboardService scoreboard) {
        this.scoreboard = scoreboard;
    }

    @GetMapping("/")
    public View viewRoot() {
        return new RedirectView("scoreboard");
    }

    @GetMapping("/scoreboard")
    public String viewScoreboard(Model model) {
        model.addAttribute("title", title);
        model.addAttribute("logos", logos);

        model.addAttribute("aliases", scoreboard.getProblemAliasMono());
        model.addAttribute("rows", scoreboard.getScoreboardRowsMono());
        model.addAttribute("lastUpdateTime", scoreboard.getLastUpdateTimeMono());

        return "ScoreboardPage";
    }

    @GetMapping("/hello")
    public String viewHello(Model model) {
        model.addAttribute("name", "Rapunzel");

        return "HelloPage";
    }
}
