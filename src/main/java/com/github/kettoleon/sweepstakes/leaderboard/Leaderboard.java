package com.github.kettoleon.sweepstakes.leaderboard;

import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.users.repo.User;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
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

        int stakes = getParticipatingUsers().size()*5;

        for (User user : getParticipatingUsers()) {
            LeaderboardEntry entry = new LeaderboardEntry();
            entry.setUserName(user.getName());
            entry.setFinishedPoints(getUserTotalPoints(user));
            entry.setProgressPoints(getUserProgressPoints(user));
            entry.setFinishedExactPredictions(getUserExactPredictions(user));
            entry.setProgressExactPredictions(getUserProgressExactPredictions(user));
            leaderboard.add(entry);
        }

        leaderboard = leaderboard.stream()
                .sorted(comparingInt(LeaderboardEntry::getFinishedPoints)
                        .thenComparingInt(LeaderboardEntry::getFinishedExactPredictions)
                        .reversed())
                .collect(toList());
        int pos = 0;
        int points = Integer.MAX_VALUE;
        int exactGuesses = Integer.MAX_VALUE;
        for (LeaderboardEntry entry : leaderboard) {
            if (!(points == entry.getFinishedPoints() && exactGuesses == entry.getFinishedExactPredictions())) {
                pos++;
            }
            entry.setFinishedPosition(pos);
            points = entry.getFinishedPoints();
            exactGuesses = entry.getFinishedExactPredictions();
        }

        leaderboard = leaderboard.stream()
                .sorted(comparingInt(LeaderboardEntry::getProgressPoints)
                        .thenComparingInt(LeaderboardEntry::getProgressExactPredictions)
                        .reversed())
                .collect(toList());
        pos = 0;
        points = Integer.MAX_VALUE;
        exactGuesses = Integer.MAX_VALUE;
        for (LeaderboardEntry entry : leaderboard) {
            if (!(points == entry.getProgressPoints() && exactGuesses == entry.getProgressExactPredictions())) {
                pos++;
            }
            entry.setProgressPosition(pos);
            points = entry.getProgressPoints();
            exactGuesses = entry.getProgressExactPredictions();
        }

        List<LeaderboardEntry> firstPosition = leaderboard.stream().filter(e -> e.getProgressPosition() == 1).collect(toList());
        double fprize = Math.floor(100*((stakes*0.7)/firstPosition.size()))/100;
        firstPosition.forEach(e -> e.setPrize(fprize));

        List<LeaderboardEntry> secondPosition = leaderboard.stream().filter(e -> e.getProgressPosition() == 2).collect(toList());
        double sprize = Math.floor(100*((stakes*0.2)/secondPosition.size()))/100;
        secondPosition.forEach(e -> e.setPrize(sprize));

        List<LeaderboardEntry> thirdPosition = leaderboard.stream().filter(e -> e.getProgressPosition() == 3).collect(toList());
        double tprize = Math.floor(100*((stakes*0.1)/thirdPosition.size()))/100;
        thirdPosition.forEach(e -> e.setPrize(tprize));



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

    public int getUserExactPredictions(User user) {
        return (int) league.getFixtures().stream().filter(Fixture::isFinished).filter(f -> getBetInfo(user.getEmail(), f.getId()).isExactPrediction()).count();
    }

    private int getUserProgressPoints(User user) {
        return league.getFixtures().stream().filter(Fixture::isStartedOrFinished).mapToInt(f -> getBetInfo(user.getEmail(), f.getId()).getTotalPoints()).sum();
    }

    private int getUserProgressExactPredictions(User user) {
        return (int) league.getFixtures().stream().filter(Fixture::isStartedOrFinished).filter(f -> getBetInfo(user.getEmail(), f.getId()).isExactPrediction()).count();
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
