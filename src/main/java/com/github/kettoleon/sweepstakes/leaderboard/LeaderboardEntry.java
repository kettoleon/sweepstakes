package com.github.kettoleon.sweepstakes.leaderboard;

import lombok.Data;

@Data
public class LeaderboardEntry {

    private String userName;
    private int finishedPosition;
    private int finishedPoints;
    private int progressPosition;
    private int progressPoints;

    public String getPoints() {
        if (pointsDelta() == 0) {
            return progressPoints + "";
        }
        return progressPoints + " (" + (pointsDelta() > 0 ? "+" : "") + pointsDelta() + ")";
    }

    public int pointsDelta() {
        return progressPoints - finishedPoints;
    }

    public String getPosition() {
        if (positionDelta() == 0) {
            return progressPosition + "";
        }
        return progressPosition + " (" + (positionDelta() > 0 ? "+" : "") + positionDelta() + ")";
    }

    public int positionDelta() {
        return finishedPosition - progressPosition;
    }

}
