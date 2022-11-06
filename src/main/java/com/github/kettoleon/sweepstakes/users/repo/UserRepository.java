package com.github.kettoleon.sweepstakes.users.repo;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);
}
