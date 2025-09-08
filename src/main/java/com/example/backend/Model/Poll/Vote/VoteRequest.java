package com.example.backend.Model.Poll.Vote;

import java.time.Instant;

import com.example.backend.Model.User;
import com.example.backend.Model.Poll.Poll;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,  property = "voteId")
public class VoteRequest {
    Integer voteId; 
    Instant publishedAt = Instant.now();
    String userName;
    Integer pollId;
    
    public Vote toVote(User voter,Poll poll){
        Vote vote = new Vote(); 
        vote.setPoll(poll);
        vote.setPublishedAt(publishedAt);
        vote.setVoter(voter);
        return vote; 

    }
    
}