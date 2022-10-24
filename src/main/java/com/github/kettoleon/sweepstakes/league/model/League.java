package com.github.kettoleon.sweepstakes.league.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public LocalDateTime start;
    public LocalDateTime end;
    public List<Fixture> fixtures = new ArrayList<>();

    public List<Fixture> getFixturesByDate() {
        return fixtures.stream().sorted(Comparator.comparing(Fixture::getTime)).collect(Collectors.toList());
    }

    public String getFormattedStart() {
        return start.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
    }

}
