package com.github.kettoleon.sweepstakes.league.model;

import com.github.kettoleon.sweepstakes.client.apifootball.ApiFootballClient;
import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.Country;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesEntry;
import com.github.kettoleon.sweepstakes.client.apifootball.model.leagues.LeagueResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ApiFootballLeagueProvider implements LeagueProvider {

    private final ApiFootballClient client;

    public ApiFootballLeagueProvider(int leagueId, int seasonId) {
        client = new ApiFootballClient(leagueId, seasonId);
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
        toLeague.setStart(fromLeague.getSeasonStart());
        toLeague.setEnd(fromLeague.getSeasonEnd());
    }

    private List<Fixture> collectFixtures() {
        List<Fixture> fixtures = new ArrayList<>();
        client.getFixtures().getResponse().forEach(ft -> fixtures.add(mapFixture(ft)));

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
