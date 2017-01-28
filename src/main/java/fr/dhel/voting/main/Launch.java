package fr.dhel.voting.main;

import static java.util.stream.Collectors.reducing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import lombok.val;
import fr.dhel.voting.model.entity.candidate.CandidateFactory;
import fr.dhel.voting.model.entity.politicalpos.Issues;
import fr.dhel.voting.model.entity.politicalpos.IssuesBasedPoliticalPosition;
import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;
import fr.dhel.voting.model.entity.voter.SimpleVoter;
import fr.dhel.voting.model.entity.voter.Voter;
import fr.dhel.voting.model.env.VoteEnvironment;
import fr.dhel.voting.model.system.AntiPluralitySystem;
import fr.dhel.voting.model.system.PluralitySystem;
import fr.dhel.voting.model.system.RandomBallotSystem;
import fr.dhel.voting.model.system.RangeValueSystem;
import fr.dhel.voting.model.system.VotingSystem;

/**
 * Classe principale servant de point d'entrée du programme.
 * 
 * @author Ronan
 *
 */
public class Launch {
	
	//===================================================================
	// METHODES
	//===================================================================
	
	/**
	 * Fonction principale.
	 * 
	 * @param args les arguments
	 */
	public static void main(final String args[]) {
		final int numberOfCandidate = 12;
		final int numberOfVoter = 12000;
		
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle");
		
		
		Random r = new Random();
		Supplier<Double> numbersGenerator = () -> r.nextDouble() * Issues.MAX_VALUE;

		final RandomBallotSystem randomBallotSystem = new RandomBallotSystem(r);
		final PluralitySystem plurality = new PluralitySystem();
		final RangeValueSystem rangeVote = new RangeValueSystem(0, 2);
		final RangeValueSystem bigRangeVote = new RangeValueSystem(0, 5);
		final AntiPluralitySystem antiPlurality = new AntiPluralitySystem();
		
		List<VotingSystem> availableSystems = Arrays.asList(randomBallotSystem, plurality, rangeVote, bigRangeVote, antiPlurality);
		
		val candidates = CandidateFactory.createMultipleCandidate(numberOfCandidate, 2, numbersGenerator);
		
		List<Voter> voters = new ArrayList<>();
		
		for (int i = 0 ; i < numberOfVoter; i++) {
			PoliticalPosition a = new IssuesBasedPoliticalPosition(new Issues(
					numbersGenerator.get(), numbersGenerator.get()));
			voters.add(new SimpleVoter(a));
		}
		
		for (VotingSystem votingSystem : availableSystems) {
			
			final VoteEnvironment env = VoteEnvironment.builder().votingSystem(votingSystem)
					.candidateSet(candidates)
					.voters(voters).build();

			val electedCandidate = env.electWinner();
			val summedUtility = voters.stream().map(v -> v.utility(electedCandidate)).collect(reducing(BigDecimal::add)).get();
			
			System.out.println(String.format("%-5s %s\tsummedUtility = %s    :=> %s", votingSystem.shortName(),
					messages.getString(votingSystem.fullName()), summedUtility, electedCandidate));
		}
		
	}
}
