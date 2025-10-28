package com.example.backend.Model.Vote;

import java.time.Instant;

import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.User.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Data;

/*Simplified Vote class that can be turned into full Vote objects. Improves API behaviour. */

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "voteId")
public class VoteDTO {
    Integer voteId;
    Instant publishedAt = Instant.now();
    String userName;
    Integer pollId;
    Integer presentationOrder;
    @JsonIgnore
    ObjectMapper mapper = initJackson();

    public Vote toVote(User voter, Poll poll) {

        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setPoll(poll);
        vote.setPublishedAt(publishedAt);
        vote.setVotesOn(poll.getOptions().get(presentationOrder - 1));

        return vote;

    }

    public Vote toVoteAnonymous(Poll poll) {

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setPublishedAt(publishedAt);
        System.out.println(poll.getOptions());
        vote.setVotesOn(poll.getOptions().get(presentationOrder - 1));

        return vote;

    }

    public boolean hasUsername() {
        return userName != null && !userName.isBlank();
    }

    public String toJson() {

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize VoteRequest to JSON", e);
        }
    }

    public static VoteDTO fromJson(String json) {
        ObjectMapper mapper = initJackson();
        try {
            return mapper.readValue(json, VoteDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to VoteRequest", e);
        }
    }

    private static ObjectMapper initJackson() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

}