package com.github.kettoleon.sweepstakes.users.controller;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class FormUserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(FormUser.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof FormUser) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "emptyField", "Name must not be empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "emptyField", "Email must not be empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "emptyField", "Password must not be empty");

            FormUser formUser = (FormUser) target;
            //TODO name not used already
            //TODO email not used already and valid

        }
    }
}
