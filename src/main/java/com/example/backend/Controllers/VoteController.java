package com.example.backend.Controllers;

import java.util.HashMap;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.Poll.Vote.Vote;
import com.example.backend.Model.Poll.Vote.VoteRequest;

@RestController
@RequestMapping("/votes")
public class VoteController {
    PollManager pollManager;

    public VoteController(PollManager pollManager){
        this.pollManager = pollManager; 
    }

    @PostMapping("/")
    public Vote addVote(@RequestBody VoteRequest voteRequest){ 
        Vote vote = pollManager.addVote(voteRequest);  
        return vote;
        }
       
}




