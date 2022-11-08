package com.github.kettoleon.sweepstakes.users.controller;

import com.github.kettoleon.sweepstakes.league.model.League;
import com.github.kettoleon.sweepstakes.league.LeagueProvider;
import com.github.kettoleon.sweepstakes.users.repo.User;
import com.github.kettoleon.sweepstakes.users.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$");

    @Autowired
    private LeagueProvider leagueProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment environment;

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loginForm() {
        return page("login", "Login");
    }


    @GetMapping("/register")
    public ModelAndView register() {
        return page("register", "Registration Form").addObject("user", new FormUser()).addObject("success", false);
    }

    @PostMapping("/register")
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Validated FormUser formUser,
            Errors errors
    ) {

        try {
            validateRegisterForm(formUser, errors);
            if (!errors.hasErrors()) {
                userRepository.save(mapToUser(formUser));
            }
        } catch (Exception e) {
            errors.reject("internal.error", "There has been an internal error, try again or contact organisers.");
            log.error("Error registering new user", e); //TODO show global errors on UI
        }

        boolean success = !errors.hasErrors();

        return page("register", "Registration Form")
                .addObject("user", formUser)
                .addObject("errors", errors)
                .addObject("success", success)
                ;
    }

    private User mapToUser(FormUser formUser) {
        User user = new User();
        user.setName(formUser.getName());
        user.setEmail(formUser.getEmail());
        user.setPasswordHash(passwordEncoder.encode(formUser.getPassword()));

        if (getPropertyAdmins().contains(formUser.getEmail())) {
            user.setAdmin(true);
        }
        user.setEnabled(true);

        return user;
    }

    private List<String> getPropertyAdmins() {
        return Arrays.stream(environment.getProperty("admins", "").split(",")).collect(Collectors.toList());
    }

    private void validateRegisterForm(FormUser formUser, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "emptyField", "Name must not be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "emptyField", "Email must not be empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "emptyField", "Password must not be empty");

        if (formUser.getName().length() > 30) {
            errors.rejectValue("name", "tooLong", "Name cannot exceed 30 characters");
        }

        if (!EMAIL_PATTERN.matcher(formUser.getEmail()).matches()) {
            errors.rejectValue("email", "invalidEmail", "Invalid e-mail address");
        }

        if (!emailFromAllowedDomains(formUser.getEmail())) {
            errors.rejectValue("email", "invalidEmail", "E-mail address has to belong to one of the allowed domains: (" + String.join(", ", getPropertyAllowedEmailDomains()) + ")");
        }

        userRepository.findByName(formUser.getName()).ifPresent(u -> {
            errors.rejectValue("name", "alreadyUsed", "This name is already being used, please pick another one");
        });

        userRepository.findByEmail(formUser.getEmail()).ifPresent(u -> {
            errors.rejectValue("email", "alreadyUsed", "This e-mail address is already registered.");
        });

    }

    private List<String> getPropertyAllowedEmailDomains() {
        return Arrays.stream(environment.getProperty("registration.domains.allowed", "").split(",")).collect(Collectors.toList());
    }

    private boolean emailFromAllowedDomains(String email) {
        if (getPropertyAllowedEmailDomains().isEmpty()) {
            return true;
        }
        for (String domain : getPropertyAllowedEmailDomains()) {
            if (email.endsWith("@" + domain)) {
                return true;
            }
        }
        return false;
    }

    //TODO eventually
//    @GetMapping("/validate")
//    public ModelAndView validate() {
//        return page("validate");
//    }
//
//    @GetMapping("/reset")
//    public ModelAndView reset() {
//        return page("reset");
//    }


    @GetMapping(value = "/users")
    public ModelAndView adminUsers() {
        FormAdminUsers form = new FormAdminUsers();
        form.setUsers(userRepository.findAll().stream().sorted(Comparator.comparing(User::getEmail)).collect(Collectors.toList()));
        return page("users", "Admin Users").addObject("form", form);
    }

    @PostMapping(value = "/users")
    public ModelAndView adminUsers(@ModelAttribute("form") FormAdminUsers form) {
        userRepository.saveAllAndFlush(form.getUsers());
        return adminUsers();
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
