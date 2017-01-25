package fr.dhel.voting.model.entity;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;
import lombok.ToString;
import lombok.val;
import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

@ToString
public class SimpleVoter implements Voter {
	@Getter
	private final PoliticalPosition politicalPosition;

	public SimpleVoter(final PoliticalPosition politicalPosition) {
		this.politicalPosition = Objects.requireNonNull(politicalPosition,
				"politicalPosition should not be null");
	}

	//===================================================================
	// METHODES
	//===================================================================
	
	@Override
	public final BigDecimal utility(
			final Candidate c) {
		return getPoliticalPosition().utility(c);
	}

	@Override
	public final boolean isIgnorant() {
		return false;
	}
	
	@Override
	public final Candidate findClosestCandidate(
			final Set<Candidate> candidateSet) {
		Candidate result = null;
		BigDecimal currentUtility = null;
		for (val c : candidateSet) {
			val newUtility = utility(c);
			if (currentUtility == null || currentUtility.compareTo(newUtility) < 0) {
				currentUtility = newUtility;
				result = c;
			}
		}

		if (result == null)
			throw new NoSuchElementException();
		return result;
	}
	
	@Override
	public final Ballot vote(
			final BallotBuilder builder) {
		return builder.buildFor(this);
	}

	@Override
	public final List<Candidate> sortCandidates(
			final Set<Candidate> candidateSet) {
		return candidateSet.stream().sorted((
				c1, c2) -> utility(c1).compareTo(utility(c2))).collect(toList());
	}
}
