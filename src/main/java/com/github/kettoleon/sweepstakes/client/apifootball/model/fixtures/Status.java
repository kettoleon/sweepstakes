package com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Status {

    @JsonProperty("long")
    private String description;

    @JsonProperty("short")
    private String code; //FT: finished, NS: Not started

    public boolean isBeingPlayed() {
        return code.equals("1H") || code.equals("HT") || code.equals("2H") || code.equals("ET") || code.equals("BT") || code.equals("P") || code.equals("SUSP") || code.equals("INT") || code.equals("LIVE");
    }

    public boolean isFinished() {
        return code.equals("FT") || code.equals("AET") || code.equals("PEN");
    }

    public boolean isScheduledOrLive() {
        //Difference between isBeingPlayed and isScheduledOrLive, (isScheduledOrLive is for updating information by polling, isBeingPlayed is for finalResults, a game might be suspended and continue next day...)
        return code.equals("NS") || code.equals("1H") || code.equals("HT") || code.equals("2H") || code.equals("ET") || code.equals("BT") || code.equals("P") || code.equals("INT") || code.equals("LIVE");

    }

}
