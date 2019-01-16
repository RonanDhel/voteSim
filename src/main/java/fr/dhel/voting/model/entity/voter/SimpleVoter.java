package fr.dhel.voting.model.entity.voter;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;
import fr.dhel.voting.model.system.ballot.RankedBallot;
import fr.dhel.voting.model.system.ballot.UninominalBallot;
import fr.dhel.voting.model.system.ballot.ValuedBallot;
import lombok.Getter;
import lombok.ToString;

@ToString
public class SimpleVoter implements Voter {
    @Getter
    private final PoliticalPosition politicalPosition;
    private final boolean ignorant;
    private final VoteStrategy voteStrategy;

    public SimpleVoter(final PoliticalPosition politicalPosition) {
        this(politicalPosition, false, new DefaultVoteStrategy());
    }

    public SimpleVoter(
            final PoliticalPosition politicalPosition, final boolean ignorant,
            final VoteStrategy voteStrategy) {
        this.politicalPosition = Objects.requireNonNull(politicalPosition,
                "politicalPosition should not be null");
        this.ignorant = ignorant;
        this.voteStrategy = Objects.requireNonNull(voteStrategy, "voteStrategy should not be null");
    }

    // ===================================================================
    // METHODES
    // ===================================================================

    @Override
    public final BigDecimal utility(final Candidate c) {
        return getPoliticalPosition().utility(c);
    }

    @Override
    public BigDecimal trueUtility(final Candidate c) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final boolean isIgnorant() {
        return ignorant;
    }

    @Override
    public final Candidate findClosestCandidate(final Set<Candidate> candidateSet) {
        Candidate result = null;
        BigDecimal currentUtility = null;
        for (var c : candidateSet) {
            var newUtility = utility(c);
            if (currentUtility == null || currentUtility.compareTo(newUtility) < 0) {
                currentUtility = newUtility;
                result = c;
            }
        }

        if (result == null)
            throw new NoSuchElementException();
        return result;
    }

    @Override
    public final Ballot vote(final BallotBuilder builder) {
        var voter = this;
        return builder.buildFor(new BallotVisitor() {

            @Override
            public UninominalBallot visitPlurality(final Set<Candidate> candidateSet) {
                return new UninominalBallot(voteStrategy.getBestCandidate(voter, candidateSet));
            }

            @Override
            public UninominalBallot visitAntiPlurality(final Set<Candidate> candidateSet) {
                return new UninominalBallot(voteStrategy.getWorstCandidate(voter, candidateSet));
            }

            @Override
            public RankedBallot visitRankedBallot(final Set<Candidate> candidateSet) {
                return new RankedBallot(candidateSet.stream()
                        .sorted(comparing(SimpleVoter.this::utility).reversed()).collect(toList()));
            }

            @Override
            public ValuedBallot visitRangeValue(
                    final Set<Candidate> candidateSet, final int minRange, final int maxRange) {
                return new ValuedBallot(voteStrategy.getListOfCandidateWithScore(voter,
                        candidateSet, minRange, maxRange));
            }

            @Override
            public ValuedBallot visitMajorityJudgment(final Set<Candidate> candidateSet) {
                return new ValuedBallot(
                        voteStrategy.getListOfCandidateWithScore(voter, candidateSet, 0, 4));
            }

        });
    }

    @Override
    public final List<Candidate> sortCandidates(final Set<Candidate> candidateSet) {
        return unmodifiableList(candidateSet.stream()
                .sorted((c1, c2) -> utility(c2).compareTo(utility(c1))).collect(toList()));
    }
}
