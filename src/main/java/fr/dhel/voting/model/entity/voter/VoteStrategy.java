package fr.dhel.voting.model.entity.voter;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.candidate.Candidate;

public interface VoteStrategy {
	Candidate getBestCandidate(final Voter voter, final Set<Candidate> candidateSet);

	Candidate getWorstCandidate(final Voter voter, final Set<Candidate> candidateSet);

	List<Pair<Candidate, Double>> getListOfCandidateWithScore(
			final Voter voter, final Set<Candidate> candidateSet, final int minRange,
			final int maxRange);
}
