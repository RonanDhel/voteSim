package fr.dhel.voting.model.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;
import fr.dhel.voting.model.system.ballot.UninominalBallot;

/**
 * Représente le <i>scrutin uninominal à 1 tour</i>.
 * <p>
 * Ce système est très simple et est utilisée pour beaucoup d'éléction,
 * notamment les éléctions américaines, mexicaine, de plusieurs pays
 * asiatiques...
 * <p>
 * Il faut voter pour une et une seule personne, la personne ayant le plus
 * de vote étant élue.
 * <p>
 * Si 40% des candidats vote pour le candidat A et que les autres candidats
 * ont tous moins de 40%, le candidat A sera élu. <br />
 * A noter que ce système est très fragile face à 2 candidats ayant
 * des idées proches : s'il y a 3 candidats et que 2 candidats A et B sont très
 * proches sur l'échiquier politique, voici ce qu'il peut se produire : <br />
 * A a 35% des voix, B a 27% des voix, C a 38% des voix, <br />
 * C est élu alors que la majorité des gens sont en accord avec A et B.
 * <p>
 * Ce système se comporte comme ceci :
 * <ul>
 * <li>si un candidat remporterait la majorité des voix, il doit gagner : oui
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
public class PluralitySystem implements VotingSystem {

	private static final String FULL_NAME = "Plurality-1-turn";

	//===================================================================
	// METHODES
	//===================================================================
	
	@Override
	public String shortName() {
		return "PL1";
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
	public BallotBuilder createBallot(
			final Set<Candidate> candidateSet) {
		return v -> new UninominalBallot(v, candidateSet);
	}

	@Override
	public ElectionResult countVotes(
			final List<Ballot> votes, final Set<Candidate> candidateSet) {
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
						firstEntry, secondEntry) -> Double.compare(secondEntry.getValue(),
						firstEntry.getValue())).findFirst().get().getKey();

		return new ElectionResult(result);
	}

}
