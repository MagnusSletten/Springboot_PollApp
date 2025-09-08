package com.example.backend.Model.Poll;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.backend.Model.User;
import com.example.backend.Model.Poll.Vote.Vote;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PollRequest {
    private String title;
    private String question;
    private ArrayList<VoteOption> voteOptions = new ArrayList<>();
    private Instant publishedAt;
    private Instant validUntil;
    private String creator;

    public Poll toPoll(Integer pollId, User creator){
    Poll poll = new Poll();
    poll.setPollID(pollId);
    poll.setCreator(creator);
    poll.setQuestion(question);
    poll.setVoteOptions(voteOptions);
    poll.setTitle(title);
    poll.setValidUntil(validUntil);
    poll.setPublishedAt(publishedAt);
    return poll; 
    }

}

