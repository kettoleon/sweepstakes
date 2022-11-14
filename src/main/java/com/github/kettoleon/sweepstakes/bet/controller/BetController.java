package com.github.kettoleon.sweepstakes.bet.controller;

import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.bet.repo.FixtureBet;
import com.github.kettoleon.sweepstakes.leaderboard.Leaderboard;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.kettoleon.sweepstakes.configuration.GlobalTemplateVariables.page;

@Controller
public class BetController {

    @Autowired
    private LeagueProvider leagueProvider;

    @Autowired
    private BetsRepository betsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/bet")
    public ModelAndView manageBetsForm(Authentication auth) {
        return manageBetsPage(auth, null);
    }

    @GetMapping("/bets")
    public ModelAndView bets() {
        return page("bets", "Everyone's bets")
                .addObject("leaderboard", new Leaderboard(userRepository, betsRepository, leagueWithoutResults()));
    }

    private League leagueWithoutResults() {
        League league = leagueProvider.getLeague();
        for (Fixture f : league.getFixtures()) {
            f.setStarted(false);
            f.setFinished(false);
            f.setScoreAway(0);
            f.setScoreHome(0);
        }
        return league;
    }


    private ModelAndView manageBetsPage(Authentication auth, Errors errors) {
        BetsForm form = new BetsForm();
        List<FixtureBet> bets = betsRepository.findAllByEmail(auth.getName());
        bets = fillWithMissingBets(bets, auth.getName());
        bets = orderBets(bets);
        form.setBets(bets);

        return page("bet", "Manage my Bet")
                .addObject("form", form)
                .addObject("errors", errors);
    }

    private List<FixtureBet> orderBets(List<FixtureBet> bets) {
        List<FixtureBet> ordered = new ArrayList<>();
        leagueProvider.getLeague().getFixturesByDate().forEach(fxt -> {
            bets.stream().filter(b -> b.getFixtureId() == fxt.getId()).findFirst().ifPresent(ordered::add);
        });
        return ordered;
    }

    private List<FixtureBet> fillWithMissingBets(List<FixtureBet> bets, String email) {
        leagueProvider.getLeague().getFixturesByDate().forEach(fxt -> {
            FixtureBet bet = new FixtureBet();
            bet.setId(UUID.randomUUID().toString());
            bet.setEmail(email);
            bet.setFixtureId(fxt.getId());
            if (bets.stream().filter(fb -> fb.getFixtureId() == fxt.getId()).findFirst().isEmpty()) {
                bets.add(bet);
            }
        });
        return bets;
    }

    @PostMapping("/bet")
    public ModelAndView saveBets(@ModelAttribute("form") BetsForm form, Errors errors, Authentication auth) {
        if (!leagueProvider.getLeague().isBeforeStart()) {
            return page("bet", "Manage my Bet");
        }

        for (int i = 0; i < form.getBets().size(); i++) {
            FixtureBet fb = form.getBets().get(i);
            if (fb.getAway() != null && (fb.getAway() < 0 || fb.getAway() > 100)) {
                fb.setAway(null);
                errors.rejectValue("bets[" + i + "].away", "outOfBounds", "Number of goals has to be between 0 and 100");
            }
            if (fb.getHome() != null && (fb.getHome() < 0 || fb.getHome() > 100)) {
                fb.setHome(null);
                errors.rejectValue("bets[" + i + "].home", "outOfBounds", "Number of goals has to be between 0 and 100");
            }
            fb.setEmail(auth.getName()); //Make sure no one changes everyone else's bets ¬¬
        }

        betsRepository.saveAllAndFlush(form.getBets());
        return manageBetsPage(auth, errors);
    }

}
