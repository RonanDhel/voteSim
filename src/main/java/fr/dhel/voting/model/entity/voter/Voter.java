package fr.dhel.voting.model.entity.voter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;
import fr.dhel.voting.model.system.VotingSystem;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;
import fr.dhel.voting.model.system.ballot.RankedBallot;
import fr.dhel.voting.model.system.ballot.UninominalBallot;
import fr.dhel.voting.model.system.ballot.ValuedBallot;

public interface Voter {
    public interface BallotVisitor {
        UninominalBallot visitPlurality(final Set<Candidate> candidateSet);

        UninominalBallot visitAntiPlurality(final Set<Candidate> candidateSet);

        RankedBallot visitRankedBallot(final Set<Candidate> candidateSet);

        ValuedBallot visitRangeValue(
                final Set<Candidate> candidateSet, final int minRange, final int maxRange);

        ValuedBallot visitMajorityJudgment(final Set<Candidate> candidateSet);

    }

    /**
     * Indique si le voteur est ignorant, un voteur ignorant ne votera pas
     * nécessairement de manière stratégique.
     * 
     * @return true si le voteur est ignorant
     */
    boolean isIgnorant();

    /**
     * @return la position politique du voteur
     */
    PoliticalPosition getPoliticalPosition();

    /**
     * Indique l'utilité perçue par le voteur sur le {@link Candidate} fourni.
     * 
     * Une valeur de 0 indiquant que l'utilité du candidat est nulle et une utilité
     * de 1 étant maximale.
     * <p>
     * L'utilité perçue est sensiblement différent de l'utilité réelle car elle peut
     * prendre en compte des facteurs altérant l'utilité perçue notamment
     * l'ignorance de l'actuel votant ({@link #isIgnorant()}).
     * 
     * 
     * @param c le candidat à tester
     * @return l'utilité du candidat
     */
    BigDecimal utility(final Candidate c);

    /**
     * Indique l'utilité réelle par le voteur sur le {@link Candidate} fourni.
     * 
     * Une valeur de 0 indiquant que l'utilité du candidat est nulle et une utilité
     * de 1 étant maximale.
     * 
     * 
     * @param c le candidat à tester
     * @return l'utilité du candidat
     * @see #utility(Candidate)
     */
    BigDecimal trueUtility(final Candidate c);

    /**
     * Retourne une liste triée des candidats en fonction de l'utilité calculée via
     * {@link #utility(Candidate)}.
     * 
     * L'ordre est décroissant, donc le candidat en première position est celui qui
     * a la plus haute utilité.
     * 
     * @param candidateSet ensemble des candidats à trier
     * @return liste des candidats triée
     */
    List<Candidate> sortCandidates(final Set<Candidate> candidateSet);

    /**
     * Détermine quel est le candidat le plus proche en terme d'utilité parmis un
     * ensemble de candidat possible.
     * 
     * @param candidateSet ensemble des candidats
     * @return le candidat avec la plus forte utilité
     */
    Candidate findClosestCandidate(final Set<Candidate> candidateSet);

    /**
     * Renseigne un vote et produit un bulletin de vote valide pour être décompter
     * via {@link VotingSystem#countVotes(List, Set)}.
     * 
     * @param builder ballot builder
     * @return un bulletin de vote valide
     */
    Ballot vote(final BallotBuilder builder);
}
