package fr.dhel.voting.model.entity.candidate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import fr.dhel.voting.model.entity.politicalpos.Issues;
import fr.dhel.voting.model.entity.politicalpos.IssuesBasedPoliticalPosition;

public class CandidateFactory {
    private CandidateFactory() {
        // constructeur vide car classe utilitaire
    }

    // ===================================================================
    // METHODES
    // ===================================================================

    public static Candidate createCandidate(
            final int id, final String name, final int numberOfIssues,
            final Supplier<Double> numbersGenerator, final PoliticalSkill politicalSkill) {
        var issues = new ArrayList<Double>();
        for (int i = 0; i < numberOfIssues; i++) {
            issues.add(numbersGenerator.get());
        }

        var ibpp = new IssuesBasedPoliticalPosition(new Issues(issues));

        return new Candidate(id, name, ibpp, politicalSkill);
    }

    public static Candidate createCandidate(
            final int id, final String name, final int numberOfIssues,
            final Supplier<Double> numbersGenerator) {
        return createCandidate(id, name, numberOfIssues, numbersGenerator, PoliticalSkill.AVERAGE);
    }

    public static Set<Candidate> createMultipleCandidate(
            final int numberOfCandidates, final int numberOfIssues,
            final Supplier<Double> numbersGenerator) {
        if (numberOfCandidates < 1)
            throw new IllegalArgumentException("numberOfCandidates should be greater than 0");

        var result = new HashSet<Candidate>();
        for (int i = 0; i < numberOfCandidates; i++) {
            result.add(createCandidate(i, "candidate - " + i, numberOfIssues, numbersGenerator));
        }
        return result;
    }

}
