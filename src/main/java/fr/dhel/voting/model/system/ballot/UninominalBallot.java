package fr.dhel.voting.model.system.ballot;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.candidate.Candidate;

public class UninominalBallot implements Ballot {
    private final Candidate candidate;

    public UninominalBallot(final Candidate candidate) {
        this.candidate = Objects.requireNonNull(candidate, "candidate should not be null");
    }

    // ===================================================================
    // METHODES
    // ===================================================================

    @Override
    public final List<Pair<Candidate, Double>> computeResults() {
        return Collections.singletonList(Pair.of(candidate, 1.0));
    }
}
