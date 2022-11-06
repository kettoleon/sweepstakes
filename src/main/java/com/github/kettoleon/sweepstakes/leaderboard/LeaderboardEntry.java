package com.github.kettoleon.sweepstakes.leaderboard;

import lombok.Data;

@Data
public class LeaderboardEntry {

    private String userName;
    private int finishedPosition;
    private int finishedPoints;
    private int progressPosition;
    private int progressPoints;
}
