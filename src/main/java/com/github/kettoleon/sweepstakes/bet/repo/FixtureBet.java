package com.github.kettoleon.sweepstakes.bet.repo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class FixtureBet {

    @Id
    private String id;

    private String email;
    private long fixtureId;
    private int home;
    private int away;

}
