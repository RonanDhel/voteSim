package fr.dhel.voting.model.system.ballot;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.candidate.Candidate;

public class RankedBallot implements Ballot {
	private final Set<Candidate> candidateSet;
	
	public RankedBallot(final Set<Candidate> candidateSet) {
		this.candidateSet = Objects.requireNonNull(candidateSet, "candidateSet should not be null");
		if (candidateSet.isEmpty())
			throw new IllegalArgumentException("candidateSet should not be empty");
		
	}

	@Override
	public List<Pair<Candidate, Double>> computeResults() {
		// TODO Auto-generated method stub
		return null;
	}
}
