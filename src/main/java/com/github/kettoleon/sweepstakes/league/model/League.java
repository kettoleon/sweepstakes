package com.github.kettoleon.sweepstakes.league.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class League {

    public String name;
    public String season;
    public String logo;

    public List<Fixture> fixtures = new ArrayList<>();

    public List<Fixture> getFixturesByDate() {
        return fixtures.stream().sorted(Comparator.comparing(Fixture::getTime)).collect(Collectors.toList());
    }

    public List<Fixture> getFixturesByDay(LocalDateTime day) {
        return fixtures.stream().filter(f -> f.getTime().truncatedTo(ChronoUnit.DAYS).isEqual(day.truncatedTo(ChronoUnit.DAYS))).sorted(Comparator.comparing(Fixture::getTime)).collect(Collectors.toList());
    }

    public List<LocalDateTime> getLeagueDays() {
        return fixtures.stream().map(f -> f.getTime().truncatedTo(ChronoUnit.DAYS)).distinct().sorted().collect(Collectors.toList());
    }

    public String formatTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
    }

    public String formatDay(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("MMM dd"));
    }

    public String getFormattedStart() {
        return formatTime(getStart());
    }

    public Fixture getFixtureById(long id) {
        return fixtures.stream().filter(f -> f.getId() == id).findFirst().orElseThrow();
    }

    public boolean isBeforeStart() {
        return LocalDateTime.now().isBefore(getStart());
    }

    public LocalDateTime getStart() {
        return getFixturesByDate().get(0).getTime().truncatedTo(ChronoUnit.DAYS);
    }

    public LocalDateTime getEnd() {
        List<Fixture> fixturesByDate = getFixturesByDate();
        return fixturesByDate.get(fixturesByDate.size() - 1).getTime().truncatedTo(ChronoUnit.DAYS);
    }

    public boolean hasFirstMatchStarted() {
        Fixture fm = getFixturesByDate().get(0);
        return fm.isStartedOrFinished();
    }

    public LocalDateTime getFirstMatchTime() {
        return getFixturesByDate().get(0).getTime();
    }

    public LocalDateTime getBetClosingTime() {
        return getFixturesByDate().get(0).getTime().minusHours(1);
    }

    public String getFormattedBetClosingTime() {
        return formatTime(getBetClosingTime());
    }

    public boolean areBetsClosed() {
        return LocalDateTime.now().isAfter(getBetClosingTime());
    }


}
