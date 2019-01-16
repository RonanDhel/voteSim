package fr.dhel.voting.model.system;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import fr.dhel.voting.model.entity.candidate.Candidate;
import fr.dhel.voting.model.env.ElectionResult;
import fr.dhel.voting.model.system.ballot.Ballot;
import fr.dhel.voting.model.system.ballot.BallotBuilder;

/**
 * Représente le <i>scrutin uninominal à 4 tour</i>.
 * <p>
 * Il faut voter pour une personne. <br />
 * Si aucun candidat n'obtient la majorité au premier tour, un second tour est
 * organisé en éliminant tout les candidats ayant moins de 8% des votes. Si cela
 * provoque tellement d'élimination qu'il n'y a plus que 3 candidats ou moins,
 * on passe directement au troisième tour. <br />
 * Si aucun candidat n'obtient la majorité au second tour, un tour est organisé
 * avec les 3 meilleurs candidats. <br />
 * Si aucun candidat n'obtient la majorité lors du troisième tour, un dernier
 * tour est organisé avec les 2 meilleurs candidats. Ce tour est gagné par le
 * candidat ayant le plus de voix. En cas d'égalité lors de ce tour, le gagnant
 * est tiré au sort.
 * <p>
 * Ce système se comporte comme ceci :
 * <ul>
 * <li>si un candidat remporterait la majorité des voix, il doit gagner : oui
 * </li>
 * <li>critère de participation (ajouter un bulletin où le candidat A est
 * préféré ne doit pas changer le résultat du candidat A vers le candidat B) :
 * oui</li>
 * <li>gagnant de Condorcet : non</li>
 * <li>perdant de Condorcet : oui</li>
 * </ul>
 * 
 * @author Ronan
 *
 */
public class FourRoundPluralitySystem implements VotingSystem {

    private static final String FULL_NAME = "Plurality-4-turn";

    // ===================================================================
    // METHODES
    // ===================================================================

    @Override
    public String shortName() {
        return "PL4";
    }

    @Override
    public String fullName() {
        return FULL_NAME;
    }

    @Override
    public boolean isPluralityType() {
        return true;
    }

    @Override
    public BallotBuilder createBallot(final Set<Candidate> candidateSet) {
        return v -> v.visitPlurality(candidateSet);
    }

    @Override
    public ElectionResult countVotes(final List<Ballot> votes, final Set<Candidate> candidateSet) {
        Map<Candidate, Double> scorePerCandidate = new HashMap<>();

        for (Ballot vote : votes) {
            Candidate c = vote.computeResults().get(0).getKey();
            final double value = vote.computeResults().get(0).getValue();

            scorePerCandidate.merge(c, value, (oldValue, newValue) -> oldValue + newValue);
        }

        var sortedResult = scorePerCandidate.entrySet().stream()
                .sorted((firstEntry, secondEntry) -> Double.compare(secondEntry.getValue(),
                        firstEntry.getValue()))
                .collect(toList());

        var bestCandidateWithScore = sortedResult.get(0);

        int numberOfVoteToBeElected = votes.size() / 2;
        if (bestCandidateWithScore.getValue() > numberOfVoteToBeElected) {
            return new ElectionResult(bestCandidateWithScore.getKey());
        }
        // il n'y a que 2 candidats et ils ont fait égalité (sinon on aurait validé le
        // if précédent)
        if (sortedResult.size() == 2) {
            Candidate candidateAtRandom = sortedResult.get(ThreadLocalRandom.current().nextInt(2))
                    .getKey();
            return new ElectionResult(candidateAtRandom);
        } else if (sortedResult.size() == 3) {
            // trois candidats donc on est au 3eme tour
            var bestTwo = new HashSet<Candidate>();
            bestTwo.add(sortedResult.get(0).getKey());
            bestTwo.add(sortedResult.get(1).getKey());
            return ElectionResult.electionWithNewRound(bestTwo);
        }

        final int numberOfVotes = votes.size();
        Set<Candidate> newCandidateSet = scorePerCandidate.entrySet().stream()
                .filter(entry -> (entry.getValue() * 100 / numberOfVotes) > 8)
                .map(entry -> entry.getKey()).collect(toSet());
        if (newCandidateSet.size() < 3 || newCandidateSet.size() == scorePerCandidate.size()) {
            // 2 candidats ou moins ont réussi à atteindre les 8% OU tous valide
            // cette condition donc on passe au 3eme tour et on prends les 3 meilleurs
            var bestThree = sortedResult.stream().map(entry -> entry.getKey()).limit(3)
                    .collect(toSet());
            return ElectionResult.electionWithNewRound(bestThree);
        }

        return ElectionResult.electionWithNewRound(newCandidateSet);
    }

}
