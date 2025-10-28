package com.example.backend.Controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Managers.PollManager;
import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.User.User;
import com.example.backend.Model.User.UserDTO;

import org.springframework.web.bind.annotation.PathVariable;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    PollManager pollManager;

    public UserController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @GetMapping("/{userName}")
    public User getUser(@PathVariable("userName") String userName) {
        return pollManager.getUser(userName).get();
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDTO userRequest) {

        try {
            User user = pollManager.addUserFromRequest(userRequest);
            if (user.getRole() == null) {
                user.setRole(User.Roles.NORMAL);
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();

        }
    }

    @GetMapping
    public Collection<User> getUsers() {
        return pollManager.getUsers();
    }
}
