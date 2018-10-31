package com.rizaldi.judgels.rapunzel.controller;

import com.rizaldi.judgels.rapunzel.config.ScoreboardConfig;
import com.rizaldi.judgels.rapunzel.config.UrielConfig;
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
    private final UrielConfig urielConfig;
    private final ScoreboardService scoreboard;

    public ScoreboardController(WebConfig webConfig, ScoreboardConfig scoreboardConfig, UrielConfig urielConfig, ScoreboardService scoreboard) {
        this.webConfig = webConfig;
        this.scoreboardConfig = scoreboardConfig;
        this.urielConfig = urielConfig;
        this.scoreboard = scoreboard;
    }

    @GetMapping({"/", "/scoreboard"})
    public String viewRoot() {
        return "redirect:/scoreboard/" + scoreboardConfig.getDefaultPath();
    }

    @GetMapping("/scoreboard/{contestPath}")
    public String viewScoreboard(Model model, @PathVariable String contestPath) {
        try {
            model.addAttribute("logos", webConfig.getLogos());
            model.addAttribute("icon", webConfig.getIcon());
            model.addAttribute("title", scoreboardConfig.getTitle(contestPath));
            model.addAttribute("host", urielConfig.getHost());

            model.addAttribute("aliases", scoreboard.getProblemAlias(contestPath));
            model.addAttribute("rows", scoreboard.getScoreboardRows(contestPath));
            model.addAttribute("lastUpdateTime", scoreboard.getLastUpdateTime(contestPath));

            return "ScoreboardPage";
        } catch (Exception e) {
            return "ScoreboardErrorPage";
        }
    }

    @GetMapping("/hello")
    public String viewHello(Model model) {
        model.addAttribute("name", "Rapunzel");

        return "HelloPage";
    }
}
