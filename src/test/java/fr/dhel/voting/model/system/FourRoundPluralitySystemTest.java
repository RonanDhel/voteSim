package fr.dhel.voting.model.system;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.system.ballot.Ballot;

@RunWith(MockitoJUnitRunner.class)
public class FourRoundPluralitySystemTest {
	
	private final Random r = new Random();
	@Mock
	private Candidate luffy;
	@Mock
	private Candidate sanji;
	@Mock
	private Candidate nami;
	@Mock
	private Candidate robin;
	@Mock
	private Candidate zorro;
	@Mock
	private Candidate chopper;
	@Mock
	private Candidate franky;
	@Mock
	private Candidate brook;
	@Mock
	private Candidate ussop;
	@Mock
	private Candidate jinbei;
	
	@Test
	public void shortName_ShouldReturnTheName() {
		FourRoundPluralitySystem ps = new FourRoundPluralitySystem();
		
		assertThat(ps.shortName(), is(equalTo("PL4")));
	}
	
	@Test
	public void isPluralityType_ShouldReturnTrue() {
		assertTrue(new FourRoundPluralitySystem().isPluralityType());
	}

	Set<Candidate> getCandidates() {
		Set<Candidate> res = new HashSet<>();
		res.add(luffy);
		res.add(sanji);
		res.add(nami);
		res.add(robin);
		res.add(zorro);
		res.add(chopper);
		res.add(franky);
		res.add(brook);
		res.add(ussop);
		res.add(jinbei);
		return res;
	}
	
	@Test
	public void createBallot_ShouldReturnABallotBuilder() {
		assertNotNull(new FourRoundPluralitySystem().createBallot(getCandidates()));
	}

	@Test
	public void countVotes_ShouldElectTheOneWithAnAbsoluteMajority() {
		FourRoundPluralitySystem ps = new FourRoundPluralitySystem();
		List<Ballot> votes = new ArrayList<>();

		for (int i = 0; i < 51; i++) {
			// ce candidat a la moitié des votes + 1 à lui tout seul
			Ballot b = mock(Ballot.class);
			when(b.computeResults()).thenReturn(Collections.singletonList(Pair.of(luffy, 1.0)));
			votes.add(b);
		}
		List<Candidate> opposingCandidates = Arrays.asList(sanji, nami, robin, zorro, chopper,
				franky);
		for (int i = 0; i < 50; i++) {
			Ballot b = mock(Ballot.class);
			Candidate chosenCandidate = opposingCandidates
					.get(r.nextInt(opposingCandidates.size()));
			when(b.computeResults())
					.thenReturn(Collections.singletonList(Pair.of(chosenCandidate, 1.0)));
			votes.add(b);
		}

		assertThat(ps.countVotes(votes, getCandidates()).getElectedCandidate(), is(equalTo(luffy)));
	}
	
	@Test
	public void countVotes_ShouldRemoveAllWeakCandidateIfNoOneHaveAnAbsoluteMajority() {
		FourRoundPluralitySystem ps = new FourRoundPluralitySystem();
		List<Ballot> votes = new ArrayList<>();

		Set<Candidate> candidates = getCandidates();
		
		for (int i = 0 ; i < 10; i++) {
			// 10 votes sur 40 soit 25%
			Ballot b = mock(Ballot.class);
			when(b.computeResults())
					.thenReturn(Collections.singletonList(Pair.of(luffy, 1.0)));
			votes.add(b);
		}
		for (int i = 0; i < 10; i++) {
			// 10 votes sur 40 soit 25%
			Ballot b = mock(Ballot.class);
			when(b.computeResults())
					.thenReturn(Collections.singletonList(Pair.of(nami, 1.0)));
			votes.add(b);
		}
		for (int i = 0; i < 8; i++) {
			// 8 votes sur 40 soit 20%
			Ballot b = mock(Ballot.class);
			when(b.computeResults())
					.thenReturn(Collections.singletonList(Pair.of(sanji, 1.0)));
			votes.add(b);
		}
		for (int i = 0; i < 7; i++) {
			// 7 votes sur 40 soit 17.5%
			Ballot b = mock(Ballot.class);
			when(b.computeResults())
					.thenReturn(Collections.singletonList(Pair.of(zorro, 1.0)));
			votes.add(b);
		}
		for (int i = 0; i < 3; i++) {
			// 3 votes sur 40 soit 7.5%
			Ballot b = mock(Ballot.class);
			when(b.computeResults())
					.thenReturn(Collections.singletonList(Pair.of(chopper, 1.0)));
			votes.add(b);
		}
		// 1 votes sur 40 soit 2.5%
		Ballot b = mock(Ballot.class);
		when(b.computeResults())
				.thenReturn(Collections.singletonList(Pair.of(robin, 1.0)));
		votes.add(b);
		// 1 votes sur 40 soit 2.5%
		Ballot b2 = mock(Ballot.class);
		when(b2.computeResults())
				.thenReturn(Collections.singletonList(Pair.of(franky, 1.0)));
		votes.add(b2);

		
		assertTrue(ps.countVotes(votes, candidates).needNextRound());
		Set<Candidate> expected = new HashSet<>();
		expected.add(luffy);
		expected.add(nami);
		expected.add(sanji);
		expected.add(zorro);
		assertThat(ps.countVotes(votes, candidates).candidatesForNextRound(), is(equalTo(expected)));
	}

