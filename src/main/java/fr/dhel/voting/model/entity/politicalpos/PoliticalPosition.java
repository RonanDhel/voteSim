package fr.dhel.voting.model.entity.politicalpos;

import java.math.BigDecimal;

import fr.dhel.voting.model.entity.candidate.Candidate;

/**
 * Représente un placement politique et d'estimer à quel point un candidat est
 * proche de celui-ci.
 * 
 * @author Ronan
 *
 */
public interface PoliticalPosition {
	/**
	 * Détermine une valeur entre 0 et 1 entre cette
	 * <code>PoliticalPosition</code> et le {@link Candidate} fourni.
	 * 
	 * Une valeur de 0 indiquant que l'utilité du candidat est nulle et une 
	 * utilité de 1 étant maximale.
	 * 
	 * @param candidate le candidat à tester
	 * @return
	 */
	public BigDecimal utility(final Candidate candidate);
}