package fr.dhel.voting.model.system;

import java.util.List;
import java.util.Set;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

/**
 * Représente le <i>Jugement majoritaire</i>.
 * <p>
 * Ce système de vote à été inventé par des français entre 2005 et 2010 et
 * permet à chaque votant d'évaluer les candidats sur une échelle de valeur. Par
 * exemple, on pourrait imaginer un bulletin avec les valeurs suivantes :
 * <i>Excellent, Bon, Moyen, Mauvais</i>.
 * <p>
 * L'implémentation actuelle transforme les valeurs vers des valeurs chiffrées,
 * ce qui n'a aucun impact sur le résultat.
 * <p>
 * Il s'agit donc de fait d'une variante du vote par valeur mais le résultat
 * est calculée différemment.
 * <p>
 * Plusieurs candidats peuvent être évalué au même niveau. <br />
 * Pour décompter les votes, il faut prendre la médiane des résultats. <br />
 * Si un ensemble de candidats ont la même meilleure médiane, le score le plus
 * mauvais est retirée et la médiane est prise de nouveau pour définir le
 * candidat vainqueur. <br />
 * Par exemple, si les évaluations des candidats A, B et C sont:
 * <ul>
 * <li>A : {3, 3, 2, 2, 1}
 * </li>
 * <li>B : {4, 2, 2, 2, 2}
 * </li>
 * <li>C : {4, 2, 1, 1, 1}
 * </li>
 * </ul>
 * leurs médianes seront de A : 2, B : 2, C : 1. Nous supprimons donc le
 * candidat C. <br />
 * Les candidats A et B sont à égalité, on retire le dernier bulletin et on
 * obtient A : {3, 3, 2, 2} et B : {4, 2, 2, 2}, qui ont tout les 2 la même 
 * médiane... il faut donc réiterer l'opération pour obtenir A : {3, 3, 2}
 * et B : {4, 2, 2}. <br />
 * La médiane de A est alors de 3 et il s'agit du candidat vainqueur de
 * l'élection.
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
public class MajorityJudgmentSystem implements VotingSystem {
	
	private static final String FULL_NAME = "Majority-judgment";
	
	@Override
	public String shortName() {
		return "MJ";
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
		return null;
	}

	@Override
	public ElectionResult countVotes(
			final List<Ballot> votes, final Set<Candidate> candidateSet) {
		throw new IllegalStateException("not yet implemented");
	}

}
