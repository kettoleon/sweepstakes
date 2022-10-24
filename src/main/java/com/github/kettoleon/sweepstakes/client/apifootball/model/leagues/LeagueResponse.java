package com.github.kettoleon.sweepstakes.client.apifootball.model.leagues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.League;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LeagueResponse {

    private List<LeagueEntry> response = new ArrayList<>();

    public String getLeagueName() {
        return getLeague().getName();
    }

    public String getLeagueLogo() {
        return StringUtils.substringAfterLast(getLeague().getLogo(), "/football/");
    }

    public String getSeasonName() {
        return String.valueOf(getSeason().getYear());
    }

    public LocalDateTime getSeasonStart() {
        return LocalDate.parse(getSeason().getStart()).atStartOfDay();
    }

    public LocalDateTime getSeasonEnd() {
        return LocalDate.parse(getSeason().getStart()).atStartOfDay();
    }

    private League getLeague() {
        return response.get(0).getLeague();
    }

    private Season getSeason() {
        return response.get(0).getSeasons().get(0);
    }


}
