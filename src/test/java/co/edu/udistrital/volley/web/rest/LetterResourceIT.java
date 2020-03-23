package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.VolleyApp;
import co.edu.udistrital.volley.domain.Letter;
import co.edu.udistrital.volley.repository.LetterRepository;

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
 * Integration tests for the {@link LetterResource} REST controller.
 */
@SpringBootTest(classes = VolleyApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class LetterResourceIT {

    private static final String DEFAULT_SUR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTH_DATE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private LetterRepository letterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLetterMockMvc;

    private Letter letter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Letter createEntity(EntityManager em) {
        Letter letter = new Letter()
            .surName(DEFAULT_SUR_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .sex(DEFAULT_SEX)
            .phone(DEFAULT_PHONE);
        return letter;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Letter createUpdatedEntity(EntityManager em) {
        Letter letter = new Letter()
            .surName(UPDATED_SUR_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .sex(UPDATED_SEX)
            .phone(UPDATED_PHONE);
        return letter;
    }

    @BeforeEach
    public void initTest() {
        letter = createEntity(em);
    }

    @Test
    @Transactional
    public void createLetter() throws Exception {
        int databaseSizeBeforeCreate = letterRepository.findAll().size();

        // Create the Letter
        restLetterMockMvc.perform(post("/api/letters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letter)))
            .andExpect(status().isCreated());

        // Validate the Letter in the database
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeCreate + 1);
        Letter testLetter = letterList.get(letterList.size() - 1);
        assertThat(testLetter.getSurName()).isEqualTo(DEFAULT_SUR_NAME);
        assertThat(testLetter.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testLetter.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testLetter.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testLetter.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createLetterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = letterRepository.findAll().size();

        // Create the Letter with an existing ID
        letter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLetterMockMvc.perform(post("/api/letters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letter)))
            .andExpect(status().isBadRequest());

        // Validate the Letter in the database
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLetters() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get all the letterList
        restLetterMockMvc.perform(get("/api/letters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(letter.getId().intValue())))
            .andExpect(jsonPath("$.[*].surName").value(hasItem(DEFAULT_SUR_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }
    
    @Test
    @Transactional
    public void getLetter() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        // Get the letter
        restLetterMockMvc.perform(get("/api/letters/{id}", letter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(letter.getId().intValue()))
            .andExpect(jsonPath("$.surName").value(DEFAULT_SUR_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    public void getNonExistingLetter() throws Exception {
        // Get the letter
        restLetterMockMvc.perform(get("/api/letters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLetter() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        int databaseSizeBeforeUpdate = letterRepository.findAll().size();

        // Update the letter
        Letter updatedLetter = letterRepository.findById(letter.getId()).get();
        // Disconnect from session so that the updates on updatedLetter are not directly saved in db
        em.detach(updatedLetter);
        updatedLetter
            .surName(UPDATED_SUR_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .sex(UPDATED_SEX)
            .phone(UPDATED_PHONE);

        restLetterMockMvc.perform(put("/api/letters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLetter)))
            .andExpect(status().isOk());

        // Validate the Letter in the database
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeUpdate);
        Letter testLetter = letterList.get(letterList.size() - 1);
        assertThat(testLetter.getSurName()).isEqualTo(UPDATED_SUR_NAME);
        assertThat(testLetter.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testLetter.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testLetter.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testLetter.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingLetter() throws Exception {
        int databaseSizeBeforeUpdate = letterRepository.findAll().size();

        // Create the Letter

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLetterMockMvc.perform(put("/api/letters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(letter)))
            .andExpect(status().isBadRequest());

        // Validate the Letter in the database
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLetter() throws Exception {
        // Initialize the database
        letterRepository.saveAndFlush(letter);

        int databaseSizeBeforeDelete = letterRepository.findAll().size();

        // Delete the letter
        restLetterMockMvc.perform(delete("/api/letters/{id}", letter.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Letter> letterList = letterRepository.findAll();
        assertThat(letterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
