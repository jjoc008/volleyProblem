package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.VolleyApp;
import co.edu.udistrital.volley.domain.MemberCard;
import co.edu.udistrital.volley.repository.MemberCardRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MemberCardResource} REST controller.
 */
@SpringBootTest(classes = VolleyApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MemberCardResourceIT {

    private static final Integer DEFAULT_MEMBER_IDENTIFICATION = 1;
    private static final Integer UPDATED_MEMBER_IDENTIFICATION = 2;

    private static final String DEFAULT_MEMBER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTH_DATE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENCEMENT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENCEMENT_DATE = "BBBBBBBBBB";

    @Autowired
    private MemberCardRepository memberCardRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberCardMockMvc;

    private MemberCard memberCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberCard createEntity(EntityManager em) {
        MemberCard memberCard = new MemberCard()
            .memberIdentification(DEFAULT_MEMBER_IDENTIFICATION)
            .memberName(DEFAULT_MEMBER_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE);
        return memberCard;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberCard createUpdatedEntity(EntityManager em) {
        MemberCard memberCard = new MemberCard()
            .memberIdentification(UPDATED_MEMBER_IDENTIFICATION)
            .memberName(UPDATED_MEMBER_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE);
        return memberCard;
    }

    @BeforeEach
    public void initTest() {
        memberCard = createEntity(em);
    }

    @Test
    @Transactional
    public void createMemberCard() throws Exception {
        int databaseSizeBeforeCreate = memberCardRepository.findAll().size();

        // Create the MemberCard
        restMemberCardMockMvc.perform(post("/api/member-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberCard)))
            .andExpect(status().isCreated());

        // Validate the MemberCard in the database
        List<MemberCard> memberCardList = memberCardRepository.findAll();
        assertThat(memberCardList).hasSize(databaseSizeBeforeCreate + 1);
        MemberCard testMemberCard = memberCardList.get(memberCardList.size() - 1);
        assertThat(testMemberCard.getMemberIdentification()).isEqualTo(DEFAULT_MEMBER_IDENTIFICATION);
        assertThat(testMemberCard.getMemberName()).isEqualTo(DEFAULT_MEMBER_NAME);
        assertThat(testMemberCard.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testMemberCard.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    public void createMemberCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberCardRepository.findAll().size();

        // Create the MemberCard with an existing ID
        memberCard.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberCardMockMvc.perform(post("/api/member-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberCard)))
            .andExpect(status().isBadRequest());

        // Validate the MemberCard in the database
        List<MemberCard> memberCardList = memberCardRepository.findAll();
        assertThat(memberCardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMemberCards() throws Exception {
        // Initialize the database
        memberCardRepository.saveAndFlush(memberCard);

        // Get all the memberCardList
        restMemberCardMockMvc.perform(get("/api/member-cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].memberIdentification").value(hasItem(DEFAULT_MEMBER_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].memberName").value(hasItem(DEFAULT_MEMBER_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE)));
    }
    
    @Test
    @Transactional
    public void getMemberCard() throws Exception {
        // Initialize the database
        memberCardRepository.saveAndFlush(memberCard);

        // Get the memberCard
        restMemberCardMockMvc.perform(get("/api/member-cards/{id}", memberCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memberCard.getId().intValue()))
            .andExpect(jsonPath("$.memberIdentification").value(DEFAULT_MEMBER_IDENTIFICATION))
            .andExpect(jsonPath("$.memberName").value(DEFAULT_MEMBER_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE));
    }

    @Test
    @Transactional
    public void getNonExistingMemberCard() throws Exception {
        // Get the memberCard
        restMemberCardMockMvc.perform(get("/api/member-cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMemberCard() throws Exception {
        // Initialize the database
        memberCardRepository.saveAndFlush(memberCard);

        int databaseSizeBeforeUpdate = memberCardRepository.findAll().size();

        // Update the memberCard
        MemberCard updatedMemberCard = memberCardRepository.findById(memberCard.getId()).get();
        // Disconnect from session so that the updates on updatedMemberCard are not directly saved in db
        em.detach(updatedMemberCard);
        updatedMemberCard
            .memberIdentification(UPDATED_MEMBER_IDENTIFICATION)
            .memberName(UPDATED_MEMBER_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE);

        restMemberCardMockMvc.perform(put("/api/member-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMemberCard)))
            .andExpect(status().isOk());

        // Validate the MemberCard in the database
        List<MemberCard> memberCardList = memberCardRepository.findAll();
        assertThat(memberCardList).hasSize(databaseSizeBeforeUpdate);
        MemberCard testMemberCard = memberCardList.get(memberCardList.size() - 1);
        assertThat(testMemberCard.getMemberIdentification()).isEqualTo(UPDATED_MEMBER_IDENTIFICATION);
        assertThat(testMemberCard.getMemberName()).isEqualTo(UPDATED_MEMBER_NAME);
        assertThat(testMemberCard.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testMemberCard.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMemberCard() throws Exception {
        int databaseSizeBeforeUpdate = memberCardRepository.findAll().size();

        // Create the MemberCard

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberCardMockMvc.perform(put("/api/member-cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberCard)))
            .andExpect(status().isBadRequest());

        // Validate the MemberCard in the database
        List<MemberCard> memberCardList = memberCardRepository.findAll();
        assertThat(memberCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMemberCard() throws Exception {
        // Initialize the database
        memberCardRepository.saveAndFlush(memberCard);

        int databaseSizeBeforeDelete = memberCardRepository.findAll().size();

        // Delete the memberCard
        restMemberCardMockMvc.perform(delete("/api/member-cards/{id}", memberCard.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemberCard> memberCardList = memberCardRepository.findAll();
        assertThat(memberCardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
