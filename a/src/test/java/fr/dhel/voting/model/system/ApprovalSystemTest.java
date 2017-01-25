package fr.dhel.voting.model.system;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.system.ApprovalSystem;
import fr.dhel.voting.model.system.ballot.Ballot;


@RunWith(MockitoJUnitRunner.class)
public class ApprovalSystemTest {
	
	@Mock
	private List<Ballot> votes;
	@Mock
	private Candidate alice;
	@Mock
	private Candidate scarface;
	
	@Test
	public void shortName_ShouldReturnTheName() {
		ApprovalSystem as = new ApprovalSystem();
		
		assertThat(as.shortName(), is(equalTo("AS")));
	}
	
	@Test
	public void countVotes_Should() {
		ApprovalSystem as = new ApprovalSystem();
		
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(alice);
		candidates.add(scarface);
		
		as.countVotes(votes, candidates);
	}
}
