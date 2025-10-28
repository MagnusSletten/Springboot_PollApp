package com.example.backend.Model.Poll;

import java.time.Instant;
import java.util.ArrayList;

import com.example.backend.Model.User.User;

import lombok.Data;
import lombok.NoArgsConstructor;

/*Simplified Poll class that can be turned into a full Poll objects. Improves API behaviour. */

@Data
@NoArgsConstructor
public class PollDTO {
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

    public static PollDTO fromPoll(Poll poll) {
        PollDTO request = new PollDTO();
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
