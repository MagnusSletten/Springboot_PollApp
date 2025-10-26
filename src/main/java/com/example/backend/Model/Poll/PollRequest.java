package com.example.backend.Model.Poll;

import java.time.Instant;
import java.util.ArrayList;

import com.example.backend.Model.User.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PollRequest {
    private String title;
    private String question;
    private ArrayList<VoteOption> voteOptions = new ArrayList<>();
    private Instant publishedAt = Instant.now();
    private Instant validUntil;
    private String creator;

    public Poll toPoll(User creator) {
        Poll poll = new Poll();
        poll.setCreatedBy(creator);
        poll.setQuestion(question);
        for (int i = 0; i < voteOptions.size(); i++) {
            VoteOption vo = voteOptions.get(i);
            vo.setPoll(poll);
            vo.setPresentationOrder(i + 1);
            poll.getOptions().add(vo);
        }

        poll.setTitle(title);
        if (validUntil != null) {
            poll.setValidUntil(validUntil);
        }
        poll.setPublishedAt(publishedAt);
        return poll;
    }

    public static PollRequest fromPoll(Poll poll) {
        PollRequest request = new PollRequest();
        request.setTitle(poll.getTitle());
        request.setQuestion(poll.getQuestion());
        request.setPublishedAt(poll.getPublishedAt());
        request.setValidUntil(poll.getValidUntil());

        if (poll.getOptions() != null) {
            request.getVoteOptions().addAll(poll.getOptions());
        }

        if (poll.getCreatedBy() != null) {
            request.setCreator(poll.getCreatedBy().getUsername());
        }

        return request;
    }

}
