package fr.dhel.voting.model.entity.candidate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

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
                        .getPoliticalSkill())
                .isEqualTo(PoliticalSkill.AVERAGE);
    }

    @Test
    public void createCandidate_ShouldReturnNotPreent() {
        int numberOfIssues = 3;
        for (var politicalSkill : PoliticalSkill.values()) {
            var c = CandidateFactory.createCandidate(21, "rHwMH7=bEI0gpvBUG", numberOfIssues,
                    () -> 5.0, politicalSkill);
            assertThat(c.getId()).isEqualTo(21);
            assertThat(c.name()).isNotNull();
            assertThat(c.getPoliticalPosition()).isInstanceOf(IssuesBasedPoliticalPosition.class);
            assertThat(c.getPoliticalSkill()).isEqualTo(politicalSkill);
        }

        numberOfIssues = 5;
        for (var politicalSkill : PoliticalSkill.values()) {
            var c = CandidateFactory.createCandidate(42, "LmoYJsXFcsp=EDSK", numberOfIssues,
                    () -> 5.0, politicalSkill);
            numberOfIssues++;
            assertThat(c.getId()).isEqualTo(42);
            assertThat(c.name()).isNotNull();
            assertThat(c.getPoliticalPosition()).isInstanceOf(IssuesBasedPoliticalPosition.class);
            assertThat(c.getPoliticalSkill()).isEqualTo(politicalSkill);
        }
    }
}
