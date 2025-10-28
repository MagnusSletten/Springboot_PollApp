package com.example.backend.Managers;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.backend.Cache.LoginStatusCache;
import com.example.backend.Cache.VoteCache;
import com.example.backend.MessageBrokers.Listener;
import com.example.backend.MessageBrokers.PollBroker;
import com.example.backend.Model.Poll.Poll;
import com.example.backend.Model.Poll.PollDTO;
import com.example.backend.Model.User.User;
import com.example.backend.Model.User.UserDTO;
import com.example.backend.Model.Vote.Vote;
import com.example.backend.Model.Vote.VoteDTO;
import com.example.backend.Repositories.PollRepository;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.Repositories.VoteRepository;
import com.example.backend.Security.SecurityConfig;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PollManager implements Listener {
    private final PollRepository pollRepo;
    private final VoteRepository voteRepo;
    private final UserRepository userRepo;
    private final VoteCache voteCache;
    private final PollBroker broker;
    private final LoginStatusCache loginCache;
    private final SecurityConfig securityConfig;

    @Transactional
    public Poll addPollFromRequest(PollDTO pollRequest) throws NoSuchElementException {
        Optional<User> userOpt = userRepo.findByUsername(pollRequest.getCreator());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Poll poll = pollRequest.toPoll(user);
            user.addPolls(poll);
            pollRepo.save(poll);
            return poll;
        }
        throw (new NoSuchElementException("creator not found"));

    }

    @Transactional
    public Optional<Poll> getPoll(Integer poll_id) {
        return pollRepo.findById(poll_id);
    }

    @Transactional
    public Optional<User> getUser(String userName) {
        return userRepo.findByUsername(userName);

    }

    @Transactional
    public Collection<User> getUsers() {
        return userRepo.findAll();
    }

    @Transactional
    public boolean deletePoll(Integer pollId) {
        if (pollRepo.existsById(pollId)) {
            pollRepo.deleteById(pollId);
        }

        return false;

    }

    public List<Poll> getPollsByUserName(String username) {
        return pollRepo.findByCreatedByUsername(username);

    }

    public List<Poll> getPolls() {
        return pollRepo.findAll();

    }

    @Transactional
    public User addUserFromRequest(UserDTO userRequest) throws Exception {
        User user = userRequest.toUser();
        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        userRepo.save(user);
        return user;
    }

    public Boolean matchUserPasswords(UserDTO userRequest) {
        try {
            return securityConfig.passwordEncoder().matches(userRequest.getPassword(),
                    getUser(userRequest.getUsername()).get().getPassword());
        } catch (Exception e) {
            System.out.println(e);
            return false;

        }
    }

    public List<Vote> getVotes(Integer pollID) throws NoSuchElementException {
        if (pollRepo.existsById(pollID)) {
            return voteRepo.findByPollId(pollID);
        } else {
            throw (new NoSuchElementException("No poll found with this ID"));
        }
    }

    @Transactional
    public Map<Integer, Integer> getVoteResults(Integer pollID) {
        Poll poll = getPoll(pollID).get();
        if (voteCache.isCached(poll)) {
            System.out.println("Poll is already cached. returning from cache");
            return voteCache.getVoteResults(poll);
        }

        else {
            voteCache.setVotes(poll);
            System.out.println("Poll not cached, querying DB and caching result");
            return pollRepo.findById(poll.getId()).get().countVotesByPresentationOrder();
        }

    }

    @Transactional()
    public Optional<Integer> getLatestPollId() {
        return pollRepo.findTopByOrderByIdDesc().map(Poll::getId);
    }

    @Transactional
    public Vote addVoteWithUser(VoteDTO request) {
        User user = userRepo.findByUsername(request.getUserName()).get();
        Integer id = request.getPollId();

        Poll poll = getPoll(id).get();
        Instant now = Instant.now();
        if (now.isBefore(poll.getPublishedAt()) || now.isAfter(poll.getValidUntil())) {
            throw new IllegalStateException("Voting is closed for this poll");
        }
        if (voteCache.isCached(poll)) {
            voteCache.removeVotes(poll);
        }
        Vote vote = request.toVote(user, poll);
        if (!voteRepo.existsByPollIdAndVoterId(id, user.getId())) {
            poll.addVote(vote);
            user.addVote(vote);
            voteRepo.save(vote);
        } else {
            Vote vote2 = voteRepo.findByPollIdAndVoterId(id, user.getId());
            vote2.setVotesOn(vote.getVotesOn());
            vote = vote2;
            poll.changeVote(vote2);
            user.addVote(vote2);

        }
        try {
            broker.sendVote(request.getPollId(), request);
        } catch (Exception e) {
            System.out.println(e);
        }
        return vote;
    }

    @Transactional
    public Vote addVoteAnonymous(VoteDTO request) {
        Poll poll = getPoll(request.getPollId()).get();
        if (voteCache.isCached(poll)) {
            voteCache.removeVotes(poll);
        }
        Vote vote = request.toVoteAnonymous(poll);
        poll.addVote(vote);
        return vote;
    }

    @Transactional
    public Vote addVote(VoteDTO request) {
        if (request.hasUsername()) {
            addVoteWithUser(request);
        } else {
            addVoteAnonymous(request);
        }
        if (request.hasUsername()) {
            return request.toVote(getUser(request.getUserName()).get(), getPoll(request.getPollId()).get());
        } else
            return request.toVoteAnonymous(getPoll(request.getPollId()).get());
    }

    @Transactional
    public void removeVote(Integer pollID, Integer userId) {
        getVotes(pollID).remove((Vote) getUserVote(pollID, userId));

    }

    @Transactional
    public Vote getUserVote(Integer pollID, Integer userId) {
        for (Vote vote : this.getVotes(pollID)) {
            if (vote.getVoter().getId().equals(userId)) {
                return vote;
            }

        }
        throw new NoSuchElementException("This pollID not found in votes");
    }

    @Override
    public void onEvent(String message) {
        System.out.println(message);
        VoteDTO voteRequest = VoteDTO.fromJson(message);
        try {
            if (voteRequest.hasUsername()) {
                addVoteWithUser(voteRequest);
            } else {
                addVoteAnonymous(voteRequest);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void login(User user) {

        loginCache.logIn(user);
    }

    public void logout(User user) {
        loginCache.logOut(user);
    }

}
