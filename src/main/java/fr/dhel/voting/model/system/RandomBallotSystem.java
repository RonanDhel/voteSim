package fr.dhel.voting.model.system;

import java.util.List;
import java.util.Random;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

/**
 * Représente le système de <i>tirage au sort pondérée</i>.
 * <p>
 * Chaque votant vote pour une seule personne et l'un des bulletins est tiré au
 * sort. Ce bulletin détermine le gagnant.
 * <p>
 * En d'autres termes, un élécteur tiré au sort décide du résultat de
 * l'élection.
 * <p>
 * Ce système se comporte comme ceci :
 * <ul>
 * <li>si un candidat remporterait la majorité des voix, il doit gagner : non
 * </li>
 * <li>critère de participation (ajouter un bulletin où le candidat A est
 * préféré ne doit pas changer le résultat du candidat A vers le candidat B) :
 * N/A</li>
 * <li>gagnant de Condorcet : non</li>
 * <li>perdant de Condorcet : non</li>
 * </ul>
 * 
 * @author Ronan
 *
 */
public class RandomBallotSystem implements VotingSystem {

    private static final String FULL_NAME = "Random-ballot";

    private final Random random;

    public RandomBallotSystem(final Random random) {
        this.random = random;
    }

    // ===================================================================
    // METHODES
    // ===================================================================

    @Override
    public String shortName() {
        return "RB";
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
        return v -> v.visitPlurality(candidateSet);
    }

    @Override
    public ElectionResult countVotes(final List<Ballot> votes, final Set<Candidate> candidateSet) {
        var ballot = votes.get(random.nextInt(votes.size()));

        var ballotResults = ballot.computeResults();

        var electedCandidate = ballotResults.get(0).getKey();

        return new ElectionResult(electedCandidate);
    }
}
