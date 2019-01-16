package fr.dhel.voting.model.system.ballot;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.candidate.Candidate;

public class RankedBallot implements Ballot {
    private final List<Candidate> candidates;

    public RankedBallot(final List<Candidate> candidates) {
        this.candidates = Objects.requireNonNull(candidates, "candidates should not be null");
        if (candidates.isEmpty())
            throw new IllegalArgumentException("candidates should not be empty");

    }

    @Override
    public List<Pair<Candidate, Double>> computeResults() {
        return unmodifiableList(candidates.stream().map(c -> Pair.of(c, 1.0)).collect(toList()));
    }
}
