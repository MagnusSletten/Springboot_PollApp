package com.example.backend.Model.Poll;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.backend.Model.User.User;
import com.example.backend.Model.Vote.Vote;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String question;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poll", orphanRemoval = true)
    private List<VoteOption> options = new ArrayList<>();
    private Instant publishedAt = Instant.now();
    private Instant validUntil = Instant.now().plus(15, ChronoUnit.MINUTES);
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poll", orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();
    @ManyToOne()
    @JsonIdentityReference(alwaysAsId = true)
    private User createdBy;

    public Poll(String question) {
        this.question = question;
    }

    public void setCreatedBy(User creator) {
        this.createdBy = creator;

    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
        vote.setPoll(this);
        vote.getVotesOn().setPoll(this);
    }

    public VoteOption addVoteOption(String option) {
        VoteOption voteOption = new VoteOption(option);
        voteOption.setPoll(this);

        voteOption.setCaption(option);
        voteOption.setPresentationOrder(this.options.size());
        this.options.add(voteOption);
        return voteOption;
    }

    public void changeVote(Vote vote) {
        for (int i = 0; i < votes.size(); i++) {
            if (votes.get(i).getVoter().getUsername() == vote.getVoter().getUsername()) {
                votes.set(i, vote);
                return;
            }
        }
        throw new IllegalArgumentException("Vote with id " + vote.getVoteId() + " not found");
    }

    public Map<Integer, Integer> countVotesByPresentationOrder() {
        Map<Integer, Integer> counts = new HashMap<>();

        for (Vote vote : votes) {
            counts.put(vote.getVotesOn().getPresentationOrder(),
                    counts.getOrDefault(vote.getVotesOn().getPresentationOrder(), 0) + 1);
        }

        return counts;
    }

    public Map<String, String> countVotesByPresentationOrderString() {
        Map<String, String> counts = new HashMap<>();

        for (Vote vote : votes) {
            counts.put(vote.getVotesOn().getPresentationOrder().toString(),
                    counts.getOrDefault(vote.getVotesOn().getPresentationOrder().toString(), "0") + 1);
        }

        return counts;
    }

    public void remoteVotes() {
        for (int i = votes.size() - 1; i >= 0; i--) {
            Vote vote = votes.get(i);
            vote.getVoter().remoteVote(vote);
            votes.remove(i);
        }
    }

    public PollDTO toPollRequest() {
        PollDTO request = new PollDTO();
        request.setTitle(this.getTitle());
        request.setQuestion(this.getQuestion());
        request.setPublishedAt(this.getPublishedAt());
        request.setValidUntil(this.getValidUntil());

        if (this.getOptions() != null) {
            request.getVoteOptions().addAll(this.getOptions());
        }

        if (this.getCreatedBy() != null) {
            request.setCreator(this.getCreatedBy().getUsername());
        }

        return request;
    }

}
