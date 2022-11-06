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
        return userRepository.findAll().stream().filter(User::isEnabled).sorted(Comparator.comparing(User::getEmail)).collect(Collectors.toList());
    }

    public String getBet(String email, long fixtureId) {
        FixtureBet bet = betsRepository.findByEmailAndFixtureId(email, fixtureId);
        if (bet == null) return "NP";
        return bet.getHome() + "-" + bet.getAway();
    }

}
