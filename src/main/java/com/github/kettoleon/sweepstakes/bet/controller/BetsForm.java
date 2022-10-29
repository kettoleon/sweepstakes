package com.github.kettoleon.sweepstakes.bet.controller;

import com.github.kettoleon.sweepstakes.bet.repo.FixtureBet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BetsForm {

    private List<FixtureBet> bets = new ArrayList<>();

}
