package fr.dhel.voting.model.system;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class BordaCountTest {

    private BordaCount bc = new BordaCount();

    @Test
    public void shortName_ShouldReturnTheName() {
        assertThat(bc.shortName()).isEqualTo("BC");
    }
}
