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
import java.util.stream.Stream;

public class DuringAnotherMatch implements Scenario {

    private League cache = null;

    @Override
    public League alterLeague(League league) {


        LocalDateTime leagueEnd = league.getEnd();
        if (leagueEnd.truncatedTo(ChronoUnit.DAYS).isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
            if (cache != null) return cache;
            long days = Duration.between(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS), leagueEnd.truncatedTo(ChronoUnit.DAYS)).toDays() - 1;

            Random random = new Random();
            for (Fixture fixture : league.getFixtures()) {
                fixture.setTime(fixture.getTime().minusDays(days));

                fixture.setStarted(false);
                fixture.setFinished(true);
                fixture.setScoreHome(random.nextInt(0, 6));
                fixture.setScoreAway(random.nextInt(0, 6));

                if (fixture.getTime().truncatedTo(ChronoUnit.DAYS).equals(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS))) {
                    fixture.setStarted(false);
                    fixture.setFinished(false);
                }

            }
            List<Fixture> beforeLastDayFixtures = league.getFixturesByDay(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)).stream().sorted(Comparator.comparing(Fixture::getTime)).collect(Collectors.toList());

            Fixture last = beforeLastDayFixtures.get(3);
            last.setFinished(false);
            last.setScoreHome(0);
            last.setScoreAway(0);
            Fixture blast = beforeLastDayFixtures.get(2);
            blast.setFinished(false);
            blast.setStarted(true);
            cache = league;

        }


        return league;
    }
}
