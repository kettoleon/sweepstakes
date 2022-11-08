package com.github.kettoleon.sweepstakes;

import com.github.kettoleon.sweepstakes.client.apifootball.ApiFootballClient;
import com.github.kettoleon.sweepstakes.client.apifootball.model.countries.CountriesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.fixtures.FixturesResponse;
import com.github.kettoleon.sweepstakes.client.apifootball.model.leagues.LeagueResponse;
import com.github.kettoleon.sweepstakes.league.ApiFootballLeagueProvider;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
        return new ApiFootballLeagueProvider(new ApiFootballClient(1, 2022));
    }

}

