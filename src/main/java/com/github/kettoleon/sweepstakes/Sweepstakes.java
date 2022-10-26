package com.github.kettoleon.sweepstakes;

import com.github.kettoleon.sweepstakes.league.model.ApiFootballLeagueProvider;
import com.github.kettoleon.sweepstakes.league.model.LeagueProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class Sweepstakes {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Sweepstakes.class);
        application.addListeners(new ApplicationPidFileWriter("./service.pid"));
        application.run(args);

    }

    @Bean
    public LeagueProvider leagueProvider() {
        return new ApiFootballLeagueProvider(1, 2022);//TODO from properties?
    }

}

