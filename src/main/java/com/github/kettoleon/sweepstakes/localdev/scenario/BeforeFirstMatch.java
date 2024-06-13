package com.github.kettoleon.sweepstakes.localdev.scenario;

import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.league.model.League;

import java.time.Duration;
import java.time.LocalDateTime;

public class BeforeFirstMatch implements Scenario {
    @Override
    public League alterLeague(League league) {


        LocalDateTime firstMatchTime = league.getFirstMatchTime();
        if (LocalDateTime.now().isBefore(firstMatchTime)) {
            long minutes = Duration.between(LocalDateTime.now(), firstMatchTime).toMinutes();

            for (Fixture fixture : league.getFixtures()) {
                fixture.setTime(fixture.getTime().minusMinutes(minutes-30));

                fixture.setStarted(false);
                fixture.setFinished(false);
            }

        }


        return league;
    }
}
