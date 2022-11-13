package com.github.kettoleon.sweepstakes.controller;

import com.github.kettoleon.sweepstakes.users.repo.User;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

import static com.github.kettoleon.sweepstakes.configuration.GlobalTemplateVariables.page;

@Controller
public class WebController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = {"", "/", "/rules"})
    public ModelAndView index() {
        return page("rules", "Sweepstake Rules")
                .addObject("contacts", userRepository.findAll().stream().filter(User::isContact).collect(Collectors.toList()));
    }

    @GetMapping("/calendar")
    public ModelAndView calendar() {
        return page("calendar", "Calendar");
    }

}
