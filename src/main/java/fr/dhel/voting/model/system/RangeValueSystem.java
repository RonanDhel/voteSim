package fr.dhel.voting.model.system;

import java.util.List;
import java.util.Set;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;
import fr.dhel.voting.model.system.ballot.ValuedBallot;

/**
 * Représente le système de <i>vote par valeur</i>.
 * <p>
 * Chaque candidat est notée entre un score minimum et un score max. <br />
 * Lors du décompte, les scores sont sommées et la moyenne est calculée sachant
 * que sur un bulletin : <br />
 * 1. tout les candidats sont notés <br />
 * 2. le même score peut être utilisé pour plusieurs candidats <br />
 * Le candidat avec la plus haute moyenne emporte l'éléction.
 * <p>
 * Petit example :
 * <p>
 * Possibilité de mettre un score entre 0 et 10 pour trois candidats A, B et C
 * et 4 bulletins comme ceci : <br />
 * <ul>
 * <li>A = 10, B = 5, C = 0
 * </li>
 * <li>A = 2, B = 6, C = 6
 * </li>
 * <li>A = 9, B = 7, C = 0
 * </li>
 * <li>A = 0, B = 1, C = 10
 * </li>
 * </ul>
 * A remporte l'éléction avec une moyenne de 7.
 * <p>
 * Ce système se comporte comme ceci :
 * <ul>
 * <li>si un candidat remporterait la majorité des voix, il doit gagner : pas
 * nécessairement mais en général oui
 * </li>
 * <li>critère de participation (ajouter un bulletin où le candidat A est
 * préféré ne doit pas changer le résultat du candidat A vers le candidat B) :
 * non
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
public class RangeValueSystem implements VotingSystem {
	
	private static final String FULL_NAME = "Range-vote";
	
	private final int minRange;
	private final int maxRange;

	public RangeValueSystem(
			final int minRange, final int maxRange) {
		if (maxRange <= minRange) {
			throw new IllegalArgumentException("maxRange should be strictly superior to minRange");
		}
		this.minRange = minRange;
		this.maxRange = maxRange;
	}
	
	//===================================================================
	// METHODES
	//===================================================================

	@Override
	public String shortName() {
		return "RV";
	}

	@Override
	public String fullName() {
		return FULL_NAME;
	}

	@Override
	public boolean isPluralityType() {
		return false;
	}

	@Override
	public BallotBuilder createBallot(
			final Set<Candidate> candidateSet) {
		return v -> new ValuedBallot(v, candidateSet, minRange, maxRange);
	}

	@Override
	public ElectionResult countVotes(
			final List<Ballot> votes, final Set<Candidate> candidateSet) {
		throw new IllegalStateException("not yet implemented");
	}
}