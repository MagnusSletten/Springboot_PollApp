package com.example.backend.Model.Poll;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.backend.Model.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,  property = "pollID")
public class Poll {
    private Integer pollID; 
    private String title;
    private String question;
    private ArrayList<VoteOption> voteOptions; 
    private Instant publishedAt;
    private Instant validUntil;
    private List<Vote> votes; 

    private User creator;
    
    public void setCreator(User creator){
        this.creator = creator;
        this.creator.addPolls(this);

    }

    public void addVote(Vote vote){
        this.votes.add(vote);
    }
    
}
