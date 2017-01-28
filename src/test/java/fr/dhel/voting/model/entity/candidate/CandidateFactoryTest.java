package fr.dhel.voting.model.entity.candidate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import lombok.val;

import org.junit.Test;

import fr.dhel.voting.model.entity.candidate.CandidateFactory;
import fr.dhel.voting.model.entity.candidate.PoliticalSkill;
import fr.dhel.voting.model.entity.politicalpos.IssuesBasedPoliticalPosition;

public class CandidateFactoryTest {
	@Test(expected=NullPointerException.class)
	public void createCandidate_ShouldFailIfNoNumberGenerator() {
		int numberOfIssues = 5;

		CandidateFactory.createCandidate(1, "MyNameIsStan=", numberOfIssues, null);
	}
	
	@Test
	public void createCandidate_ShouldReturnAveragePoliticalSKillWhenNotPresent() {
		int numberOfIssues = 8;

		assertThat(
				CandidateFactory.createCandidate(17, "AlsQYrmh=kbZ1Byc", numberOfIssues, () -> 1.0)
						.getPoliticalSkill(), is(equalTo(PoliticalSkill.AVERAGE)));
	}

	@Test
	public void createCandidate_ShouldReturnNotPreent() {
		int numberOfIssues = 3;
		for (PoliticalSkill politicalSkill : PoliticalSkill.values()) {
			val c = CandidateFactory.createCandidate(21, "rHwMH7=bEI0gpvBUG", numberOfIssues, () -> 5.0, politicalSkill);
			assertThat(c.getId(), is(equalTo(21)));
			assertNotNull(c.name());
			assertThat(c.getPoliticalPosition(), is(instanceOf(IssuesBasedPoliticalPosition.class)));
			assertThat(c.getPoliticalSkill(), is(equalTo(politicalSkill)));
		}

		numberOfIssues = 5;
		for (PoliticalSkill politicalSkill : PoliticalSkill.values()) {
			val c = CandidateFactory.createCandidate(42, "LmoYJsXFcsp=EDSK", numberOfIssues, () -> 5.0, politicalSkill);
			numberOfIssues++;
			assertThat(c.getId(), is(equalTo(42)));
			assertNotNull(c.name());
			assertThat(c.getPoliticalPosition(), is(instanceOf(IssuesBasedPoliticalPosition.class)));
			assertThat(c.getPoliticalSkill(), is(equalTo(politicalSkill)));
		}
	}
}
