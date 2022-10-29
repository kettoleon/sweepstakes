package com.github.kettoleon.sweepstakes.users.repo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
public class User {

    @Id
    private String email;

    private String name;
    private String passwordHash;
    private boolean admin;
    private boolean contact;
    private boolean paid;


}
