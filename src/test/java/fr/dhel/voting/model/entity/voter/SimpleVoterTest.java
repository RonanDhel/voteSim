package fr.dhel.voting.model.entity.voter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;
import fr.dhel.voting.model.entity.voter.Voter.BallotVisitor;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

@RunWith(MockitoJUnitRunner.class)
public class SimpleVoterTest {

    private static final BigDecimal FAIRLY_LOW_UTILITY = BigDecimal.valueOf(0.25);
    private static final BigDecimal NULL_UTILITY = BigDecimal.ZERO;
    private static final BigDecimal PERFECT_UTILITY = BigDecimal.valueOf(1);
    private static final BigDecimal AVERAGE_UTILITY = BigDecimal.valueOf(0.5);
    private static final BigDecimal ALMOST_PERFECT_UTILITY = new BigDecimal("0.99985");

    @Mock
    private Candidate candidate1;
    @Mock
    private Candidate candidate2;
    @Mock
    private Candidate candidate3;
    @Mock
    private Candidate candidate4;
    @Mock
    private Candidate candidate5;
    @Mock
    private Ballot ballot;
    @Mock
    private VoteStrategy voteStrategy;

    @Mock
    private PoliticalPosition politicalPosition;

    @Before
    public void setUp() {
        when(politicalPosition.utility(candidate1)).thenReturn(FAIRLY_LOW_UTILITY);
        when(politicalPosition.utility(candidate2)).thenReturn(NULL_UTILITY);
        when(politicalPosition.utility(candidate3)).thenReturn(AVERAGE_UTILITY);
        when(politicalPosition.utility(candidate4)).thenReturn(ALMOST_PERFECT_UTILITY);
        when(politicalPosition.utility(candidate5)).thenReturn(PERFECT_UTILITY);
        when(voteStrategy.getBestCandidate(any(), any())).thenReturn(candidate1);
        when(voteStrategy.getWorstCandidate(any(), any())).thenReturn(candidate1);
        when(voteStrategy.getListOfCandidateWithScore(any(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());
    }

    @Test
    public void findClosestCandidate_ShouldReturnTheCandidateWithHighestUtility() {
        SimpleVoter sv = new SimpleVoter(politicalPosition);

        Set<Candidate> candidates = new HashSet<>();
        candidates.add(candidate1);
        candidates.add(candidate2);
        candidates.add(candidate5);

        assertThat(sv.findClosestCandidate(candidates)).isEqualTo(candidate5);
    }

    @Test(expected = RuntimeException.class)
    public void findClosestCandidate_ShouldThrowAnExceptionIfNoCandidates() {
        SimpleVoter sv = new SimpleVoter(politicalPosition);

        Set<Candidate> candidates = new HashSet<>();

        sv.findClosestCandidate(candidates);
    }

    @Test
    public void isIgnorant_ShouldReturnWhateverTheVoterIsIgnorant() {
        SimpleVoter sv = new SimpleVoter(politicalPosition, true, voteStrategy);

        assertTrue(sv.isIgnorant());

        sv = new SimpleVoter(politicalPosition, false, voteStrategy);

        assertFalse(sv.isIgnorant());
    }

    @Test
    public void vote_ShouldReturnTheBallotFromBallotBuilder() {
        SimpleVoter sv = new SimpleVoter(politicalPosition);

        BallotBuilder bb = new BallotBuilder() {
            @Override
            public Ballot buildFor(final BallotVisitor visitor) {
                return ballot;
            }
        };

        assertThat(sv.vote(bb)).isSameAs(ballot);
    }

    @Test
    public void vote_ShouldHaveADefaultVisitorFindingTheBestPossibleResult() {
        SimpleVoter sv = new SimpleVoter(politicalPosition, false, voteStrategy);

        Set<Candidate> candidates = new HashSet<>();
        candidates.add(candidate1);
        candidates.add(candidate3);
        candidates.add(candidate2);

        assertThat(sv.vote(v -> v.visitPlurality(candidates))).isNotNull();

        verify(voteStrategy).getBestCandidate(eq(sv), any());

        assertThat(sv.vote(v -> v.visitAntiPlurality(candidates))).isNotNull();

        verify(voteStrategy).getWorstCandidate(eq(sv), any());

        assertThat(sv.vote(v -> v.visitRangeValue(candidates, 0, 2))).isNotNull();

        verify(voteStrategy).getListOfCandidateWithScore(eq(sv), any(), eq(0), eq(2));
    }

    @Test
    public void sortCandidate_ShouldSortTheCandidateByUtility() {
        SimpleVoter sv = new SimpleVoter(politicalPosition, false, voteStrategy);

        Set<Candidate> candidates = new HashSet<>();
        candidates.add(candidate1);
        candidates.add(candidate3);
        candidates.add(candidate2);

        assertThat(sv.sortCandidates(candidates)).containsExactly(candidate3, candidate1,
                candidate2);
    }

    @Test
    public void sortCandidate_ShouldSortTheCandidateByUtility2() {
        SimpleVoter sv = new SimpleVoter(politicalPosition, false, voteStrategy);

        Set<Candidate> candidates = new HashSet<>();
        candidates.add(candidate1);
        candidates.add(candidate2);
        candidates.add(candidate3);
        candidates.add(candidate4);
        candidates.add(candidate5);

        assertThat(sv.sortCandidates(candidates)).containsExactly(candidate5, candidate4,
                candidate3, candidate1, candidate2);
    }
}
