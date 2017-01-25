package fr.dhel.voting.model.system.ballot;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.Candidate;

public interface Ballot {
	List<Pair<Candidate, Double>> computeResults();
	
	Set<Candidate> getCandidates();
}
