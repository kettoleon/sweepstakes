package com.github.kettoleon.sweepstakes.league.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import static org.apache.commons.lang3.StringUtils.substringBetween;

@Data
@NoArgsConstructor
public class Team {

    private String name;
    private String icon;

    public String getCode() {
        return substringBetween(icon, "/", ".").toUpperCase();
    }

}
