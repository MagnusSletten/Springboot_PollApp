package com.example.backend.Controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;


import com.example.backend.Model.User;
import com.example.backend.Model.UserRequest;
import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.Poll.PollRequest;
import com.example.backend.Model.Poll.Vote.Vote;
import com.example.backend.Model.Poll.Vote.VoteRequest;

@Component
public class PollManager {
    HashMap<Integer,Poll> polls;
    HashMap<String,User> users;
    
    Integer maxPollId = 1;
    Integer maxUserId = 1;  
            
    public PollManager() {
        this.polls = new HashMap<>();
        this.users = new HashMap<>(); 

    }

    public Poll addPollFromRequest(PollRequest pollRequest){
        User user = users.get(pollRequest.getCreator()); 
        Poll poll = pollRequest.toPoll(maxPollId, user);
        user.addPolls(poll);
        polls.put(maxPollId, poll);
        maxPollId++;     
        return poll; 
    }

    public Poll getPoll(Integer poll_id){
       return polls.get(poll_id);
    }

    public User getUser(Integer userID){
        return users.get(userID);
        
    }

    public Collection<User> getUsers(){
        return users.values(); 
    }

    public User addUserFromRequest(UserRequest userRequest){
      User user = userRequest.toUser();
      users.put(userRequest.getUserName(), user);
      return user; 
    }

    public List<Vote> getVotes(Integer pollID){
        if(polls.get(pollID).getVotes().contains(pollID)){
            polls.get(pollID).getVotes();
        }
        throw new NoSuchElementException("No votes found for " + pollID);
        
    }

    public Vote addVote(VoteRequest voteRequest) {
        User user = users.get(voteRequest.getUserName());
        Poll poll = polls.get(voteRequest.getPollId());
        
        Vote vote = voteRequest.toVote(user, poll);

        if (!user.hasVoted(poll.getPollID())){
            poll.addVote(vote);

        }
        else {
            poll.changeVote(vote);
        }
        return vote; 
       
    }
       
    

    public void removeVote(Integer pollID, Integer userId){
        getVotes(pollID).remove((Vote) getUserVote(pollID, userId));

    
    }

    public Vote getUserVote(Integer pollID, Integer userId){
        for(Vote vote : this.getVotes(pollID)){
            if(vote.getVoteId().equals(userId)){
                return vote; 
            }

        }
       throw new NoSuchElementException("This pollID not found in votes");
    }

    



}



