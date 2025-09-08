package com.example.backend.Model;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.Poll.Vote.Vote;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "userName"
)
public class User{
    private String userName;
    private String email;
    private Integer userId;
    @JsonIdentityReference(alwaysAsId = true)
    private List<Poll> polls = new ArrayList<>();
    @JsonIdentityReference(alwaysAsId = true)
    private List<Vote> votes = new ArrayList<>(); 

    public User(String userName, String email){
        this.userName = userName;
        this.email = email; 
        this.polls = new ArrayList<>(); 
        this.votes = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return userName.equals(user.userName);  
    }

    @Override
    public int hashCode() {
        return userName.hashCode();  
    }

    public void addPolls(Poll poll){
        polls.add(poll);

    }

    public boolean hasVoted(Integer pollId){
        boolean hasVoted = true; 
        for(Vote vote : votes){
            if(vote.getVoter().equals(this)){
                hasVoted = true; 
            }
            ;

        }
        return hasVoted; 
        
    }
}
