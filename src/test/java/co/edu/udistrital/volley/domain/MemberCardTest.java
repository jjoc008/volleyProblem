package co.edu.udistrital.volley.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import co.edu.udistrital.volley.web.rest.TestUtil;

public class MemberCardTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberCard.class);
        MemberCard memberCard1 = new MemberCard();
        memberCard1.setId(1L);
        MemberCard memberCard2 = new MemberCard();
        memberCard2.setId(memberCard1.getId());
        assertThat(memberCard1).isEqualTo(memberCard2);
        memberCard2.setId(2L);
        assertThat(memberCard1).isNotEqualTo(memberCard2);
        memberCard1.setId(null);
        assertThat(memberCard1).isNotEqualTo(memberCard2);
    }
}
