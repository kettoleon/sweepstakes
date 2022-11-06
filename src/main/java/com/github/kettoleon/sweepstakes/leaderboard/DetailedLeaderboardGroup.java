package com.github.kettoleon.sweepstakes.leaderboard;

import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.users.repo.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DetailedLeaderboardGroup {
    private String name;
    private List<Fixture> fixtures;

    public DetailedLeaderboardGroup(String name, List<Fixture> fixtures) {

        this.name = name;
        this.fixtures = fixtures;
    }

    public boolean isAnyFixtureStartedOrFinished() {
        for (Fixture fixture : fixtures) {
            if (fixture.isStartedOrFinished()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnyFixtureFinished() {
        for (Fixture fixture : fixtures) {
            if (fixture.isFinished()) {
                return true;
            }
        }
        return false;
    }
}
