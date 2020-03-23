package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.VolleyApp;
import co.edu.udistrital.volley.domain.Secretary;
import co.edu.udistrital.volley.repository.SecretaryRepository;

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
 * Integration tests for the {@link SecretaryResource} REST controller.
 */
@SpringBootTest(classes = VolleyApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SecretaryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SecretaryRepository secretaryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecretaryMockMvc;

    private Secretary secretary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Secretary createEntity(EntityManager em) {
        Secretary secretary = new Secretary()
            .name(DEFAULT_NAME);
        return secretary;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Secretary createUpdatedEntity(EntityManager em) {
        Secretary secretary = new Secretary()
            .name(UPDATED_NAME);
        return secretary;
    }

    @BeforeEach
    public void initTest() {
        secretary = createEntity(em);
    }

    @Test
    @Transactional
    public void createSecretary() throws Exception {
        int databaseSizeBeforeCreate = secretaryRepository.findAll().size();

        // Create the Secretary
        restSecretaryMockMvc.perform(post("/api/secretaries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(secretary)))
            .andExpect(status().isCreated());

        // Validate the Secretary in the database
        List<Secretary> secretaryList = secretaryRepository.findAll();
        assertThat(secretaryList).hasSize(databaseSizeBeforeCreate + 1);
        Secretary testSecretary = secretaryList.get(secretaryList.size() - 1);
        assertThat(testSecretary.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSecretaryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = secretaryRepository.findAll().size();

        // Create the Secretary with an existing ID
        secretary.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecretaryMockMvc.perform(post("/api/secretaries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(secretary)))
            .andExpect(status().isBadRequest());

        // Validate the Secretary in the database
        List<Secretary> secretaryList = secretaryRepository.findAll();
        assertThat(secretaryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSecretaries() throws Exception {
        // Initialize the database
        secretaryRepository.saveAndFlush(secretary);

        // Get all the secretaryList
        restSecretaryMockMvc.perform(get("/api/secretaries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(secretary.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSecretary() throws Exception {
        // Initialize the database
        secretaryRepository.saveAndFlush(secretary);

        // Get the secretary
        restSecretaryMockMvc.perform(get("/api/secretaries/{id}", secretary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(secretary.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingSecretary() throws Exception {
        // Get the secretary
        restSecretaryMockMvc.perform(get("/api/secretaries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSecretary() throws Exception {
        // Initialize the database
        secretaryRepository.saveAndFlush(secretary);

        int databaseSizeBeforeUpdate = secretaryRepository.findAll().size();

        // Update the secretary
        Secretary updatedSecretary = secretaryRepository.findById(secretary.getId()).get();
        // Disconnect from session so that the updates on updatedSecretary are not directly saved in db
        em.detach(updatedSecretary);
        updatedSecretary
            .name(UPDATED_NAME);

        restSecretaryMockMvc.perform(put("/api/secretaries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSecretary)))
            .andExpect(status().isOk());

        // Validate the Secretary in the database
        List<Secretary> secretaryList = secretaryRepository.findAll();
        assertThat(secretaryList).hasSize(databaseSizeBeforeUpdate);
        Secretary testSecretary = secretaryList.get(secretaryList.size() - 1);
        assertThat(testSecretary.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSecretary() throws Exception {
        int databaseSizeBeforeUpdate = secretaryRepository.findAll().size();

        // Create the Secretary

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecretaryMockMvc.perform(put("/api/secretaries")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(secretary)))
            .andExpect(status().isBadRequest());

        // Validate the Secretary in the database
        List<Secretary> secretaryList = secretaryRepository.findAll();
        assertThat(secretaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSecretary() throws Exception {
        // Initialize the database
        secretaryRepository.saveAndFlush(secretary);

        int databaseSizeBeforeDelete = secretaryRepository.findAll().size();

        // Delete the secretary
        restSecretaryMockMvc.perform(delete("/api/secretaries/{id}", secretary.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Secretary> secretaryList = secretaryRepository.findAll();
        assertThat(secretaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
