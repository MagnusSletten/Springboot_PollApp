package com.example.backend.Controllers;

import java.util.HashMap;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.Poll.PollRequest;


@RestController
@CrossOrigin
@RequestMapping("/polls")
public class PollController {
    	HashMap<Integer,Poll> polls;
        PollManager manager;
         

        public PollController(PollManager manager) {
            this.polls = new HashMap<>();
            this.manager = manager;  
        }

        @PostMapping
        public Poll addPoll(@RequestBody PollRequest pollRequest){ 
            Poll poll = manager.addPollFromRequest(pollRequest);
            return poll; 
        }

        @GetMapping("/{pollID}")
        public Poll getPoll(@PathVariable("pollID") Integer pollID) throws Exception{
            try {
            return manager.getPoll(pollID);
            }
            catch(Exception e){
                throw e; 
            }
 
        }

}