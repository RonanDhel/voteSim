package fr.dhel.voting.model.system;

import java.util.List;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;

/**
 * Représente la <i>méthode Borda</i>.
 * <p>
 * Ce système permet de trier les candidats du pire au meilleur et leur accorde
 * un nombre de points en fonction de leur position.
 * <p>
 * S'il y a <i>n</i> candidats, le candidat placé en tête gagnera <i>n</i>
 * points, le second <i>n - 1</i> et le dernier <i>1</i> point. <br />
 * Par exemple, avec 4 candidats A, B, C et D et un bulletin de vote A &gt; B
 * &gt; D &gt; C, A gagne 4 points, B gagne 3 points, D 2 point et C 1 points.
 * <p>
 * Ce système se comporte comme ceci :
 * <ul>
 * <li>si un candidat remporterait la majorité des voix, il doit gagner : non
 * </li>
 * <li>critère de participation (ajouter un bulletin où le candidat A est
 * préféré ne doit pas changer le résultat du candidat A vers le candidat B) :
 * oui
 * </li>
 * <li>gagnant de Condorcet : non
 * </li>
 * <li>perdant de Condorcet : oui
 * </li>
 * </ul>
 * 
 * @author Ronan
 *
 */
public class BordaCount extends RankedVotingSystem {
	
	private static final String FULL_NAME = "Borda-count";
	
	@Override
	public String shortName() {
		return "BC";
	}

	@Override
	public String fullName() {
		return FULL_NAME;
	}

	@Override
	public ElectionResult countVotes(
			final List<Ballot> votes, final Set<Candidate> candidateSet) {
		throw new IllegalStateException("not implemented yet");
	}

}
