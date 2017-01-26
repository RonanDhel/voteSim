package fr.dhel.voting.model.system;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import lombok.val;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.system.RandomBallotSystem;
import fr.dhel.voting.model.system.ballot.Ballot;


@RunWith(MockitoJUnitRunner.class)
public class RandomBallotSystemTest {
	
	private List<Ballot> votes;
	@Mock
	private Ballot ballotForAlice;
	@Mock
	private Ballot ballotForMadHatter;
	@Mock
	private Candidate alice;
	@Mock
	private Candidate madHatter;
	
	@Test
	public void shortName_ShouldReturnTheName() {
		RandomBallotSystem rbs = new RandomBallotSystem(new Random());
		
		assertThat(rbs.shortName(), is(equalTo("RB")));
	}
	
	Set<Candidate> getCandidates() {
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(alice);
		candidates.add(madHatter);
		return candidates;
	}
	
	@Test
	public void createBallot_ShouldReturnABallotBuilder() {
		assertNotNull(new RandomBallotSystem(new Random()).createBallot(getCandidates()));
	}
	
	@Test
	public void countVotes_ShouldReturnVariousCandidateDependingOnRandom() {
		RandomBallotSystem rbs = new RandomBallotSystem(new Random(5));
		
		when(ballotForAlice.computeResults()).thenReturn(Collections.singletonList(Pair.of(alice, 1.0)));
		when(ballotForMadHatter.computeResults()).thenReturn(Collections.singletonList(Pair.of(madHatter, 1.0)));
		votes = new ArrayList<>();
		votes.add(ballotForAlice);
		votes.add(ballotForMadHatter);
		votes.add(ballotForAlice);
		
		val candidates = getCandidates();
		
		
		assertThat(rbs.countVotes(votes, candidates).getElectedCandidate(), is(alice));
		assertThat(rbs.countVotes(votes, candidates).getElectedCandidate(), is(madHatter));
		assertThat(rbs.countVotes(votes, candidates).getElectedCandidate(), is(alice));
		assertThat(rbs.countVotes(votes, candidates).getElectedCandidate(), is(alice));
		assertThat(rbs.countVotes(votes, candidates).getElectedCandidate(), is(alice));
	}
	
	@Test
	public void countVotes_ShouldElectACandidateProportionallyToTheNumberOfVote() {
		RandomBallotSystem rbs = new RandomBallotSystem(new Random(2));
		
		when(ballotForAlice.computeResults()).thenReturn(Collections.singletonList(Pair.of(alice, 1.0)));
		when(ballotForMadHatter.computeResults()).thenReturn(Collections.singletonList(Pair.of(madHatter, 1.0)));
		votes = new ArrayList<>();
		votes.add(ballotForMadHatter);
		votes.add(ballotForAlice);
		votes.add(ballotForAlice);
		votes.add(ballotForAlice);
		votes.add(ballotForAlice);
		votes.add(ballotForAlice);
		votes.add(ballotForAlice);
		votes.add(ballotForAlice);
		votes.add(ballotForAlice);
		votes.add(ballotForAlice);
		
		val candidates = getCandidates();
		
		
		List<Candidate> elected = new ArrayList<>();
		for (int i = 0 ; i < 100_000 ; i++) {
			elected.add(rbs.countVotes(votes, candidates).getElectedCandidate());
		}
		
		long numberOfTimesHatterHasBeenElected = elected.stream().filter(c -> c.equals(madHatter)).count();
		assertThat(numberOfTimesHatterHasBeenElected, is(greaterThan(9500L)));
		assertThat(numberOfTimesHatterHasBeenElected, is(lessThan(10_200L)));
		
	}
}
