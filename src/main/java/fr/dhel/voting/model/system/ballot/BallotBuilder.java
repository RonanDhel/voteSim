package fr.dhel.voting.model.system.ballot;

import fr.dhel.voting.model.entity.voter.Voter.BallotVisitor;

@FunctionalInterface
public interface BallotBuilder {
	Ballot buildFor(final BallotVisitor ballotVisitor);
}