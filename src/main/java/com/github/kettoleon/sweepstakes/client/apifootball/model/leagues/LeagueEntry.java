package com.github.kettoleon.sweepstakes.client.apifootball.model.leagues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.League;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LeagueEntry {

    private League league;

    private List<Season> seasons;
}
