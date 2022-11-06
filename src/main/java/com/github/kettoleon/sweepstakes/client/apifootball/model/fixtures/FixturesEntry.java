package com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FixturesEntry {

    private Fixture fixture;
    private League league;
    private Teams teams;
    private Goals goals;

    public LocalDateTime getTime() {
        return Instant.parse(fixture.getDate()).atZone(ZoneId.of("CET")).toLocalDateTime(); //TODO configurable timezone?
    }

    public String getHomeTeamName() {
        return teams.getHome().getName();
    }

    public String getAwayTeamName() {
        return teams.getAway().getName();
    }

    public int getHomeScore() {
        return goals.getHome();
    }

    public int getAwayScore() {
        return goals.getAway();
    }

    public String getRound() {
        return league.getRound();
    }

    public boolean isFinished() {
        return fixture.getStatus().isFinished();
    }

    public boolean isBeingPlayed() {
        return fixture.getStatus().isBeingPlayed();
    }

}