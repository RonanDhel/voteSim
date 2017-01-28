package fr.dhel.voting.model.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

/**
 * * Représente le <i>système de vote par élimination</i>.
 * <p>
 * Ce système permet de voter contre un candidat. Le gagnant de l'élection est
 * le candidat qui a reçu le moins de vote.
 * <p>
 * Il est possible d'approuver tous les candidats ou aucun.
 * <p>
 * Il s'agit d'un système très mauvais pouvant servir de point de comparaison
 * avec les autres systèmes. <br />
 * Un candidat bon qui attirerait trop l'attention sur lui pourrait se faire
 * éliminer facilement.
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
 * <li>perdant de Condorcet : non
 * </li>
 * </ul>
 * 
 * @author Ronan
 *
 */
public class AntiPluralitySystem implements VotingSystem {

	private static final String FULL_NAME = "Anti-plurality";
	
	@Override
	public String shortName() {
		return "APL";
	}

	@Override
	public String fullName() {
		return FULL_NAME;
	}

	@Override
	public boolean isPluralityType() {
		return true;
	}

	@Override
	public BallotBuilder createBallot(final Set<Candidate> candidateSet) {
		return v -> v.visitAntiPlurality(candidateSet);
	}

	@Override
	public ElectionResult countVotes(final List<Ballot> votes, final Set<Candidate> candidateSet) {
		Map<Candidate, Double> scorePerCandidate = new HashMap<>();

		for (Ballot vote : votes) {
			Candidate c = vote.computeResults().get(0).getKey();
			final double value = vote.computeResults().get(0).getValue();

			scorePerCandidate.merge(c, value, (
					oldValue, newValue) -> oldValue + newValue);
		}

		Candidate result = scorePerCandidate
				.entrySet()
				.stream()
				.sorted((
						firstEntry, secondEntry) -> Double.compare(firstEntry.getValue(),
						secondEntry.getValue())).findFirst().get().getKey();

		return new ElectionResult(result);
	}

}
