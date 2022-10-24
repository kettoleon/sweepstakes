package com.github.kettoleon.sweepstakes.league.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class Fixture {

    private LocalDateTime time;
    private Team home;
    private Team away;
    private int scoreHome;
    private int scoreAway;
    private String round;

    public String getFormattedTime() {
        return time.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
    }

}
