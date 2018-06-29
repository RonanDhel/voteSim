package fr.dhel.voting.model.env;

import java.util.NoSuchElementException;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;

/**
 * Contient le résultat d'une éléction.
 * 
 * @author Ronan
 *
 */
public class ElectionResult {
	private final Set<Candidate> newCandidates;
	
	private final Candidate electedCandidate;
	
	private ElectionResult(final Set<Candidate> newCandidates) {
		this.electedCandidate = null;
		this.newCandidates = newCandidates;
	}
	
	public ElectionResult(final Candidate electedCandidate) {
		this.electedCandidate = electedCandidate;
		this.newCandidates = null;
	}
	
	//===================================================================
	// METHODES
	//===================================================================
	
	public static ElectionResult electionWithNewRound(final Set<Candidate> candidatesForNextRound) {
		return new ElectionResult(candidatesForNextRound);
	}
	
	public boolean needNextRound() {
		return newCandidates != null;
	}
	
	public Set<Candidate> candidatesForNextRound() {
		if (!needNextRound()) {
			throw new NoSuchElementException();
		}
		return newCandidates;
	}
	
	public Candidate getElectedCandidate() {
		return electedCandidate;
	}
	
	@Override
	public String toString() {
		return "ElectionResult [electedCandidate=" + electedCandidate + "]";
	}
}
