package fr.dhel.voting.model.system;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.system.RandomBallotSystem;
import fr.dhel.voting.model.system.ballot.Ballot;


@RunWith(MockitoJUnitRunner.class)
public class RandomBallotSystemTest {
	
	@Mock
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
	
	@Test
	public void countVotes_ShouldReturnVariousCandidateDependingOnRandom() {
		RandomBallotSystem rbs = new RandomBallotSystem(new Random(5));
		
		when(ballotForAlice.computeResults()).thenReturn(Collections.singletonList(Pair.of(alice, 1.0)));
		when(ballotForMadHatter.computeResults()).thenReturn(Collections.singletonList(Pair.of(madHatter, 1.0)));
		when(votes.size()).thenReturn(3);
		when(votes.get(0)).thenReturn(ballotForAlice);
		when(votes.get(1)).thenReturn(ballotForMadHatter);
		when(votes.get(2)).thenReturn(ballotForAlice);
		
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(alice);
		candidates.add(madHatter);

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
		when(votes.size()).thenReturn(10);
		when(votes.get(0)).thenReturn(ballotForMadHatter);
		when(votes.get(1)).thenReturn(ballotForAlice);
		when(votes.get(2)).thenReturn(ballotForAlice);
		when(votes.get(3)).thenReturn(ballotForAlice);
		when(votes.get(4)).thenReturn(ballotForAlice);
		when(votes.get(5)).thenReturn(ballotForAlice);
		when(votes.get(6)).thenReturn(ballotForAlice);
		when(votes.get(7)).thenReturn(ballotForAlice);
		when(votes.get(8)).thenReturn(ballotForAlice);
		when(votes.get(9)).thenReturn(ballotForAlice);
		
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(alice);
		candidates.add(madHatter);

		List<Candidate> elected = new ArrayList<>();
		for (int i = 0 ; i < 100_000 ; i++) {
			elected.add(rbs.countVotes(votes, candidates).getElectedCandidate());
		}
		
		long numberOfTimesHatterHasBeenElected = elected.stream().filter(c -> c.equals(madHatter)).count();
		assertThat(numberOfTimesHatterHasBeenElected, is(greaterThan(9500L)));
		assertThat(numberOfTimesHatterHasBeenElected, is(lessThan(10_200L)));
		
	}
}
