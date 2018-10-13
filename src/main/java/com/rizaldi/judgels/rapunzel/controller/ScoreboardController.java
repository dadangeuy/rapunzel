package com.rizaldi.judgels.rapunzel.controller;

import com.rizaldi.judgels.rapunzel.service.ScoreboardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScoreboardController {
    private final ScoreboardService scoreboard;
    @Value("${rapunzel.title}")
    private String title;
    @Value("${rapunzel.logos}")
    private String[] logos;
    @Value("${rapunzel.icon}")
    private String icon;
    @Value("${uriel.defaultPath}")
    private String defaultPath;
    @Value("${uriel.host}")
    private String urielHost;

    public ScoreboardController(ScoreboardService scoreboard) {
        this.scoreboard = scoreboard;
    }

    @GetMapping({"/", "/scoreboard"})
    public String viewRoot() {
        return "redirect:/scoreboard/" + defaultPath;
    }

    @GetMapping("/scoreboard/{contestPath}")
    public String viewScoreboard(Model model, @PathVariable String contestPath) {
        model.addAttribute("title", title);
        model.addAttribute("logos", logos);
        model.addAttribute("icon", icon);
        model.addAttribute("host", urielHost);

        model.addAttribute("aliases", scoreboard.getProblemAliasMono(contestPath));
        model.addAttribute("rows", scoreboard.getScoreboardRowsMono(contestPath));
        model.addAttribute("lastUpdateTime", scoreboard.getLastUpdateTimeMono(contestPath));

        return "ScoreboardPage";
    }

    @GetMapping("/hello")
    public String viewHello(Model model) {
        model.addAttribute("name", "Rapunzel");

        return "HelloPage";
    }
}
