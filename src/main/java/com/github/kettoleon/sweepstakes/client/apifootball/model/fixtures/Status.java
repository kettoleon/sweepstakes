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
        return code.equals("1H") || code.equals("2H") || code.equals("ET") || code.equals("BT") || code.equals("HT") || code.equals("P") || code.equals("SUSP") || code.equals("INT");
    }

    public boolean isFinished() {
        return code.equals("FT") || code.equals("AET") || code.equals("PEN");
    }

}
