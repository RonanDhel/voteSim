package fr.dhel.voting.model.entity.politicalpos;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Getter;
import lombok.ToString;
import fr.dhel.voting.model.entity.candidate.Candidate;

/**
 * Représente un placement politique utilisant un ensemble de sujets, tels que
 * défini par {@link Issues}, et d'estimer à quel point un candidat est proche
 * des idées.
 * <p>
 * L'utilité d'un candidat ne pourra être calculée que si lui-même utilise un
 * mode de représentation par <code>Issues</code>.
 * 
 * @author Ronan
 *
 */
@ToString @Getter
public class IssuesBasedPoliticalPosition implements PoliticalPosition {
    private final Issues issues;

    public IssuesBasedPoliticalPosition(final Issues issues) {
        this.issues = Objects.requireNonNull(issues, "issues should not be null");
    }

    // ===================================================================
    // METHODES
    // ===================================================================

    @Override
    public BigDecimal utility(final Candidate candidate) {
        if (candidate.getPoliticalPosition() instanceof IssuesBasedPoliticalPosition) {
            var other = (IssuesBasedPoliticalPosition) candidate.getPoliticalPosition();
            var distance = issues.getDistanceFrom(other.getIssues());

            var distanceAsDouble = distance.doubleValue();

            return BigDecimal.valueOf(Math.exp(-distanceAsDouble));
        }
        return BigDecimal.ZERO;
    }

}
