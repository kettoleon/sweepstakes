package com.github.kettoleon.sweepstakes;

import com.github.kettoleon.sweepstakes.client.apifootball.RestApiFootballClient;
import com.github.kettoleon.sweepstakes.localdev.MockedApiFootballClient;
import com.github.kettoleon.sweepstakes.league.ApiFootballLeagueProvider;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    @ConditionalOnProperty(name = "rapidapi.key")
    public LeagueProvider leagueProvider(Environment environment) {
        String rapidApiHost = environment.getProperty("rapidapi.host", "api-football-v1.p.rapidapi.com");
        String rapidApiKey = environment.getRequiredProperty("rapidapi.key");
        return new ApiFootballLeagueProvider(new RestApiFootballClient(1, 2022, rapidApiHost, rapidApiKey));
    }

}

