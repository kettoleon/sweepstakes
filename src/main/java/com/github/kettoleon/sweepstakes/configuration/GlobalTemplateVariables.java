package com.github.kettoleon.sweepstakes.configuration;

import com.github.kettoleon.sweepstakes.client.apifootball.RestApiFootballClient;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.users.repo.User;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@ControllerAdvice(annotations = Controller.class)
public class GlobalTemplateVariables {

    @Autowired
    private LeagueProvider leagueProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private RestApiFootballClient restApiFootballClient;

    public static ModelAndView page(String viewId, String title) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("page", viewId);
        modelAndView.addObject("pageTitle", title);
        return modelAndView;
    }


    @ModelAttribute("version")
    public String version() {
        return Optional.ofNullable(getClass().getPackage().getImplementationVersion()).orElse("0.0.x-SNAPSHOT");
    }

    @ModelAttribute("league")
    public League league() {
        return leagueProvider.getLeague();
    }

    @ModelAttribute("lastUpdate")
    public String lastUpdate() {
        return restApiFootballClient == null ? null : formatUpdateTime(restApiFootballClient.getLastCacheTime());
    }

    private String formatUpdateTime(LocalDateTime time) {
        if(time.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)) || time.isAfter(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS))){
            return time.format(DateTimeFormatter.ofPattern("MMM, dd HH:mm"));
        }
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @ModelAttribute("nextUpdate")
    public String nextUpdate() {
        return restApiFootballClient == null ? null : formatUpdateTime(restApiFootballClient.getNextUpdateTime());
    }

    @ModelAttribute("user")
    public User user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) return null;
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

}
