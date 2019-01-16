package fr.dhel.voting.model.system;

import java.util.List;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

/**
 * Représente le <i>système de vote par approbation</i>.
 * <p>
 * Ce système permet de voter positivement pour plusieurs candidats. Le gagnant
 * de l'élection est le candidat le plus "approuvé".
 * <p>
 * Il est possible d'approuver tous les candidats ou aucun.
 * <p>
 * Ce système a été décrit pour la première fois en 1977, il peut être imaginée
 * comme un cas particulier de {@link RangeValueSystem} et de
 * {@link MajorityJudgmentSystem} où il n'y aurait qu'une seule valeur.
 * <p>
 * Ce système se comporte comme ceci :
 * <ul>
 * <li>si un candidat remporterait la majorité des voix, il doit gagner : pas
 * nécessairement mais en général oui</li>
 * <li>critère de participation (ajouter un bulletin où le candidat A est
 * préféré ne doit pas changer le résultat du candidat A vers le candidat B) :
 * non</li>
 * <li>gagnant de Condorcet : non</li>
 * <li>perdant de Condorcet : non</li>
 * </ul>
 * 
 * @author Ronan
 *
 */
public class ApprovalSystem implements VotingSystem {

    private static final String FULL_NAME = "Approval-vote";

    private final RangeValueSystem rangeS = new RangeValueSystem(0, 1);

    // ===================================================================
    // METHODES
    // ===================================================================

    @Override
    public String shortName() {
        return "AS";
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
    public BallotBuilder createBallot(final Set<Candidate> candidateSet) {
        return rangeS.createBallot(candidateSet);
    }

    @Override
    public ElectionResult countVotes(final List<Ballot> votes, final Set<Candidate> candidateSet) {
        return rangeS.countVotes(votes, candidateSet);
    }

}
