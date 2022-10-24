package com.github.kettoleon.sweepstakes.controller;

import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.league.model.LeagueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {

    @Autowired
    private LeagueProvider leagueProvider;

    @GetMapping(path = {"", "/", "/rules"})
    public ModelAndView index() {
        return page("rules", "Sweepstake Rules");
    }

    @GetMapping("/classification")
    public ModelAndView classification() {
        return page("classification", "Sweepstake Classification");
    }

    @GetMapping("/calendar")
    public ModelAndView calendar() {
        return page("calendar", "Fixtures Calendar");
    }

    @GetMapping("/bet")
    public ModelAndView bet() {
        return page("bet", "Manage my Bet");
    }

    public ModelAndView page(String viewId, String title) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("page", viewId);
        modelAndView.addObject("pageTitle", title);
        League league = leagueProvider.getLeague();
        modelAndView.addObject("league", league);
        return modelAndView;
    }

}
