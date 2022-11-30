package com.github.kettoleon.sweepstakes.localdev;

import com.github.kettoleon.sweepstakes.client.apifootball.ApiFootballClient;
import com.github.kettoleon.sweepstakes.league.ApiFootballLeagueProvider;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.localdev.scenario.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MockedLeagueProvider implements LeagueProvider {

    private static final Logger log = LoggerFactory.getLogger(MockedLeagueProvider.class);
    private final String scenarioId;
    private final Map<String, Scenario> scenarios = new HashMap<>();
    private final ApiFootballLeagueProvider original;
    private String scenarioName = "beforeLeagueStarts";

    public MockedLeagueProvider(String scenarioId, ApiFootballClient client) {
        this.scenarioId = scenarioId;
        original = new ApiFootballLeagueProvider(client);
        scenarios.put("beforeLeagueStarts", new BeforeLeagueStarts());
        scenarios.put("beforeFirstMatch", new BeforeFirstMatch());
        scenarios.put("duringFirstMatch", new DuringFirstMatch());
        scenarios.put("afterFirstMatch", new AfterFirstMatch());
        scenarios.put("duringAnotherMatch", new DuringAnotherMatch());
        scenarios.put("beforeLastMatch", new BeforeLastMatch());
        scenarios.put("afterLastMatch", new AfterLastMatch());
    }

    @Override
    public League getLeague() {

        Scenario orDefault = scenarios.getOrDefault(scenarioName, l -> l);
        log.info("Current scenario: {}", orDefault.getClass().getSimpleName());
        return orDefault.alterLeague(original.getLeague());
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }
}
