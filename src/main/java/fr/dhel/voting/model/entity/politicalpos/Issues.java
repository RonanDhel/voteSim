package fr.dhel.voting.model.entity.politicalpos;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import lombok.experimental.ExtensionMethod;

/**
 * Liste de points évalués sur l'échelle des réelles (positifs seulements). Ces
 * points permettent de simuler un ensemble d'interrogations/sujets et de placer
 * les {@link fr.dhel.voting.model.entity.candidate.Candidate} et les
 * {@link fr.dhel.voting.model.entity.voter.Voter} sur l'échiquier politique de
 * manière plus fine qu'un schéma gauche/droite classique.
 * <p>
 * Ces listes peuvent être comparée entre elles en calculant leur distance, une
 * distance de 0 indiquant que les deux listes sont exactement pareilles.
 * <p>
 * Par exemple, si un ensemble de 3 sujets est évalué par une personne {0, 0.5,
 * 2} et qu'il y a 2 candidats {0, 1, 1} et {1, 2, 0}, le candidat qui sera le
 * mieux évaluée sera le premier.
 * 
 * @author Ronan
 *
 */
@ExtensionMethod(java.util.Arrays.class)
public class Issues implements Iterable<Double> {
    public static final double MAX_VALUE = 5;

    private final List<Double> opinionOnIssues;
    private String stringCache;

    public Issues(final List<Double> issues) {
        if (issues.isEmpty())
            throw new IllegalArgumentException("Should have at least one issues");

        if (issues.stream().anyMatch(e -> e < 0 || e > MAX_VALUE))
            throw new IllegalArgumentException(
                    "List of issues should all be between 0 and " + MAX_VALUE);

        opinionOnIssues = issues;
    }

    public Issues(final Iterable<Double> issues) {
        this(StreamSupport.stream(issues.spliterator(), false).collect(toList()));
    }

    public Issues(final double firstIssue, final Double... othersIssues) {
        this(constructorHelper(firstIssue, othersIssues));
    }

    // ===================================================================
    // METHODES
    // ===================================================================

    private static List<Double> constructorHelper(
            final double firstIssue, final Double... othersIssues) {
        List<Double> a = new ArrayList<>();
        a.add(firstIssue);
        a.addAll(Arrays.asList(othersIssues));
        return a;
    }

    @Override
    public final Iterator<Double> iterator() {
        return opinionOnIssues.iterator();
    }

    /**
     * @return le nombre de points en traitement
     */
    public final int numberOfIssues() {
        return opinionOnIssues.size();
    }

    public final BigDecimal getDistanceFrom(final Issues other) {
        final int size = opinionOnIssues.size();
        if (other.numberOfIssues() != size)
            throw new IllegalArgumentException(
                    "You can't use getDistanceFrom if the number of issues of the argument ("
                            + other.numberOfIssues()
                            + ") is different from the current object number of issues");

        BigDecimal result = BigDecimal.ZERO;

        var otherIssues = other.opinionOnIssues;

        for (int i = 0; i < size; i++) {
            result = result
                    .add(BigDecimal.valueOf(otherIssues.get(i) - opinionOnIssues.get(i)).pow(2));
        }

        return result;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(opinionOnIssues);
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof Issues) {
            Issues other = (Issues) obj;
            return opinionOnIssues.equals(other.opinionOnIssues);
        }
        return false;
    }

    @Override
    public String toString() {
        if (stringCache == null) {
            stringCache = "Issues : "
                    + String.join(", ",
                            opinionOnIssues.stream().map(e -> e.toString()).collect(toList()));
        }
        return stringCache;
    }
}
