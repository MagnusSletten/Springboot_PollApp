package com.example.backend.Controllers;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Model.User;
import com.example.backend.Model.UserRequest;

import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/users")
public class UserController {
    PollManager pollManager; 
    
    public UserController(PollManager pollManager){
        this.pollManager = pollManager;
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer userId) {
        return pollManager.getUser(userId);
    }

    @PostMapping
    public User addUser(@RequestBody UserRequest userRequest){
        User user = pollManager.addUserFromRequest(userRequest);
        return user; 
    }

    @GetMapping
    public Collection<User> getUsers(){
       return pollManager.getUsers();  
    }
}

