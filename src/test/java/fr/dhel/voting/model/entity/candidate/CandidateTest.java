package fr.dhel.voting.model.entity.candidate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import lombok.val;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.entity.candidate.PoliticalSkill;
import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;

@RunWith(MockitoJUnitRunner.class)
public class CandidateTest {

	@Mock
	private PoliticalPosition politicalPosition;
	
	@Test
	public void name_ShouldReturnTheName() {
		final String firstName = "Test";
		Candidate c = new Candidate(1, firstName, politicalPosition, PoliticalSkill.AVERAGE);
		
		assertThat(c.name(), is(equalTo(firstName)));
		
		final String secondName = "Lucy Heartfillia";
		c = new Candidate(2, secondName, politicalPosition, PoliticalSkill.VERY_HIGH);
		
		assertThat(c.name(), is(equalTo(secondName)));
	}

	@Test
	public void getPoliticalSkill_ShouldReturnThePoliticalSkill() {
		final String name = "Test";
		
		for (val politicalSkill : PoliticalSkill.values()) {
			Candidate c = new Candidate(3, name, politicalPosition, politicalSkill);
		
			assertThat(c.getPoliticalSkill(), is(equalTo(politicalSkill)));
		}
	}
}
