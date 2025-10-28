package com.example.backend.MessageBrokers;

import com.example.backend.Model.Vote.VoteDTO;

public interface PollMessageBroker {

    public void sendVote(Integer pollId, VoteDTO vote) throws Exception;

    public void recieve(Listener listener);

}
