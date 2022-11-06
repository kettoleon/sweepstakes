package com.github.kettoleon.sweepstakes.leaderboard;

import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.league.model.LeagueProvider;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LeaderboardController {

    @Autowired
    private LeagueProvider leagueProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BetsRepository betsRepository;

    @GetMapping("/leaderboard")
    public ModelAndView leaderboard() {
        return page("leaderboard", "Leaderboard");
    }

    @GetMapping("/tracking")
    public ModelAndView allBets() {
        return page("tracking", "Leaderboard tracking")
                .addObject("leaderboard", new Leaderboard(userRepository, betsRepository, leagueProvider.getLeague()));
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
