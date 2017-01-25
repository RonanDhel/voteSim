package fr.dhel.voting.model.system.ballot;

import static java.util.Collections.unmodifiableSet;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.val;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.entity.Voter;

public class AntiPluralityBallot implements Ballot {
	private final Voter voter;
	private final Set<Candidate> candidateSet;

	public AntiPluralityBallot(final Voter voter, final Set<Candidate> candidateSet) {
		this.voter = Objects.requireNonNull(voter, "voter should not be null");
		this.candidateSet = Objects.requireNonNull(candidateSet, "candidateSet should not be null");
		if (candidateSet.isEmpty())
			throw new IllegalArgumentException("candidateSet should not be empty");
		
	}

	//===================================================================
	// METHODES
	//===================================================================
	
	@Override
	public List<Pair<Candidate, Double>> computeResults() {
		val candidatesByUtility = voter.sortCandidates(candidateSet);
		
		val chosenCandidate = candidatesByUtility.get(candidatesByUtility.size() - 1);
		
		return Collections.singletonList(Pair.of(chosenCandidate, 1.0));
	}

	@Override
	public Set<Candidate> getCandidates() {
		return unmodifiableSet(candidateSet);
	}
}
