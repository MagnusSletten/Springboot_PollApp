package com.example.backend.Controllers;

import java.util.HashMap;
import java.util.List;

import org.h2.engine.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Managers.PollManager;
import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.Poll.PollDTO;
import com.example.backend.Model.User.User;

@RestController
@CrossOrigin
@RequestMapping("/polls")
public class PollController {
    HashMap<Integer, Poll> polls;
    PollManager manager;

    public PollController(PollManager manager) {
        this.polls = new HashMap<>();
        this.manager = manager;
    }

    @PostMapping
    public Poll addPoll(@RequestBody PollDTO pollRequest) {
        Poll poll = manager.addPollFromRequest(pollRequest);
        return poll;
    }

    @DeleteMapping("/{pollID}")
    public ResponseEntity<?> deletePoll(@PathVariable Integer pollID, @AuthenticationPrincipal UserDetails user) {
        var pollOpt = manager.getPoll(pollID);
        if (pollOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var poll = pollOpt.get();
        if (!poll.getCreatedBy().getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        manager.deletePoll(pollID);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{pollID}")
    public Poll getPoll(@PathVariable("pollID") Integer pollID) throws Exception {
        try {
            return manager.getPoll(pollID).get();
        } catch (Exception e) {
            throw e;
        }

    }

    @GetMapping
    public ResponseEntity<List<Poll>> getPolls(@RequestParam(value = "user", required = false) String username) {
        List<Poll> polls;
        if (username != null) {
            polls = manager.getPollsByUserName(username);
        } else {
            polls = manager.getPolls();
        }
        return ResponseEntity.ok(polls);
    }

}