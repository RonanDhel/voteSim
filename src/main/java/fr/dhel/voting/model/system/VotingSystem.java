package fr.dhel.voting.model.system;

import java.util.List;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

public interface VotingSystem {
    /**
     * Représente ce <code>VotingSystem</code> via un nom court de 2 à 5 caractères.
     * 
     * @return un nom court
     */
    String shortName();

    /**
     * @return nom du système
     */
    String fullName();

    /**
     * @return true si ce système est de type uninominal, false sinon
     */
    boolean isPluralityType();

    /**
     * Génère un {@link BallotBuilder} qui permettra de produire des bulletins de
     * vote pour les candidats indiqués.
     * 
     * @param candidateSet ensemble des candidats disponible
     * @return
     */
    BallotBuilder createBallot(final Set<Candidate> candidateSet);

    /**
     * Compte l'ensemble des votes et détermine le gagnant de l'élection.
     * 
     * Si des votes font référence à des candidats non fournies, le comportement
     * n'est pas déterminé.
     * 
     * @param votes        liste des bulletins
     * @param candidateSet ensemble des candidats
     * @return le gagnant de l'élection
     */
    ElectionResult countVotes(final List<Ballot> votes, final Set<Candidate> candidateSet);
}
