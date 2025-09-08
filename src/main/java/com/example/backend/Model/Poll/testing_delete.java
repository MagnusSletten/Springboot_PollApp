package com.example.backend.Model.Poll;

import com.example.backend.Model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class testing_delete  {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User("m", "mm");
        Poll poll = new Poll();
        poll.setTitle("hello");
        poll.setCreator(user);
        poll.setPollID(1);
        System.out.println(user.getPolls().get(0).getTitle());
        System.out.println(poll.getPollID());

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(user)); 
        
    }
    
    
}
