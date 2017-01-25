package fr.dhel.voting.model.system.ballot;

import fr.dhel.voting.model.entity.Voter;

@FunctionalInterface
public interface BallotBuilder {
	Ballot buildFor(final Voter voter);
}
