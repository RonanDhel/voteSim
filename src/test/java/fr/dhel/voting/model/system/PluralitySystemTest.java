package fr.dhel.voting.model.system;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.system.ballot.Ballot;


@RunWith(MockitoJUnitRunner.class)
public class PluralitySystemTest {
	
	@Mock
	private List<Ballot> votes;
	@Mock
	private Ballot ballot;
	@Mock
	private Candidate gandalf;
	@Mock
	private Candidate aragorn;
	
	@Test
	public void shortName_ShouldReturnTheName() {
		PluralitySystem ps = new PluralitySystem();
		
		assertThat(ps.shortName(), is(equalTo("PL1")));
	}
	
	@Test
	public void countVotes_Should() {
		PluralitySystem ps = new PluralitySystem();
		
		Set<Candidate> candidates = new HashSet<>();
		candidates.add(gandalf);
		candidates.add(aragorn);
		
		
		ps.countVotes(votes, candidates);
	}
}
