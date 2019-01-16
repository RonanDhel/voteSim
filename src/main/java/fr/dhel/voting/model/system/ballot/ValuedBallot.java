package fr.dhel.voting.model.system.ballot;

import static java.util.Collections.unmodifiableList;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.candidate.Candidate;

public class ValuedBallot implements Ballot {
    private final List<Pair<Candidate, Double>> result;

    // ===================================================================
    // METHODES
    // ===================================================================

    public ValuedBallot(final List<Pair<Candidate, Double>> listOfCandidateWithScore) {
        this.result = Objects.requireNonNull(listOfCandidateWithScore,
                "listOfCandidateWithScore should not be null");
    }

    @Override
    public final List<Pair<Candidate, Double>> computeResults() {
        return unmodifiableList(result);
    }
}
