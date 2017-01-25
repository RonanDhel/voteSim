package fr.dhel.voting.model.entity.politicalpos;


import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import lombok.val;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.Test;

import fr.dhel.voting.model.entity.politicalpos.Issues;


public class IssuesTest {
	
	private static final BigDecimal DELTA = new BigDecimal("0.001");
	
	@Test
	public void constructor_1() {
		new Issues(Collections.singleton(2.0));
	}
	
	@SuppressWarnings("unused")
	@Test(expected=IllegalArgumentException.class)
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
		assertThat(new Issues(1.0, 2.0, 3.0).numberOfIssues(), is(equalTo(3)));
		
		assertThat(new Issues(1.17).numberOfIssues(), is(equalTo(1)));
		
		assertThat(new Issues(0.0, 0.5, 1.25, 1.75, 2.5, 3.33, 3.9).numberOfIssues(), is(equalTo(7)));
	}
	
	@Test
	public void getDistanceFrom_ShouldReturnZeroWhenComparedWithItself() {
		Issues i = new Issues(2.0);
		
		assertThat(i.getDistanceFrom(i), is(closeTo(BigDecimal.ZERO, DELTA)));
	}
	
	@Test
	public void getDistanceFrom_ShouldReturnSomeMidrangeValue() {
		Issues i1 = new Issues(1.0, 2.0, 5.0);
		Issues i2 = new Issues(0.87, 2.0, 2.999);
		
		val expected = BigDecimal.valueOf(4.02);
		
		assertThat(i1.getDistanceFrom(i2), is(closeTo(expected, DELTA)));
	}
	
	@Test
	public void getDistanceFrom_ShouldBeCommutative() {
		Random r = new Random();
		
		for (int i = 0 ; i < 250_000; i++) {
			Issues i1 = new Issues(r.nextDouble() * 3);
			Issues i2 = new Issues(r.nextDouble() * 3);
			
			assertThat(i1.getDistanceFrom(i2), is(equalTo(i2.getDistanceFrom(i1))));
		}
	}
	
	@Test
	public void getDistanceFrom_ShouldReturnABigNumberWhenIssuesAreCompletelyOpposite() {
		Issues i1 = new Issues(5.0, 5.0);
		Issues i2 = new Issues(0.0, 0.0);
		
		
		assertThat(i1.getDistanceFrom(i2), is(closeTo(BigDecimal.valueOf(50), DELTA)));
	}

	@Test
	public void equals_ShouldWork() {
		EqualsVerifier.forClass(Issues.class)
				.suppress(Warning.NULL_FIELDS, Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
	}
}
