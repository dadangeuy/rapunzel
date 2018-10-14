package com.rizaldi.judgels.rapunzel.controller;

import com.rizaldi.judgels.rapunzel.config.JudgelsConfig;
import com.rizaldi.judgels.rapunzel.config.ScoreboardConfig;
import com.rizaldi.judgels.rapunzel.config.WebConfig;
import com.rizaldi.judgels.rapunzel.service.ScoreboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ScoreboardController {
    private final WebConfig webConfig;
    private final ScoreboardConfig scoreboardConfig;
    private final JudgelsConfig judgelsConfig;
    private final ScoreboardService scoreboard;

    public ScoreboardController(WebConfig webConfig, ScoreboardConfig scoreboardConfig, JudgelsConfig judgelsConfig, ScoreboardService scoreboard) {
        this.webConfig = webConfig;
        this.scoreboardConfig = scoreboardConfig;
        this.judgelsConfig = judgelsConfig;
        this.scoreboard = scoreboard;
    }

    @GetMapping({"/", "/scoreboard"})
    public String viewRoot() {
        return "redirect:/scoreboard/" + scoreboardConfig.getDefaultPath();
    }

    @GetMapping("/scoreboard/{contestPath}")
    public String viewScoreboard(Model model, @PathVariable String contestPath) {
        model.addAttribute("logos", webConfig.getLogos());
        model.addAttribute("icon", webConfig.getIcon());
        model.addAttribute("title", scoreboardConfig.getTitle(contestPath));
        model.addAttribute("host", judgelsConfig.getUriel().getHost());

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
