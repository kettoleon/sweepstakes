package com.github.kettoleon.sweepstakes.controller;

import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.league.model.LeagueProvider;
import com.github.kettoleon.sweepstakes.users.repo.User;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired
    private LeagueProvider leagueProvider;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = {"", "/", "/rules"})
    public ModelAndView index() {
        return page("rules", "Sweepstake Rules")
                .addObject("contacts", userRepository.findAll().stream().filter(User::isContact).collect(Collectors.toList()));
    }

    @GetMapping("/classification")
    public ModelAndView classification() {
        return page("classification", "Sweepstake Classification");
    }

    @GetMapping("/calendar")
    public ModelAndView calendar() {
        return page("calendar", "Fixtures Calendar");
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
