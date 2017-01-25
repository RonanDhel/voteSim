package fr.dhel.voting.model.env;

import fr.dhel.voting.model.entity.Candidate;

/**
 * Contient le résultat d'une éléction.
 * 
 * @author Ronan
 *
 */
public class ElectionResult {
	private final Candidate electedCandidate;
	
	public ElectionResult(final Candidate electedCandidate) {
		this.electedCandidate = electedCandidate;
	}
	
	//===================================================================
	// METHODES
	//===================================================================
	
	public Candidate getElectedCandidate() {
		return electedCandidate;
	}
	
	@Override
	public String toString() {
		return "ElectionResult [electedCandidate=" + electedCandidate + "]";
	}
}
