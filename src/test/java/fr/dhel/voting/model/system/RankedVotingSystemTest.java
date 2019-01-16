package fr.dhel.voting.model.system;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;

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
        assertThat(rankedVotingSystemSpy.isPluralityType()).isFalse();
    }

    @Test
    public void createBallot_ShouldReturnABallotBuilder() {
        assertThat(rankedVotingSystemSpy.createBallot(getCandidates())).isNotNull();
    }
}
