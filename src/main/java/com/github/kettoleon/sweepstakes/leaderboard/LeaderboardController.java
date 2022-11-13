package com.github.kettoleon.sweepstakes.leaderboard;

import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.github.kettoleon.sweepstakes.configuration.GlobalTemplateVariables.page;

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
        return page("leaderboard", "Leaderboard")
                .addObject("leaderboard", new Leaderboard(userRepository, betsRepository, leagueProvider.getLeague()));
    }

    @GetMapping("/tracking")
    public ModelAndView allBets() {
        return page("tracking", "Leaderboard tracking")
                .addObject("leaderboard", new Leaderboard(userRepository, betsRepository, leagueProvider.getLeague()));
    }


}
