package fr.dhel.voting.model.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.system.ballot.Ballot;

@RunWith(MockitoJUnitRunner.class)
public class MajorityJudgmentSystemTest {

    @Mock
    private Ballot ballotWithPerfectScoreForSithAndLowScoreForYoda;
    @Mock
    private Ballot ballotWithMainlyHighValue;
    @Mock
    private Ballot ballotWithOnlyLowValueAndNotPointForPalpatine;
    @Mock
    private Ballot ballotMainlyForPalpatineAndHanSolo;
    @Mock
    private Ballot ballotForJedi;
    @Mock
    private Ballot ballotWithMediumValue;
    @Mock
    private Ballot ballotWithOnlyLowValueAndNotPointForHanSolo;
    @Mock
    private Candidate anakinSkywalker;
    @Mock
    private Candidate hanSolo;
    @Mock
    private Candidate obiWan;
    @Mock
    private Candidate yoda;
    @Mock
    private Candidate palpatine;

    Set<Candidate> getCandidates() {
        Set<Candidate> candidates = new HashSet<>();
        candidates.add(anakinSkywalker);
        candidates.add(hanSolo);
        candidates.add(obiWan);
        candidates.add(yoda);
        candidates.add(palpatine);
        return candidates;
    }

    @Before
    public void setup() {
        when(ballotWithPerfectScoreForSithAndLowScoreForYoda.computeResults())
                .thenReturn(Arrays.asList(Pair.of(anakinSkywalker, 4.0), Pair.of(hanSolo, 3.0),
                        Pair.of(obiWan, 3.0), Pair.of(yoda, 2.0), Pair.of(palpatine, 4.0)));

        when(ballotWithMainlyHighValue.computeResults())
                .thenReturn(Arrays.asList(Pair.of(anakinSkywalker, 3.0), Pair.of(hanSolo, 3.0),
                        Pair.of(obiWan, 3.0), Pair.of(yoda, 4.0), Pair.of(palpatine, 4.0)));

        when(ballotWithOnlyLowValueAndNotPointForPalpatine.computeResults())
                .thenReturn(Arrays.asList(Pair.of(anakinSkywalker, 2.0), Pair.of(hanSolo, 1.0),
                        Pair.of(obiWan, 2.0), Pair.of(yoda, 2.0), Pair.of(palpatine, 0.0)));

        when(ballotMainlyForPalpatineAndHanSolo.computeResults())
                .thenReturn(Arrays.asList(Pair.of(anakinSkywalker, 1.0), Pair.of(hanSolo, 3.0),
                        Pair.of(obiWan, 2.0), Pair.of(yoda, 2.0), Pair.of(palpatine, 4.0)));

        when(ballotForJedi.computeResults())
                .thenReturn(Arrays.asList(Pair.of(anakinSkywalker, 3.0), Pair.of(hanSolo, 1.0),
                        Pair.of(obiWan, 2.0), Pair.of(yoda, 4.0), Pair.of(palpatine, 0.0)));

        when(ballotWithMediumValue.computeResults())
                .thenReturn(Arrays.asList(Pair.of(anakinSkywalker, 1.0), Pair.of(hanSolo, 2.0),
                        Pair.of(obiWan, 2.0), Pair.of(yoda, 2.0), Pair.of(palpatine, 2.0)));

        when(ballotWithOnlyLowValueAndNotPointForHanSolo.computeResults())
                .thenReturn(Arrays.asList(Pair.of(anakinSkywalker, 1.0), Pair.of(hanSolo, 0.0),
                        Pair.of(obiWan, 2.0), Pair.of(yoda, 2.0), Pair.of(palpatine, 2.0)));

        when(ballotMainlyForPalpatineAndHanSolo.computeResults())
                .thenReturn(Arrays.asList(Pair.of(anakinSkywalker, 1.0), Pair.of(hanSolo, 3.0),
                        Pair.of(obiWan, 2.0), Pair.of(yoda, 2.0), Pair.of(palpatine, 4.0)));

    }

    @Test
    public void shortName_ShouldReturnTheName() {
        assertThat(new MajorityJudgmentSystem().shortName()).isEqualTo("MJ");
    }

    @Test
    public void createBallot_ShouldReturnABallotBuilder() {
        assertThat(new MajorityJudgmentSystem().createBallot(getCandidates())).isNotNull();
    }

    @Test
    public void countVotes_ShouldElectTheCandidateWithBestMedianButNotNecessarilyTheHighestAverage() {
        MajorityJudgmentSystem mjs = new MajorityJudgmentSystem();

        List<Ballot> votes = new ArrayList<>();
        votes.add(ballotWithPerfectScoreForSithAndLowScoreForYoda);
        votes.add(ballotWithMainlyHighValue);
        votes.add(ballotWithOnlyLowValueAndNotPointForPalpatine);
        votes.add(ballotMainlyForPalpatineAndHanSolo);
        votes.add(ballotForJedi);

        // yoda has the highest average with 2.8, palpatine has 2.4
        assertThat(mjs.countVotes(votes, getCandidates()).getElectedCandidate()).isEqualTo(palpatine);
    }

    @Test
    public void countVotes_ShouldElectTheCandidateEvenIfMultipleCandidateHaveTheSameMedian() {
        MajorityJudgmentSystem mjs = new MajorityJudgmentSystem();

        for (int i = 0; i < 10_000; i++) {
            List<Ballot> votes = new ArrayList<>();
            votes.add(ballotWithMainlyHighValue);
            votes.add(ballotWithOnlyLowValueAndNotPointForPalpatine);
            votes.add(ballotWithMediumValue);
            votes.add(ballotWithOnlyLowValueAndNotPointForHanSolo);

            assertThat(mjs.countVotes(votes, getCandidates()).getElectedCandidate()).isIn(palpatine,
                    yoda);
        }
    }
}
