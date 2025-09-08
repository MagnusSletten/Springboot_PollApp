package com.example.backend.Model.Poll.Vote;
import lombok.Data;
import java.time.Instant;

import com.example.backend.Model.User;
import com.example.backend.Model.Poll.Poll;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,  property = "voteId")
public class Vote {
    Integer voteId; 
    Instant publishedAt = Instant.now();
    @JsonIdentityReference(alwaysAsId = true)
    User voter;
    @JsonIdentityReference(alwaysAsId = true)
    Poll poll; 
    
}

