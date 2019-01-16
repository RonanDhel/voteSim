package fr.dhel.voting.model.system;

import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

public abstract class RankedVotingSystem implements VotingSystem {
    @Override
    public final boolean isPluralityType() {
        return false;
    }

    @Override
    public BallotBuilder createBallot(final Set<Candidate> candidateSet) {
        return v -> v.visitRankedBallot(candidateSet);
    }
}
