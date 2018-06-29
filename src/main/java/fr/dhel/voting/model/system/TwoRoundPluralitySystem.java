package fr.dhel.voting.model.system;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;
import lombok.val;

/**
 * Représente le <i>scrutin uninominal à 2 tour</i>.
 * <p>
 * Ce système est typiquement utilisé par les éléctions présidentielles
 * française sous la Ve république.
 * <p>
 * Il faut voter pour une personne. Celle-ci est élue si elle a 50% des voix ou
 * +, sinon, un second tour est organisé avec les deux meilleurs candidats. <br />
 * Le second tour est gagné par le candidat ayant le plus de voix.
 * <p>
 * Ce système se comporte comme ceci :
 * <ul>
 * <li>si un candidat remporterait la majorité des voix, il doit gagner : oui
 * </li>
 * <li>critère de participation (ajouter un bulletin où le candidat A est
 * préféré ne doit pas changer le résultat du candidat A vers le candidat B) :
 * oui</li>
 * <li>gagnant de Condorcet : non</li>
 * <li>perdant de Condorcet : oui</li>
 * </ul>
 * 
 * @author Ronan
 *
 */
public class TwoRoundPluralitySystem implements VotingSystem {

	private static final String FULL_NAME = "Plurality-2-turn";

	//===================================================================
	// METHODES
	//===================================================================
	
	@Override
	public String shortName() {
		return "PL2";
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
		return v -> v.visitPlurality(candidateSet);
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

		val sortedResult = scorePerCandidate
				.entrySet()
				.stream()
				.sorted((
						firstEntry, secondEntry) -> Double.compare(secondEntry.getValue(),
						firstEntry.getValue())).collect(toList());

		val bestCandidateWithScore = sortedResult.get(0);
		
		int numberOfVoteToBeElected = (votes.size() + 1) / 2;
		if (bestCandidateWithScore.getValue() >= numberOfVoteToBeElected) {
			return new ElectionResult(bestCandidateWithScore.getKey());
		}
		
		val bestTwo = new HashSet<Candidate>();
		bestTwo.add(sortedResult.get(0).getKey());
		bestTwo.add(sortedResult.get(1).getKey());
		
		return ElectionResult.electionWithNewRound(bestTwo);
	}

}
