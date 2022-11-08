package com.github.kettoleon.sweepstakes.localdev.scenario;

import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.league.model.League;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BeforeLeagueStarts implements Scenario {
    @Override
    public League alterLeague(League league) {


        LocalDateTime leagueStart = league.getStart();
        if (leagueStart.truncatedTo(ChronoUnit.DAYS).isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
            long days = Duration.between(leagueStart, LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS)).toDays();

            for (Fixture fixture : league.getFixtures()) {
                fixture.setTime(fixture.getTime().plusDays(days));
                fixture.setStarted(false);
                fixture.setFinished(false);
            }

        }


        return league;
    }
}
