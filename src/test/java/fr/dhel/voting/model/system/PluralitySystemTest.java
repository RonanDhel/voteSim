package fr.dhel.voting.model.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.system.ballot.Ballot;

@RunWith(MockitoJUnitRunner.class)
public class PluralitySystemTest {

    @Mock
    private Ballot ballotForGandalf;
    @Mock
    private Ballot ballotForAragorn;
    @Mock
    private Candidate gandalf;
    @Mock
    private Candidate aragorn;

    @Test
    public void shortName_ShouldReturnTheName() {
        PluralitySystem ps = new PluralitySystem();

        assertThat(ps.shortName()).isEqualTo("PL1");
    }

    @Test
    public void isPluralityType_ShouldReturnTrue() {
        assertThat(new PluralitySystem().isPluralityType()).isTrue();
    }

    Set<Candidate> getCandidates() {
        Set<Candidate> candidates = new HashSet<>();
        candidates.add(gandalf);
        candidates.add(aragorn);
        return candidates;
    }

    @Test
    public void createBallot_ShouldReturnABallotBuilder() {
        assertThat(new PluralitySystem().createBallot(getCandidates())).isNotNull();
    }

    @Test
    public void countVotes_ShouldElectTheCandidateWithTheMostVote() {
        PluralitySystem ps = new PluralitySystem();

        var candidates = getCandidates();

        when(ballotForGandalf.computeResults())
                .thenReturn(Collections.singletonList(Pair.of(gandalf, 1.0)));
        when(ballotForAragorn.computeResults())
                .thenReturn(Collections.singletonList(Pair.of(aragorn, 1.0)));

        for (int i = 0; i < 10; i++) {
            List<Ballot> votes = new ArrayList<>();
            final Candidate expectedCandidate;

            votes.add(ballotForGandalf);
            votes.add(ballotForAragorn);
            if (i % 2 == 0) {
                votes.add(ballotForGandalf);
                expectedCandidate = gandalf;
            } else {
                votes.add(ballotForAragorn);
                expectedCandidate = aragorn;
            }

            assertThat(ps.countVotes(votes, candidates).getElectedCandidate())
                    .isEqualTo(expectedCandidate);
        }
    }
}
