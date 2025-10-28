package com.example.backend.Model.User;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.NoArgsConstructor;

/*Simplified User class that can be turned into full User objects. Improves API behaviour. */
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
public class UserDTO   {
    private String username;
    private String email;
    private String password; 

    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setPolls(new ArrayList<>());
        user.setUsername(username);
        user.setVotes(new ArrayList<>());
        return user;

    }

}