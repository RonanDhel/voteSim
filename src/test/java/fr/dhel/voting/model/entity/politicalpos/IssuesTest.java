package fr.dhel.voting.model.entity.politicalpos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class IssuesTest {

    private static final BigDecimal DELTA = new BigDecimal("0.001");

    @Test
    public void constructor_ShouldNotThrowIfValueIsAllowed() {
        new Issues(Collections.singleton(2.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_ShouldAcceptSomeValueAndRefuseTooBig() {
        try {
            new Issues(Collections.singleton(5.0));
        } catch (Exception e) {
            fail("Should not have thrown with a value of 5.0");
        }
        new Issues(Arrays.asList(2.0, 3.0, 17.0));
    }

    @Test
    public void numberOfIssues_ShouldReturnTheSize() {
        assertThat(new Issues(1.0, 2.0, 3.0).numberOfIssues()).isEqualTo(3);

        assertThat(new Issues(1.17).numberOfIssues()).isEqualTo(1);

        assertThat(new Issues(0.0, 0.5, 1.25, 1.75, 2.5, 3.33, 3.9).numberOfIssues()).isEqualTo(7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDistanceFrom_ShouldFailIfComparingTwoObjectWithDifferentNumberOfIssues() {
        Issues oneItem = new Issues(2.0);
        Issues bigIssues = new Issues(0.3, 0.6, 1.2, 1.8, 2.4, 3.0);

        oneItem.getDistanceFrom(bigIssues);
    }

    @Test
    public void getDistanceFrom_ShouldReturnZeroWhenComparedWithItself() {
        Issues i = new Issues(2.0);

        assertThat(i.getDistanceFrom(i)).isCloseTo(BigDecimal.ZERO, byLessThan(DELTA));
    }

    @Test
    public void getDistanceFrom_ShouldReturnSomeMidrangeValue() {
        Issues i1 = new Issues(1.0, 2.0, 5.0);
        Issues i2 = new Issues(0.87, 2.0, 2.999);

        var expected = BigDecimal.valueOf(4.02);

        assertThat(i1.getDistanceFrom(i2)).isCloseTo(expected, byLessThan(DELTA));
    }

    @Test
    public void getDistanceFrom_ShouldBeCommutative() {
        Random r = new Random();

        for (int i = 0; i < 250_000; i++) {
            Issues i1 = new Issues(r.nextDouble() * 3);
            Issues i2 = new Issues(r.nextDouble() * 3);

            assertThat(i1.getDistanceFrom(i2)).isEqualTo(i2.getDistanceFrom(i1));
        }
    }

    @Test
    public void getDistanceFrom_ShouldReturnABigNumberWhenIssuesAreCompletelyOpposite() {
        Issues i1 = new Issues(5.0, 5.0);
        Issues i2 = new Issues(0.0, 0.0);

        assertThat(i1.getDistanceFrom(i2)).isCloseTo(BigDecimal.valueOf(50), byLessThan(DELTA));
    }

    @Test
    public void equals_ShouldWork() {
        EqualsVerifier.forClass(Issues.class)
                .suppress(Warning.NULL_FIELDS, Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }
}
