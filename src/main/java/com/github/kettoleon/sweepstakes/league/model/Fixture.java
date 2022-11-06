package com.github.kettoleon.sweepstakes.league.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class Fixture {

    private long id;
    private LocalDateTime time;
    private Team home;
    private Team away;
    private int scoreHome;
    private int scoreAway;
    private String round;
    private boolean started;
    private boolean finished;

    public String getFormattedTime() {
        return time.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
    }

    public boolean isStartedOrFinished() {
        return started || finished;
    }

}
