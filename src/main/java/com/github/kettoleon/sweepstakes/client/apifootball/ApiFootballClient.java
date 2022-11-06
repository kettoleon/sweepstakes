package com.github.kettoleon.sweepstakes.client.apifootball;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.CountriesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.leagues.LeagueResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class ApiFootballClient {

    private final int leagueId;
    private final int seasonId;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ApiFootballClient(int leagueId, int seasonId) {
        this.leagueId = leagueId;
        this.seasonId = seasonId;
        restTemplate = new RestTemplateBuilder().build();
    }

    public FixturesResponse getFixtures() {
        //TODO cache, request only if not requested for 30 minutes
        try {
            return objectMapper.readValue(ResourceUtils.getURL("classpath:fixtures.testing.json"), FixturesResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public CountriesResponse getCountries() {
        //TODO cache, request only if not requested for 30 minutes
        try {
            return objectMapper.readValue(ResourceUtils.getURL("classpath:countries.json"), CountriesResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public LeagueResponse getLeague() {
        //TODO cache, request only if not requested for 30 minutes
        try {
            return objectMapper.readValue(ResourceUtils.getURL("classpath:league.json"), LeagueResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