	@Test
	public void countVotes_ShouldSelectTopTwoOnThirdTurn() {
		FourRoundPluralitySystem ps = new FourRoundPluralitySystem();
		List<Ballot> votes = new ArrayList<>();
		
		for (int i = 0; i < 60; i++) {
			Ballot b = mock(Ballot.class);
			when(b.computeResults()).thenReturn(Collections.singletonList(Pair.of(nami, 1.0)));
			votes.add(b);
		}
		for (int i = 0; i < 40; i++) {
			Ballot b = mock(Ballot.class);
			when(b.computeResults()).thenReturn(Collections.singletonList(Pair.of(luffy, 1.0)));
			votes.add(b);
		}
		for (int i = 0; i < 30; i++) {
			Ballot b = mock(Ballot.class);
			when(b.computeResults()).thenReturn(Collections.singletonList(Pair.of(zorro, 1.0)));
			votes.add(b);
		}

		assertTrue(ps.countVotes(votes, getCandidates()).needNextRound());

		Set<Candidate> res = ps.countVotes(votes, getCandidates()).candidatesForNextRound();

		assertThat(res, hasSize(2));
		assertThat(res, containsInAnyOrder(luffy, nami));
	}

	@Test
	public void countVotes_ShouldSelectBest3IfTooManyWeakCandidate() {
		FourRoundPluralitySystem ps = new FourRoundPluralitySystem();
		List<Ballot> votes = new ArrayList<>();
		
		for (int i = 0; i < 40; i++) {
			Ballot b = mock(Ballot.class);
			when(b.computeResults()).thenReturn(Collections.singletonList(Pair.of(luffy, 1.0)));
			votes.add(b);
		}
		Ballot b1 = mock(Ballot.class);
		when(b1.computeResults()).thenReturn(Collections.singletonList(Pair.of(sanji, 1.0)));
		votes.add(b1);
		for (int i = 0; i < 70; i++) {
			Ballot b = mock(Ballot.class);
			Candidate chosenCandidate = null;
			switch (i % 9) {
				case 0:
					chosenCandidate = sanji;
					break;
				case 1:
					chosenCandidate = nami;
					break;
				case 2:
					chosenCandidate = robin;
					break;
				case 3:
					chosenCandidate = zorro;
					break;
				case 4:
					chosenCandidate = chopper;
					break;
				case 5:
					chosenCandidate = franky;
					break;
				case 6:
					chosenCandidate = brook;
					break;
				case 7:
					chosenCandidate = ussop;
					break;
				case 8:
					chosenCandidate = jinbei;
					break;
				default:
					fail();
			}
			when(b.computeResults())
					.thenReturn(Collections.singletonList(Pair.of(chosenCandidate, 1.0)));
			votes.add(b);
		}

		
		assertTrue(ps.countVotes(votes, getCandidates()).needNextRound());
		
		Set<Candidate> res = ps.countVotes(votes, getCandidates()).candidatesForNextRound();
		
		assertThat(res, hasSize(3));
		assertThat(res, containsInAnyOrder(luffy, sanji, brook));
	}
}
