package fr.dhel.voting.model.entity.candidate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;

@RunWith(MockitoJUnitRunner.class)
public class CandidateTest {

    @Mock
    private PoliticalPosition politicalPosition;

    @Test
    public void name_ShouldReturnTheName() {
        final String firstName = "Test";
        Candidate c = new Candidate(1, firstName, politicalPosition, PoliticalSkill.AVERAGE);

        assertThat(c.name()).isEqualTo(firstName);

        final String secondName = "Lucy Heartfillia";
        c = new Candidate(2, secondName, politicalPosition, PoliticalSkill.VERY_HIGH);

        assertThat(c.name()).isEqualTo(secondName);
    }

    @Test
    public void getPoliticalSkill_ShouldReturnThePoliticalSkill() {
        final String name = "Test";

        for (var politicalSkill : PoliticalSkill.values()) {
            Candidate c = new Candidate(3, name, politicalPosition, politicalSkill);

            assertThat(c.getPoliticalSkill()).isEqualTo(politicalSkill);
        }
    }
}
