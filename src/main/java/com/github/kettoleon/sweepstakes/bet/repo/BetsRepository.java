package com.github.kettoleon.sweepstakes.bet.repo;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetsRepository extends JpaRepositoryImplementation<FixtureBet, String> {
    List<FixtureBet> findAllByEmail(String email);

    List<FixtureBet> findAllByFixtureId(long fixtureId);
}
