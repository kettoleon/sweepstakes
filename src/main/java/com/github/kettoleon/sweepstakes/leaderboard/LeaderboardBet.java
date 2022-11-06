package com.github.kettoleon.sweepstakes.leaderboard;

import com.github.kettoleon.sweepstakes.bet.repo.FixtureBet;
import com.github.kettoleon.sweepstakes.league.model.Fixture;
import lombok.Data;

@Data
public class LeaderboardBet {

    private FixtureBet fixtureBet;
    private Fixture fixture;

    public LeaderboardBet(Fixture fixture, FixtureBet fixtureBet) {
        this.fixture = fixture;
        this.fixtureBet = fixtureBet;
    }

    public String getBet() {
        if (fixtureBet == null) return "NP";
        return fixtureBet.getHome() + "-" + fixtureBet.getAway();
    }

    public boolean shouldCountPoints() {
        return fixtureBet != null && fixture != null && fixture.isStartedOrFinished();
    }

    public int getResultPoints() {
        if (shouldCountPoints()) {
            if (tieMatch() || homeWinnerMatch() || awayWinnerMatch()) {
                return 3;
            }
        }
        return 0;
    }

    private boolean awayWinnerMatch() {
        return betAwayWinner() && resultAwayWinner();
    }

    private boolean homeWinnerMatch() {
        return betHomeWinner() && resultHomeWinner();
    }

    private boolean resultHomeWinner() {
        return fixture.getScoreHome() > fixture.getScoreAway();
    }

    private boolean resultAwayWinner() {
        return fixture.getScoreAway() > fixture.getScoreHome();
    }

    private boolean betHomeWinner() {
        return fixtureBet.getHome() > fixtureBet.getAway();
    }

    private boolean betAwayWinner() {
        return fixtureBet.getAway() > fixtureBet.getHome();
    }

    private boolean tieMatch() {
        return betIsTie() && resultIsTie();
    }

    private boolean resultIsTie() {
        return fixture.getScoreHome() == fixture.getScoreAway();
    }

    private boolean betIsTie() {
        return fixtureBet.getAway() == fixtureBet.getHome();
    }

    public int getGoalPoints() {
        if (shouldCountPoints()) {
            int pts = 0;
            if (homeScoreMatches()) {
                pts++;
            }
            if (awayScoreMatches()) {
                pts++;
            }
            return pts;
        }
        return 0;
    }

    private boolean awayScoreMatches() {
        return fixtureBet.getAway() == fixture.getScoreAway();
    }

    private boolean homeScoreMatches() {
        return fixtureBet.getHome() == fixture.getScoreHome();
    }

    public int getBonusPoints() {
        if (shouldCountPoints()) {
            if (homeScoreMatches() && awayScoreMatches()) {
                return 2;
            }
        }
        return 0;
    }

    public int getTotalPoints() {
        if (shouldCountPoints()) {
            return getResultPoints() + getGoalPoints() + getBonusPoints();
        }
        return 0;
    }

}
