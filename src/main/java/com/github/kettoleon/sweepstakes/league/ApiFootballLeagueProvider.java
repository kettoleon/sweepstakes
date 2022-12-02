package com.github.kettoleon.sweepstakes.league;

import com.github.kettoleon.sweepstakes.client.apifootball.ApiFootballClient;
import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.Country;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesEntry;
import com.github.kettoleon.sweepstakes.client.apifootball.model.leagues.LeagueResponse;
import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.league.model.Team;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ApiFootballLeagueProvider implements LeagueProvider {

    private final ApiFootballClient client;

    public ApiFootballLeagueProvider(ApiFootballClient client) {
        this.client = client;
    }

    @Override
    public League getLeague() {
        League league = new League();
        getLeagueData(league);
        league.setFixtures(collectFixtures());
        return league;
    }

    private void getLeagueData(League toLeague) {
        LeagueResponse fromLeague = client.getLeague();
        toLeague.setName(fromLeague.getLeagueName());
        toLeague.setSeason(fromLeague.getSeasonName());
        toLeague.setLogo(fromLeague.getLeagueLogo());
    }

    private List<Fixture> collectFixtures() {
        List<Fixture> fixtures = new ArrayList<>();
        client.getFixtures().getResponse().stream().filter(FixturesEntry::isGroupStage).forEach(ft -> fixtures.add(mapFixture(ft)));

        return fixtures;
    }

    private Fixture mapFixture(FixturesEntry ft) {
        Fixture fixture = new Fixture();
        fixture.setId(ft.getFixture().getId());
        fixture.setTime(ft.getTime());
        fixture.setRound(ft.getRound());
        fixture.setScoreHome(ft.getHomeScore());
        fixture.setScoreAway(ft.getAwayScore());
        fixture.setHome(team(ft.getHomeTeamName()));
        fixture.setAway(team(ft.getAwayTeamName()));
        fixture.setFinished(ft.isFinished());
        fixture.setStarted(ft.isBeingPlayed());
        return fixture;
    }

    private Team team(String teamName) {
        Team team = new Team();
        team.setName(teamName);
        team.setIcon(getFlagIcon(teamName));
        return team;
    }

    private String getFlagIcon(String teamName) {
        return client.getCountries().getCountry(teamName).map(Country::getFlag).map(flg -> "countries/" + StringUtils.substringAfterLast(flg, "/flags/")).orElse(null);
    }
}
