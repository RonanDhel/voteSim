package fr.dhel.voting.model.system;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.system.ballot.Ballot;

@RunWith(MockitoJUnitRunner.class)
public class AntiPluralitySystemTest {

	@Mock
	private Ballot ballotForGandalf;
	@Mock
	private Ballot ballotForAragorn;
	@Mock
	private Ballot ballotForGimli;
	@Mock
	private Candidate gandalf;
	@Mock
	private Candidate aragorn;
	@Mock
	private Candidate gimli;

	@Test
	public void shortName_ShouldReturnTheName() {
		AntiPluralitySystem aps = new AntiPluralitySystem();

		assertThat(aps.shortName(), is(equalTo("APL")));
	}

	@Test
	public void isPluralityType_ShouldReturnTrue() {
		assertTrue(new AntiPluralitySystem().isPluralityType());
	}

	Set<Candidate> getCandidates() {
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(gandalf);
		candidates.add(aragorn);
		candidates.add(gimli);
		return candidates;
	}

	@Test
	public void createBallot_ShouldReturnABallotBuilder() {
		assertNotNull(new AntiPluralitySystem().createBallot(getCandidates()));
	}

	@Test
	public void countVotes_ShouldElectTheCandidateWithTheLeastVote() {
		AntiPluralitySystem aps = new AntiPluralitySystem();

		when(ballotForGandalf.computeResults()).thenReturn(
				Collections.singletonList(Pair.of(gandalf, 1.0)));
		when(ballotForAragorn.computeResults()).thenReturn(
				Collections.singletonList(Pair.of(aragorn, 1.0)));
		when(ballotForGimli.computeResults()).thenReturn(
				Collections.singletonList(Pair.of(gimli, 1.0)));

		List<Ballot> votes = Arrays.asList(ballotForGandalf, ballotForAragorn, ballotForGimli,
				ballotForGimli, ballotForAragorn, ballotForGimli);

		assertThat(aps.countVotes(votes, getCandidates()).getElectedCandidate(),
				is(equalTo(gandalf)));
	}
}
