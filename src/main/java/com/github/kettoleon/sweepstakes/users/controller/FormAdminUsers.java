package com.github.kettoleon.sweepstakes.users.controller;

import com.github.kettoleon.sweepstakes.users.repo.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormAdminUsers {
    private List<User> users = new ArrayList<>();
}
