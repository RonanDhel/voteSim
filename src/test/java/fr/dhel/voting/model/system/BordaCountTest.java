package fr.dhel.voting.model.system;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BordaCountTest {
	
	private BordaCount bc = new BordaCount();
	
	@Test
	public void shortName_ShouldReturnTheName() {
		assertThat(bc.shortName(), is(equalTo("BC")));
	}
}
