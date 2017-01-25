package fr.dhel.voting.model.system.ballot;

import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

import lombok.val;

import org.apache.commons.lang3.tuple.Pair;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.entity.Voter;

public class ValuedBallot implements Ballot {
	private final Voter voter;
	private final Set<Candidate> candidateSet;
	private final int minRange;
	private final int maxRange;

	public ValuedBallot(
			final Voter voter, final Set<Candidate> candidateSet, final int minRange,
			final int maxRange) {
		this.voter = Objects.requireNonNull(voter, "voter should not be null");
		this.candidateSet = Objects.requireNonNull(candidateSet, "candidateSet should not be null");
		if (candidateSet.isEmpty())
			throw new IllegalArgumentException("candidateSet should not be empty");
		
		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	// ===================================================================
	// METHODES
	// ===================================================================

	public static BiFunction<Voter, Set<Candidate>, ValuedBallot> valuedBallotWithRange(
			final int minRange, final int maxRange) {
		return (voter, candidateSet) -> new ValuedBallot(voter, candidateSet, minRange, maxRange);
	}

	/**
	 * Associe à un candidat un score en fonction du minRange et du maxRange.
	 * 
	 * Le score est prévue pour être de minRange si le paramètre
	 * <code>utility</code> est de 0 et de maxRange si le paramètre
	 * <code>utility</code> est de 1.
	 * 
	 * @param c
	 * @return
	 */
	Pair<Candidate, Double> getCandidateWithScore(
			final double utility, final Candidate candidate) {
		double v = Math.round(utility * (maxRange - minRange) + minRange);
		return Pair.of(candidate, v);
	}

	private List<Pair<Candidate, Double>> computeResultsNormalCase(
			final List<Candidate> candidatesSortedByUtility, final BigDecimal maximalUtility) {
		return candidateSet
				.stream()
				.map(c -> getCandidateWithScore(voter.utility(c).divide(maximalUtility)
						.doubleValue(), c)).collect(toList());
	}

	/**
	 * Un seul candidat en lice, on multiplie son utilité par la valeur
	 * maximale.
	 * <p>
	 * Cela signifie qu'un candidat dont l'utilité est de 0.5 aura la
	 * moyenne.
	 * 
	 * @param c candidat
	 * @return
	 */
	private List<Pair<Candidate, Double>> singleCandidateCollection(
			final Candidate c) {
		val utility = voter.utility(c).doubleValue();
		return Collections.singletonList(getCandidateWithScore(utility, c));
	}

	@Override
	public final List<Pair<Candidate, Double>> computeResults() {
		val candidatesByUtility = voter.sortCandidates(candidateSet);

		val bestCandidate = candidatesByUtility.get(0);

		if (candidateSet.size() == 1) {
			return singleCandidateCollection(bestCandidate);
		}

		// il y a plusieurs candidats
		BigDecimal maximalUtility = voter.utility(bestCandidate);

		val worstCandidate = candidatesByUtility.get(candidatesByUtility.size() - 1);

		if (maximalUtility.compareTo(voter.utility(worstCandidate)) == 0) {
			// tous les candidats sont estimés à la même qualité, ce n'est pas
			// censé se produire
			// pour résoudre ce point, on les évalue tous à la note moyenne
			return candidateSet.stream().map(c -> getCandidateWithScore(0.5, c)).collect(toList());
		}

		return computeResultsNormalCase(candidatesByUtility, maximalUtility);
	}

	@Override
	public Set<Candidate> getCandidates() {
		return unmodifiableSet(candidateSet);
	}

}
