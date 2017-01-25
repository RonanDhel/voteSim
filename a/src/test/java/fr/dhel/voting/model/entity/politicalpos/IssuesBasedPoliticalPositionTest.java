package fr.dhel.voting.model.entity.politicalpos;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.Candidate;
import fr.dhel.voting.model.entity.politicalpos.Issues;
import fr.dhel.voting.model.entity.politicalpos.IssuesBasedPoliticalPosition;

@RunWith(MockitoJUnitRunner.class)
public class IssuesBasedPoliticalPositionTest {
	
	@Mock
	private Candidate candidate;
	
	@Test
	public void utility_ShouldReturnTheUtilityOfTheCandidate() {
		Issues issues = new Issues(Arrays.asList(1.0, 2.0, 0.5));
		IssuesBasedPoliticalPosition a = new IssuesBasedPoliticalPosition(issues);
		
		IssuesBasedPoliticalPosition b = new IssuesBasedPoliticalPosition(issues);
		
		when(candidate.getPoliticalPosition()).thenReturn(b);
		
		assertThat(a.utility(candidate), is(equalTo(new BigDecimal("1.0"))));
		
	}
}
