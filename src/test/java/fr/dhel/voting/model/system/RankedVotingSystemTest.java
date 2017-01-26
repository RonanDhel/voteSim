package fr.dhel.voting.model.system;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.Candidate;

@RunWith(MockitoJUnitRunner.class)
public class RankedVotingSystemTest {

	@Mock
	private Candidate zorba;
	
	@Spy
	private RankedVotingSystem rankedVotingSystemSpy;
	
	Set<Candidate> getCandidates() {
		return Collections.singleton(zorba);
	}
	
	@Test
	public void isPluralityType_ShouldReturnFalse() {
		assertFalse(rankedVotingSystemSpy.isPluralityType());
	}
	
	@Test
	public void createBallot_ShouldReturnABallotBuilder() {
		assertNotNull(rankedVotingSystemSpy.createBallot(getCandidates()));
	}
}
