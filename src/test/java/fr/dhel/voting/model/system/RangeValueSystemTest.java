package fr.dhel.voting.model.system;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.val;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.system.ballot.Ballot;

@RunWith(MockitoJUnitRunner.class)
public class RangeValueSystemTest {

	private RangeValueSystem rv = new RangeValueSystem(0, 6);

	@Mock
	private Ballot ballotMainlyForLuffy;
	@Mock
	private Ballot ballotWithMidValue;
	@Mock
	private Ballot ballotOnlyForNami;
	@Mock
	private Ballot ballotOnlyForSanjiAndZorro;
	@Mock
	private Candidate luffy;
	@Mock
	private Candidate sanji;
	@Mock
	private Candidate nami;
	@Mock
	private Candidate zorro;
	

	@Before
	public void setup() {
		when(ballotMainlyForLuffy.computeResults()).thenReturn(
				Arrays.asList(Pair.of(luffy, 5.0), Pair.of(sanji, 0.0), Pair.of(nami, 2.0),
						Pair.of(zorro, 3.0)));
		when(ballotWithMidValue.computeResults()).thenReturn(
				Arrays.asList(Pair.of(luffy, 3.0), Pair.of(sanji, 3.0), Pair.of(nami, 3.0),
						Pair.of(zorro, 4.0)));
		when(ballotOnlyForNami.computeResults()).thenReturn(
				Arrays.asList(Pair.of(luffy, 0.0), Pair.of(sanji, 0.0), Pair.of(nami, 6.0),
						Pair.of(zorro, 0.0)));
		when(ballotOnlyForSanjiAndZorro.computeResults()).thenReturn(
				Arrays.asList(Pair.of(luffy, 0.0), Pair.of(sanji, 6.0), Pair.of(nami, 0.0),
						Pair.of(zorro, 6.0)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor_ShouldFailWhenMinValueIsEqualToMaxValue() {
		new RangeValueSystem(3, 3);
	}

	@Test
	public void shortName_ShouldReturnTheName() {
		assertThat(rv.shortName(), is(equalTo("RV")));
	}

	@Test
	public void isPluralityType_ShouldReturnFalse() {
		assertFalse(new RangeValueSystem(0, 2).isPluralityType());
	}

	Set<Candidate> getCandidates() {
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(luffy);
		candidates.add(sanji);
		candidates.add(nami);
		candidates.add(zorro);
		return candidates;
	}
	
	@Test
	public void createBallot_ShouldReturnABallotBuilder() {
		assertNotNull(rv.createBallot(getCandidates()));
	}
	

	@Test
	public void countVotes_ShouldElectTheCandidateWithHighestAverageScore() {
		val candidates = getCandidates();

		List<Ballot> votes = new ArrayList<>();
		votes.add(ballotMainlyForLuffy);
		votes.add(ballotWithMidValue);
		votes.add(ballotOnlyForNami);
		votes.add(ballotOnlyForSanjiAndZorro);

		assertThat(rv.countVotes(votes, candidates).getElectedCandidate(), is(equalTo(zorro)));
		
		votes = new ArrayList<>();
		votes.add(ballotMainlyForLuffy);
		
		assertThat(rv.countVotes(votes, candidates).getElectedCandidate(), is(equalTo(luffy)));
		
		votes = new ArrayList<>();
		votes.add(ballotMainlyForLuffy);
		votes.add(ballotOnlyForNami);
		
		assertThat(rv.countVotes(votes, candidates).getElectedCandidate(), is(equalTo(nami)));
	}
}
