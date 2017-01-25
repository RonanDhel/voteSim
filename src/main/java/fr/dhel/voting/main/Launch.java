package fr.dhel.voting.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import lombok.val;
import fr.dhel.voting.model.entity.CandidateFactory;
import fr.dhel.voting.model.entity.SimpleVoter;
import fr.dhel.voting.model.entity.Voter;
import fr.dhel.voting.model.entity.politicalpos.Issues;
import fr.dhel.voting.model.entity.politicalpos.IssuesBasedPoliticalPosition;
import fr.dhel.voting.model.entity.politicalpos.PoliticalPosition;
import fr.dhel.voting.model.env.VoteEnvironment;
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

	/*
	1) Vote par valeur (0 à 2) - Range voting - RV
	2) Vote par valeur (0 à 5) - Range voting - RV
	3) Jugement Majoritaire - Majority Judgment - MJ
	2) Vote uninominal à 1 tour - First past the post - plurality - PL1
	3) Vote uninominal à 2 tours - 2-round plurality - PL2
	4) Vote uninominal à 4 tours - 4-round plurality - PL4
	5) Méthode Borda - Borda count - BC
	6) Anti-plurality - BU
	7) Electeur au hasard choisi le gagnant - Random Ballot - RB
	8) Candidat au hasard - Random Winner - RW
	9) Candidat avec la pire utilité choisi arbitrairement - Worst-summed-utility WInner - WW
	10) Coombs - Coombs - BC
	11) Bucklin - Bucklin
	12) Copeland
	13) Vote de Dabagh - Dabagh
	14) Black
	15) Méthode de Condorcet
	16) Vote alternatif - Instant Run-off voting - IRV
	17) Schulze - Schulze
	 */
	
	//===================================================================
	// METHODES
	//===================================================================
	
	/**
	 * Fonction principale.
	 * 
	 * @param args les arguments
	 */
	public static void main(final String args[]) {
		final int numberOfCandidate = 5;
		final int numberOfVoter = 1200;
		
		ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle");
		
		
		Random r = new Random();
		Supplier<Double> numbersGenerator = () -> r.nextDouble() * Issues.MAX_VALUE;

		final RandomBallotSystem randomBallotSystem = new RandomBallotSystem(r);
		final PluralitySystem plurality = new PluralitySystem();
		final RangeValueSystem rangeVote = new RangeValueSystem(0, 2);
		final RangeValueSystem bigRangeVote = new RangeValueSystem(0, 5);
		
		
		List<VotingSystem> availableSystems = Arrays.asList(randomBallotSystem, plurality, rangeVote, bigRangeVote);
		
		IssuesBasedPoliticalPosition ibpp = new IssuesBasedPoliticalPosition(new Issues(1.0, 2.0));
		IssuesBasedPoliticalPosition ibpp2 = new IssuesBasedPoliticalPosition(new Issues(5.0, 0.0));
		
		val candidates = CandidateFactory.createMultipleCandidate(numberOfCandidate, 2, numbersGenerator);
		
		for (VotingSystem votingSystem : availableSystems) {
			List<Voter> voters = new ArrayList<>();
			
			for (int i = 0 ; i < numberOfVoter; i++) {
				PoliticalPosition a;
				switch (r.nextInt(3)) {
					case 0:
						a = ibpp;
						break;
					case 1:
						a = ibpp2;
						break;
					default:
						a = new IssuesBasedPoliticalPosition(new Issues(numbersGenerator.get(), 4.0));
				}
				voters.add(new SimpleVoter(a));
			}
			
			
			final VoteEnvironment env = VoteEnvironment.builder().votingSystem(votingSystem)
					.candidateSet(candidates)
					.voters(voters).build();

			System.out.println(String.format("%-5s %s :=> %s", votingSystem.shortName(),
					messages.getString(votingSystem.fullName()), env.electWinner()));
		}
		
	}
}
