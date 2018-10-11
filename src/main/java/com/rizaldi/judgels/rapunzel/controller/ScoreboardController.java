package com.rizaldi.judgels.rapunzel.controller;

import com.rizaldi.judgels.rapunzel.model.Scoreboard;
import com.rizaldi.judgels.rapunzel.service.JudgelsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class ScoreboardController {
    private final JudgelsService judgels;
    @Value("${rapunzel.scoreboard.title}")
    private String title;
    @Value("${rapunzel.scoreboard.logo}")
    private String logo;

    public ScoreboardController(JudgelsService judgels) {
        this.judgels = judgels;
    }

    @GetMapping("/")
    public String scoreboard(Model model) throws IOException, ExecutionException, InterruptedException {
        Scoreboard scoreboard = judgels.getScoreboard();
        model.addAttribute("title", title);
        model.addAttribute("logo", logo);
        model.addAttribute("problemAliases", scoreboard.getState().getProblemAliases());
        model.addAttribute("entries", scoreboard.getContent().getEntries());
        return "ScoreboardPage";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "Rapunzel");
        return "HelloPage";
    }
}
