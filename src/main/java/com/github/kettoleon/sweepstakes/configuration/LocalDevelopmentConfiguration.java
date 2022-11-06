package com.github.kettoleon.sweepstakes.configuration;

import com.github.javafaker.Faker;
import com.github.kettoleon.sweepstakes.bet.repo.BetsRepository;
import com.github.kettoleon.sweepstakes.users.repo.User;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @EventListener(ApplicationReadyEvent.class)
    public void addRandomUsersAndBetsIfNeeded() {
        if (userRepository.findAll().isEmpty()) {
            for (int i = 0; i < 50; i++) {
                User user = createUser(i);
                log.info("    - Creating user: " + user.getEmail());
                userRepository.save(user);
            }
        }
    }

    private User createUser(int id) {
        Faker faker = new Faker();
        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(faker.name().username() + "@gmail.com");
        user.setPasswordHash(passwordEncoder.encode("pass"));
        user.setEnabled(true);
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
