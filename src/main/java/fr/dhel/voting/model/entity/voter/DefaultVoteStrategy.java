package fr.dhel.voting.model.entity.voter;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.candidate.Candidate;

public class DefaultVoteStrategy implements VoteStrategy {

    @Override
    public Candidate getBestCandidate(final Voter voter, final Set<Candidate> candidateSet) {
        final Candidate chosenCandidate;

        chosenCandidate = voter.findClosestCandidate(candidateSet);

        return chosenCandidate;
    }

    @Override
    public Candidate getWorstCandidate(final Voter voter, final Set<Candidate> candidateSet) {
        final Candidate chosenCandidate;

        var candidatesByUtility = voter.sortCandidates(candidateSet);

        chosenCandidate = candidatesByUtility.get(candidatesByUtility.size() - 1);

        return chosenCandidate;
    }

    Pair<Candidate, Double> getCandidateWithScore(
            final double utility, final Candidate candidate, final int minRange,
            final int maxRange) {
        double v = Math.round(utility * (maxRange - minRange) + minRange);
        return Pair.of(candidate, v);
    }

    private List<Pair<Candidate, Double>> computeResultsNormalCase(
            final Voter voter, final List<Candidate> candidatesByUtility,
            final BigDecimal maximalUtility, final int minRange, final int maxRange) {
        return candidatesByUtility.stream()
                .map(c -> getCandidateWithScore(voter.utility(c)
                        .divide(maximalUtility, RoundingMode.HALF_EVEN).doubleValue(), c, minRange,
                        maxRange))
                .collect(toList());
    }

    /**
     * Un seul candidat en lice, on multiplie son utilité par la valeur maximale.
     * <p>
     * Cela signifie qu'un candidat dont l'utilité est de 0.5 aura la moyenne.
     * 
     * @param c candidat
     * @return
     */
    private List<Pair<Candidate, Double>> singleCandidateCollection(
            final Voter voter, final Candidate c, final int minRange, final int maxRange) {
        var utility = voter.utility(c).doubleValue();
        return Collections.singletonList(getCandidateWithScore(utility, c, minRange, maxRange));
    }

    @Override
    public List<Pair<Candidate, Double>> getListOfCandidateWithScore(
            final Voter voter, final Set<Candidate> candidateSet, final int minRange,
            final int maxRange) {
        var candidatesByUtility = voter.sortCandidates(candidateSet);

        var bestCandidate = candidatesByUtility.get(0);

        if (candidateSet.size() == 1) {
            return singleCandidateCollection(voter, bestCandidate, minRange, maxRange);
        }

        // il y a plusieurs candidats
        BigDecimal maximalUtility = voter.utility(bestCandidate);

        var worstCandidate = candidatesByUtility.get(candidatesByUtility.size() - 1);

        if (maximalUtility.compareTo(voter.utility(worstCandidate)) == 0) {
            // tous les candidats sont estimés à la même qualité, ce n'est pas
            // censé se produire
            // pour résoudre ce point, on les évalue tous à la note moyenne
            return candidateSet.stream().map(c -> getCandidateWithScore(0.5, c, minRange, maxRange))
                    .collect(toList());
        }

        return unmodifiableList(computeResultsNormalCase(voter, candidatesByUtility, maximalUtility,
                minRange, maxRange));
    }
}
