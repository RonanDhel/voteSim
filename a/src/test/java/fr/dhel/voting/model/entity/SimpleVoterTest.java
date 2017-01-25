package fr.dhel.voting.model.entity;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.entity.SimpleVoter;
import fr.dhel.voting.model.entity.Voter;
import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

@RunWith(MockitoJUnitRunner.class)
public class SimpleVoterTest {
	
	private static final BigDecimal FAIRLY_LOW_UTILITY = BigDecimal.valueOf(0.25);
	private static final BigDecimal NULL_UTILITY = BigDecimal.ZERO;
	private static final BigDecimal PERFECT_UTILITY = BigDecimal.valueOf(1);
	private static final BigDecimal AVERAGE_UTILITY = BigDecimal.valueOf(0.5);
	private static final BigDecimal ALMOST_PERFECT_UTILITY = new BigDecimal("0.99985");
	
	
	@Mock
	private Candidate candidate1;
	@Mock
	private Candidate candidate2;
	@Mock
	private Candidate candidate3;
	@Mock
	private Candidate candidate4;
	@Mock
	private Candidate candidate5;
	@Mock
	private Ballot ballot;
	
	@Mock
	private PoliticalPosition politicalPosition;
	
	@Before
	public void setUp() {
		when(politicalPosition.utility(candidate1)).thenReturn(FAIRLY_LOW_UTILITY);
		when(politicalPosition.utility(candidate2)).thenReturn(NULL_UTILITY);
		when(politicalPosition.utility(candidate3)).thenReturn(AVERAGE_UTILITY);
		when(politicalPosition.utility(candidate4)).thenReturn(ALMOST_PERFECT_UTILITY);
		when(politicalPosition.utility(candidate5)).thenReturn(PERFECT_UTILITY);
		
	}
	
	
	@Test
	public void findClosestCandidate_ShouldReturnTheCandidateWithHighestUtility() {
		SimpleVoter sv = new SimpleVoter(politicalPosition);
		
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(candidate1);
		candidates.add(candidate2);
		candidates.add(candidate5);
		
		
		assertThat(sv.findClosestCandidate(candidates), is(equalTo(candidate5)));
	}
	
	@Test(expected=RuntimeException.class)
	public void findClosestCandidate_ShouldThrowAnExceptionIfNoCandidates() {
		SimpleVoter sv = new SimpleVoter(politicalPosition);
		
		Set<Candidate> candidates = new HashSet<>();
		
		sv.findClosestCandidate(candidates);
	}
	
	@Test
	public void vote_ShouldReturnTheBallotFromBallotBuilder() {
		SimpleVoter sv = new SimpleVoter(politicalPosition);
		
		BallotBuilder bb = new BallotBuilder() {
			@Override
			public Ballot buildFor(final Voter voter) {
				return ballot;
			}
		};
		
		assertThat(sv.vote(bb), is(ballot));
	}
	
	@Test
	public void sortCandidate_ShouldSortTheCandidateByUtility() {
		SimpleVoter sv = new SimpleVoter(politicalPosition);
		
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(candidate1);
		candidates.add(candidate3);
		candidates.add(candidate2);
		
		assertThat(sv.sortCandidates(candidates), contains(candidate2, candidate1, candidate3));
	}
	
	@Test
	public void sortCandidate_ShouldSortTheCandidateByUtility2() {
		SimpleVoter sv = new SimpleVoter(politicalPosition);
		
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(candidate1);
		candidates.add(candidate2);
		candidates.add(candidate3);
		candidates.add(candidate4);
		candidates.add(candidate5);
		
		assertThat(sv.sortCandidates(candidates), contains(candidate2, candidate1, candidate3, candidate4, candidate5));
	}
}
