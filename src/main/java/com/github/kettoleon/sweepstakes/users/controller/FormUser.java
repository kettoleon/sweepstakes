package com.github.kettoleon.sweepstakes.users.controller;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
public class FormUser {

    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private String password;


}
