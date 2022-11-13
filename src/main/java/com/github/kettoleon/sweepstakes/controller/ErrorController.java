package com.github.kettoleon.sweepstakes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static com.github.kettoleon.sweepstakes.configuration.GlobalTemplateVariables.page;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            return page("error", "Oops! This is so embarrassing...")
                    .addObject("statusCode", statusCode);
        }
        return page("error", "Oops! This is so embarrassing...");
    }

}