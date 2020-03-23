package co.edu.udistrital.volley.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.edu.udistrital.volley.web.rest.TestUtil;

public class RegistryBookTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegistryBook.class);
        RegistryBook registryBook1 = new RegistryBook();
        registryBook1.setId(1L);
        RegistryBook registryBook2 = new RegistryBook();
        registryBook2.setId(registryBook1.getId());
        assertThat(registryBook1).isEqualTo(registryBook2);
        registryBook2.setId(2L);
        assertThat(registryBook1).isNotEqualTo(registryBook2);
        registryBook1.setId(null);
        assertThat(registryBook1).isNotEqualTo(registryBook2);
    }
}
