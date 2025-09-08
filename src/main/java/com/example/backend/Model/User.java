package com.example.backend.Model;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.Poll.Vote;
import com.example.backend.Model.Poll.VoteOption;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userName" )

public class User{
    private String userName;
    private String email;
    private Integer userId;
    private List<Poll> polls;

    public User(String userName, String email){
        this.userName = userName;
        this.email = email; 
        this.polls = new ArrayList<>(); 
    }

     public Vote vote(Poll poll, VoteOption option){
        

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
}
