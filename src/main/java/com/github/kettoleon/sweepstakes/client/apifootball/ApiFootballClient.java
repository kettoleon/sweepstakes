package com.github.kettoleon.sweepstakes.client.apifootball;

import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.CountriesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.leagues.LeagueResponse;

public interface ApiFootballClient {
    FixturesResponse getFixtures();

    CountriesResponse getCountries();

    LeagueResponse getLeague();
}
