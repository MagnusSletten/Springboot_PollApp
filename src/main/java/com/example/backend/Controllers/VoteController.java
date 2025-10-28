package com.example.backend.Controllers;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Managers.PollManager;

import com.example.backend.Model.Vote.Vote;
import com.example.backend.Model.Vote.VoteDTO;

@RestController
@CrossOrigin
@RequestMapping("/polls")
public class VoteController {
    PollManager pollManager;

    public VoteController(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @PostMapping("/{pollID}/votes")
    public ResponseEntity<VoteDTO> addVote(@RequestBody VoteDTO voteRequest) throws Exception {
        pollManager.addVote(voteRequest);
        try {
            return ResponseEntity.ok(voteRequest);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/{pollID}/votes")
    public List<Vote> getVotes(@PathVariable Integer pollID) {
        return pollManager.getVotes(pollID);
    }

    @GetMapping("/{pollID}/votes/results")
    public ResponseEntity<Map<Integer, Integer>> getVoteResults(@PathVariable Integer pollID) {
        try {
            Map<Integer, Integer> results = pollManager.getVoteResults(pollID);
            return ResponseEntity.ok(results);
        } catch (NoSuchElementException e) {
            System.out.println("Poll not found: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
