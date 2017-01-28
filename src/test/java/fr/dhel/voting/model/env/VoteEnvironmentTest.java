package fr.dhel.voting.model.env;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.val;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.entity.voter.Voter;
import fr.dhel.voting.model.system.VotingSystem;

@RunWith(MockitoJUnitRunner.class)
public class VoteEnvironmentTest {
	
	@Mock
	private Candidate popeye;
	@Mock
	private VotingSystem votingSystem;
	
	List<Voter> getListOfVoters() {
		return Arrays.asList();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void builder_ShouldNotBuildAnEnvWithNoCandidate() {
		VoteEnvironment.builder().votingSystem(votingSystem).build();
	}
	
	@Test(expected=RuntimeException.class)
	public void builder_ShouldNotBuildAnEnvWithNoVotingSystem() {
		VoteEnvironment.builder().candidate(popeye).voters(Arrays.asList()).build();
	}
	
	@Test
	public void countVotes_ShouldDelegateToTheVotingSystem() {
		val candidateSet = Collections.singleton(popeye);
		val voteEnv = new VoteEnvironment(votingSystem, candidateSet, getListOfVoters());
		
		when(votingSystem.countVotes(any(), eq(candidateSet))).thenReturn(new ElectionResult(popeye));
		
		voteEnv.countVotes(v -> v.visitPlurality(candidateSet));
		
		verify(votingSystem).countVotes(any(), eq(candidateSet));
	}
	
	@Test
	public void countVotesAndComputeElectionResult_ShouldReturnTheBestCandidateFromTheSystem() {
		val candidateSet = Collections.singleton(popeye);
		val voteEnv = new VoteEnvironment(votingSystem, candidateSet, getListOfVoters());
		
		when(votingSystem.countVotes(any(), eq(candidateSet))).thenReturn(new ElectionResult(popeye));
		
		assertThat(voteEnv.countVotesAndComputeElectionResult(), is(equalTo(popeye)));
	}

	@Test
	public void toString_ShouldReturnADescription() {
		assertNotNull(new VoteEnvironment(votingSystem, Collections.singleton(popeye),
				Arrays.asList()).toString());
	}
}
