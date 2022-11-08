package com.github.kettoleon.sweepstakes.localdev;

import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ConditionalOnProperty(name = "localdev", havingValue = "true")
public class LocalDevController {

    @Autowired
    private MockedLeagueProvider leagueProvider;

    @GetMapping("/localdev")
    public ModelAndView localdev() {
        return page("localdev", "Local Development Tools");
    }

    @GetMapping("/localdev/{scenario}")
    public ModelAndView setScenario(@PathVariable("scenario") String mode) {
        leagueProvider.setScenarioName(mode);
        return page("localdev", "Local Development Tools");
    }

    public ModelAndView page(String viewId, String title) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("page", viewId);
        modelAndView.addObject("pageTitle", title);
        League league = leagueProvider.getLeague();
        modelAndView.addObject("league", league);
        return modelAndView;
    }


}

