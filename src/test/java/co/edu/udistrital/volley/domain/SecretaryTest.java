package co.edu.udistrital.volley.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.edu.udistrital.volley.web.rest.TestUtil;

public class SecretaryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Secretary.class);
        Secretary secretary1 = new Secretary();
        secretary1.setId(1L);
        Secretary secretary2 = new Secretary();
        secretary2.setId(secretary1.getId());
        assertThat(secretary1).isEqualTo(secretary2);
        secretary2.setId(2L);
        assertThat(secretary1).isNotEqualTo(secretary2);
        secretary1.setId(null);
        assertThat(secretary1).isNotEqualTo(secretary2);
    }
}
