package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Model.Poll.Poll;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/poll")
    public String testPoll(@RequestBody Poll poll) {
        if (poll.getCreator() == null) {
            return "Creator is null!";
        } else {
            return "Creator userName = " + poll.getCreator().getUserName();
        }
    }
}
