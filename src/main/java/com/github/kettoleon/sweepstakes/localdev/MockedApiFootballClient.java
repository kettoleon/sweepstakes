package com.github.kettoleon.sweepstakes.localdev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kettoleon.sweepstakes.client.apifootball.ApiFootballClient;
import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.CountriesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.leagues.LeagueResponse;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

public class MockedApiFootballClient implements ApiFootballClient {

    private ObjectMapper objectMapper = new ObjectMapper();
    private LeagueResponse cachedLeague;
    private CountriesResponse cachedCountries;
    private FixturesResponse fixtures;

    public MockedApiFootballClient(int leagueId, int seasonId) {
        try {
            cachedCountries = objectMapper.readValue(ResourceUtils.getURL("classpath:countries.json"), CountriesResponse.class);
            cachedLeague = objectMapper.readValue(ResourceUtils.getURL("classpath:league.json"), LeagueResponse.class);
            fixtures = objectMapper.readValue(ResourceUtils.getURL("classpath:fixtures.json"), FixturesResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FixturesResponse getFixtures() {
        //We are mocking in development, so it is safe to always load it from the file and keep it in memory.
        return fixtures;
    }

    @Override
    public CountriesResponse getCountries() {
        //Countries won't change during the world cup, so it is safe to always load it from the file and keep it in memory.
        return cachedCountries;
    }

    @Override
    public LeagueResponse getLeague() {
        //The league won't change during the world cup, so it is safe to always load it from the file and keep it in memory.
        return cachedLeague;
    }


}
