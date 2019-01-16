package fr.dhel.voting.model.env;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.entity.voter.Voter;
import fr.dhel.voting.model.system.VotingSystem;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

public class VoteEnvironment {

    private final VotingSystem system;

    private final Set<Candidate> candidateSet;

    private final List<Voter> voters;

    VoteEnvironment(
            final VotingSystem system, final Set<Candidate> candidateSet,
            final List<Voter> voters) {
        if (candidateSet.isEmpty()) {
            throw new IllegalArgumentException("candidateSet should not be empty");
        }
        this.system = Objects.requireNonNull(system, "system should not be null");
        this.candidateSet = candidateSet;
        this.voters = Objects.requireNonNull(voters, "voters should not be null");
    }

    public static class Builder {
        private VotingSystem system;
        private Set<Candidate> candidateSet;
        private List<Voter> voters;

        private Builder() {
            candidateSet = new HashSet<>();
        }

        // ===================================================================
        // METHODES
        // ===================================================================

        public Builder votingSystem(final VotingSystem votingSystem) {
            this.system = votingSystem;
            return this;
        }

        public Builder candidate(final Candidate candidate) {
            this.candidateSet.add(candidate);
            return this;
        }

        public Builder candidateSet(final Set<Candidate> candidateSet) {
            this.candidateSet = candidateSet;
            return this;
        }

        public Builder voters(final List<Voter> voters) {
            this.voters = voters;
            return this;
        }

        public VoteEnvironment build() {
            return new VoteEnvironment(system, candidateSet, voters);
        }
    }

    // ===================================================================
    // METHODES
    // ===================================================================

    public static Builder builder() {
        return new Builder();
    }

    void setup() {
        // rien à faire
    }

    private Set<Candidate> getCandidateSet() {
        return unmodifiableSet(candidateSet);
    }

    ElectionResult countVotes(final BallotBuilder builder) {
        List<Ballot> res = voters.stream().map(v -> v.vote(builder)).collect(toList());

        return system.countVotes(res, getCandidateSet());
    }

    public Candidate countVotesAndComputeElectionResult() {
        var b = system.createBallot(getCandidateSet());

        ElectionResult er = countVotes(b);

        while (er.needNextRound()) {
            er = countVotes(system.createBallot(er.candidatesForNextRound()));
        }
        return er.getElectedCandidate();
    }

    public final Candidate electWinner() {
        setup();
        return countVotesAndComputeElectionResult();
    }

    @Override
    public String toString() {
        return "Vote environment using " + system.getClass().getName() + " for candidates ("
                + candidateSet + ") with " + voters.size() + " voters";
    }
}
