package com.github.kettoleon.sweepstakes.bet.controller;

import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.bet.repo.FixtureBet;
import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
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

@Controller
public class BetController {

    @Autowired
    private LeagueProvider leagueProvider;

    @Autowired
    private BetsRepository betsRepository;

    @GetMapping("/bet")
    public ModelAndView manageBetsForm(Authentication auth) {
        return manageBetsPage(auth, null);
    }


    private ModelAndView manageBetsPage(Authentication auth, Errors errors) {
        BetsForm form = new BetsForm();
        List<FixtureBet> bets = betsRepository.findAllByEmail(auth.getName());
        if (bets.isEmpty()) {
            bets = populateNewBets(auth.getName());
        }
        bets = orderBets(bets);
        form.setBets(bets);

        return page("bet", "Manage my Bet")
                .addObject("form", form)
                .addObject("errors", errors);
    }

    private List<FixtureBet> orderBets(List<FixtureBet> bets) {
        List<FixtureBet> ordered = new ArrayList<>();
        leagueProvider.getLeague().getFixturesByDate().forEach(fxt -> {
            ordered.add(bets.stream().filter(b -> b.getFixtureId() == fxt.getId()).findFirst().orElseThrow());
        });
        return ordered;
    }

    private List<FixtureBet> populateNewBets(String email) {
        List<FixtureBet> bets = new ArrayList<>();
        leagueProvider.getLeague().getFixturesByDate().forEach(fxt -> {
            FixtureBet bet = new FixtureBet();
            bet.setId(UUID.randomUUID().toString());
            bet.setEmail(email);
            bet.setFixtureId(fxt.getId());
            bets.add(bet);
        });
        return bets;
    }

    @PostMapping("/bet")
    public ModelAndView saveBets(@ModelAttribute("form") BetsForm form, Errors errors, Authentication auth) {
        form.getBets().forEach(b -> b.setEmail(auth.getName())); //Make sure no one changes everyone else's bets ¬¬
        betsRepository.saveAllAndFlush(form.getBets());
        return manageBetsPage(auth, errors);
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
