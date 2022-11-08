package com.github.kettoleon.sweepstakes.localdev.scenario;

import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.league.model.League;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AfterFirstMatch implements Scenario {
    @Override
    public League alterLeague(League league) {


        LocalDateTime leagueStart = league.getStart();
        if (leagueStart.truncatedTo(ChronoUnit.DAYS).isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
            long days = Duration.between(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), leagueStart.truncatedTo(ChronoUnit.DAYS)).toDays();

            for (Fixture fixture : league.getFixtures()) {
                fixture.setTime(fixture.getTime().minusDays(days));

                fixture.setStarted(false);
                fixture.setFinished(false);

                if (fixture.getTime().truncatedTo(ChronoUnit.DAYS).equals(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
                    fixture.setTime(LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.HOURS));
                    fixture.setStarted(true);
                    fixture.setFinished(true);
                    fixture.setScoreHome(3);
                    fixture.setScoreAway(2);
                }


            }

        }


        return league;
    }
}
