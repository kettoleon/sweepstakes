package com.github.kettoleon.sweepstakes.localdev.scenario;

import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.league.model.League;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AfterLastMatch implements Scenario {

    private League cache = null;

    @Override
    public League alterLeague(League league) {


        LocalDateTime leagueEnd = league.getEnd();
        if (leagueEnd.truncatedTo(ChronoUnit.DAYS).isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
            if (cache != null) return cache;
            long days = Duration.between(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), leagueEnd.truncatedTo(ChronoUnit.DAYS)).toDays() + 1;

            Random random = new Random();
            for (Fixture fixture : league.getFixtures()) {
                fixture.setTime(fixture.getTime().minusDays(days));
                fixture.setStarted(false);
                fixture.setFinished(true);
                fixture.setScoreHome(random.nextInt(0, 6));
                fixture.setScoreAway(random.nextInt(0, 6));
            }
            cache = league;

        }


        return league;
    }
}
