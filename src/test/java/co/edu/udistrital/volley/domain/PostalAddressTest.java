package co.edu.udistrital.volley.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.edu.udistrital.volley.web.rest.TestUtil;

public class PostalAddressTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalAddress.class);
        PostalAddress postalAddress1 = new PostalAddress();
        postalAddress1.setId(1L);
        PostalAddress postalAddress2 = new PostalAddress();
        postalAddress2.setId(postalAddress1.getId());
        assertThat(postalAddress1).isEqualTo(postalAddress2);
        postalAddress2.setId(2L);
        assertThat(postalAddress1).isNotEqualTo(postalAddress2);
        postalAddress1.setId(null);
        assertThat(postalAddress1).isNotEqualTo(postalAddress2);
    }
}
