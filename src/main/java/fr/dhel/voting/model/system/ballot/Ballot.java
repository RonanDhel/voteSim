package fr.dhel.voting.model.system.ballot;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.candidate.Candidate;

public interface Ballot {
	List<Pair<Candidate, Double>> computeResults();
}
