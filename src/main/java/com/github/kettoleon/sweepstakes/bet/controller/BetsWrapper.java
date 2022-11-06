package com.github.kettoleon.sweepstakes.bet.controller;

import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.bet.repo.FixtureBet;

public class BetsWrapper {

    public BetsRepository repository;

    public BetsWrapper(BetsRepository repository) {

        this.repository = repository;
    }

    public String getBet(String email, long fixtureId) {
        FixtureBet bet = repository.findByEmailAndFixtureId(email, fixtureId);
        if (bet == null) return "NP";
        return bet.getHome() + "-" + bet.getAway();
    }


}
