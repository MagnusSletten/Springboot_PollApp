package com.example.backend.Controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;


import com.example.backend.Model.User;
import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.Poll.Vote;

@Component
public class PollManager {
    HashMap<Integer,Poll> polls;
    HashMap<Integer,User> users;
    HashMap<Integer,List<Vote>> votes;  
    
    Integer maxPollId = 1;
    Integer maxUserId = 1;  
            
    public PollManager() {
        this.polls = new HashMap<>();
        this.users = new HashMap<>(); 
        this.votes = new HashMap<>();  

    }

    public void addPoll(Poll poll){ 
        polls.put(maxPollId, poll);
        poll.getCreator().addPolls(poll);
        poll.setPollID(maxPollId);
        maxPollId ++;
        
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

    public void addUser(User user){
        this.users.put(maxUserId, user);
        user.setUserId(maxUserId);
        maxUserId ++;  
    }

    public List<Vote> getVotes(Integer pollID){
        if(votes.keySet().contains(pollID)){
            return votes.get(pollID);
        }
        throw new NoSuchElementException("No votes found for " + pollID);
        
    }

    public String addVote(Integer pollId, Vote vote) {
        boolean voted = false; 
        if(userVoteExists(pollId, vote.getUserId())){
            removeVote(pollId, vote.getUserId());
            voted = true; 
        }
        List<Vote> pollVotes = votes.get(pollId);
        if (pollVotes == null) {
            pollVotes = new ArrayList<>();
            votes.put(pollId, pollVotes);
        }
        pollVotes.add(vote);
        if(!voted){
            return "User with ID " + vote.getUserId().toString() + " has given a new vote"; 
        }
        else{
            return "User with ID " + vote.getUserId().toString() + " has changed vote to "+ vote.getOptionId().toString(); 
        }
    }
       
    

    public void removeVote(Integer pollID, Integer userId){
        getVotes(pollID).remove((Vote) getUserVote(pollID, userId));

    
    }

    public Vote getUserVote(Integer pollID, Integer userId){
        for(Vote vote : this.getVotes(pollID)){
            if(vote.getUserId().equals(userId)){
                return vote; 
            }

        }
       throw new NoSuchElementException("This pollID not found in votes");
    }

    public boolean userVoteExists(Integer pollID, Integer userId){
        try {
         getVotes(pollID);
        } 
        catch (NoSuchElementException e) {
        return false;
        }
            
        for(Vote vote : this.getVotes(pollID)){
            if(vote.getUserId().equals(userId)){
                return true; 
            }

        }
       return false; 
    }


}



