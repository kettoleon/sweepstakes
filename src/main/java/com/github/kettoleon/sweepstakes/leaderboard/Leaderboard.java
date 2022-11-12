package com.github.kettoleon.sweepstakes.leaderboard;

import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.bet.repo.FixtureBet;
import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.users.repo.User;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Data
public class Leaderboard {

    private final UserRepository userRepository;
    private final BetsRepository betsRepository;
    private final League league;

    public Leaderboard(UserRepository userRepository, BetsRepository betsRepository, League league) {
        this.userRepository = userRepository;
        this.betsRepository = betsRepository;
        this.league = league;
    }

    public List<LeaderboardEntry> getLeaderboardEntries() {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        for (User user : getParticipatingUsers()) {
            LeaderboardEntry entry = new LeaderboardEntry();
            entry.setUserName(user.getName());
            entry.setFinishedPoints(getUserTotalPoints(user));
            entry.setProgressPoints(getUserProgressPoints(user));
            leaderboard.add(entry);
        }

        leaderboard = leaderboard.stream().sorted(Comparator.comparingInt(LeaderboardEntry::getFinishedPoints).reversed()).collect(toList());
        int pos = 0;
        int points = Integer.MAX_VALUE;
        for (LeaderboardEntry entry : leaderboard) {
            if (points > entry.getFinishedPoints()) {
                pos++;
            }
            entry.setFinishedPosition(pos);
            points = entry.getFinishedPoints();
        }

        leaderboard = leaderboard.stream().sorted(Comparator.comparingInt(LeaderboardEntry::getProgressPoints).reversed()).collect(toList());
        pos = 0;
        points = Integer.MAX_VALUE;
        for (LeaderboardEntry entry : leaderboard) {
            if (points > entry.getProgressPoints()) {
                pos++;
            }
            entry.setProgressPosition(pos);
            points = entry.getProgressPoints();
        }

        return leaderboard;
    }


    public List<DetailedLeaderboardGroup> getDetailedLeaderboardGroups() {
        List<DetailedLeaderboardGroup> groups = new ArrayList<>();

        List<Fixture> groupFixtures = new ArrayList<>();
        List<LocalDateTime> groupDays = new ArrayList<>();
        for (LocalDateTime leagueDay : league.getLeagueDays()) {
            groupFixtures.addAll(league.getFixturesByDay(leagueDay));
            groupDays.add(leagueDay);
            if (groupFixtures.size() >= 4) {
                groups.add(new DetailedLeaderboardGroup(groupName(groupDays), groupFixtures));
                groupFixtures = new ArrayList<>();
                groupDays = new ArrayList<>();
            }
        }
        return groups;
    }

    private String groupName(List<LocalDateTime> groupDays) {
        return groupDays.stream().map(league::formatDay).collect(Collectors.joining(", "));
    }

    public List<User> getParticipatingUsers() {

        if (league.hasFirstMatchStarted()) {
            return userRepository.findAll().stream()
                    .filter(User::isEnabled).filter(User::isPaid)
                    .sorted(comparing(User::getEmail))
                    .collect(toList());
        } else {

            return userRepository.findAll().stream()
                    .filter(User::isEnabled)
                    .sorted(comparing(User::getEmail))
                    .collect(toList());
        }
    }

    public LeaderboardBet getBetInfo(String email, long fixtureId) {

        return new LeaderboardBet(league.getFixtureById(fixtureId), betsRepository.findByEmailAndFixtureId(email, fixtureId));

    }

    public String getFixtureResult(Fixture fixture) {
        if (fixture.isStartedOrFinished()) {
            return fixture.getScoreHome() + " - " + fixture.getScoreAway();
        }
        return "-";
    }

    public int getUserTotalPoints(User user, DetailedLeaderboardGroup group) {
        return group.getFixtures().stream().filter(Fixture::isFinished).mapToInt(f -> getBetInfo(user.getEmail(), f.getId()).getTotalPoints()).sum();
    }

    public int getUserTotalPoints(User user) {
        return league.getFixtures().stream().filter(Fixture::isFinished).mapToInt(f -> getBetInfo(user.getEmail(), f.getId()).getTotalPoints()).sum();
    }

    private int getUserProgressPoints(User user) {
        return league.getFixtures().stream().filter(Fixture::isStartedOrFinished).mapToInt(f -> getBetInfo(user.getEmail(), f.getId()).getTotalPoints()).sum();
    }

    public boolean isAnyFixtureFinished() {
        for (Fixture fixture : league.getFixtures()) {
            if (fixture.isFinished()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnyFixtureStarted() {
        for (Fixture fixture : league.getFixtures()) {
            if (fixture.isStarted()) {
                return true;
            }
        }
        return false;
    }

}
