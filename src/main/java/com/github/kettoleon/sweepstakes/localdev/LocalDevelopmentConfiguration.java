package com.github.kettoleon.sweepstakes.localdev;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.bet.repo.FixtureBet;
import com.github.kettoleon.sweepstakes.league.model.Fixture;
import com.github.kettoleon.sweepstakes.users.repo.User;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
@ConditionalOnProperty(name = "localdev", havingValue = "true")
public class LocalDevelopmentConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LocalDevelopmentConfiguration.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BetsRepository betsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Random r = new Random();

    @Bean
    @Primary
    public MockedLeagueProvider mockedLeagueProvider() {
        return new MockedLeagueProvider("before", new MockedApiFootballClient(4, 2024));
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addRandomUsersAndBetsIfNeeded() {

        if (userRepository.findAll().isEmpty()) {
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                User user = createUser(i);
                if (r.nextInt(0, 100) > 95) {
                    user.setContact(true);
                }
                if (r.nextInt(0, 100) > 95) {
                    user.setPaid(false);
                }
                users.add(user);
                log.info("    - Created user: " + user.getEmail());
            }
            userRepository.saveAllAndFlush(users);
            log.info("    - Done creating users");
        }
        if (betsRepository.findAll().isEmpty()) {
            log.info("    - Creating everyone's bets...");
            for (User user : userRepository.findAll()) {
                List<FixtureBet> bets = new ArrayList<>();
                for (Fixture fixture : mockedLeagueProvider().getLeague().getFixtures()) {
                    FixtureBet bet = new FixtureBet();
                    bet.setId(UUID.randomUUID().toString());
                    bet.setFixtureId(fixture.getId());
                    bet.setEmail(user.getEmail());
                    bet.setHome(r.nextInt(0, 6));
                    bet.setAway(r.nextInt(0, 6));
                    bets.add(bet);
                    if (r.nextInt(0, 100) < 1) {
                        if (r.nextInt(0, 100) > 50) {
                            bet.setHome(null);
                            bet.setAway(null);
                        } else {
                            if (r.nextInt(0, 100) > 50) {
                                bet.setHome(null);
                            } else {
                                bet.setAway(null);
                            }
                        }
                        if (r.nextInt(0, 100) > 50) {
                            bets.remove(bet);
                        }
                    }
                }

                betsRepository.saveAllAndFlush(bets);
            }
            log.info("    - Done creating everyone's bets");
        }
    }

    private User createUser(int id) {
        User user = new User();
        Name name = new Faker().name();
        String fullName = name.firstName() + " " + name.lastName();
        user.setName((r.nextBoolean() ? name.prefix() + " " : "") + fullName);
        user.setEmail(fullName.toLowerCase().replace(' ', '.') + "@gmail.com");
        user.setPasswordHash(passwordEncoder.encode("pass"));
        user.setEnabled(true);
        user.setPaid(true);
        if (id == 0) {
            user.setName("kettoleon");
            user.setEmail("kettoleon@gmail.com");
            user.setAdmin(true);
        }
        if (id == 1) {
            user.setName("disabled");
            user.setEmail("disabled@gmail.com");
            user.setEnabled(false);
        }
        return user;
    }

}
