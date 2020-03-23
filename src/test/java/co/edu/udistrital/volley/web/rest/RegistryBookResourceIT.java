package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.VolleyApp;
import co.edu.udistrital.volley.domain.RegistryBook;
import co.edu.udistrital.volley.repository.RegistryBookRepository;

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
 * Integration tests for the {@link RegistryBookResource} REST controller.
 */
@SpringBootTest(classes = VolleyApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class RegistryBookResourceIT {

    private static final Integer DEFAULT_MEMBER_IDENTIFICATION = 1;
    private static final Integer UPDATED_MEMBER_IDENTIFICATION = 2;

    @Autowired
    private RegistryBookRepository registryBookRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegistryBookMockMvc;

    private RegistryBook registryBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistryBook createEntity(EntityManager em) {
        RegistryBook registryBook = new RegistryBook()
            .memberIdentification(DEFAULT_MEMBER_IDENTIFICATION);
        return registryBook;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistryBook createUpdatedEntity(EntityManager em) {
        RegistryBook registryBook = new RegistryBook()
            .memberIdentification(UPDATED_MEMBER_IDENTIFICATION);
        return registryBook;
    }

    @BeforeEach
    public void initTest() {
        registryBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegistryBook() throws Exception {
        int databaseSizeBeforeCreate = registryBookRepository.findAll().size();

        // Create the RegistryBook
        restRegistryBookMockMvc.perform(post("/api/registry-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registryBook)))
            .andExpect(status().isCreated());

        // Validate the RegistryBook in the database
        List<RegistryBook> registryBookList = registryBookRepository.findAll();
        assertThat(registryBookList).hasSize(databaseSizeBeforeCreate + 1);
        RegistryBook testRegistryBook = registryBookList.get(registryBookList.size() - 1);
        assertThat(testRegistryBook.getMemberIdentification()).isEqualTo(DEFAULT_MEMBER_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void createRegistryBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registryBookRepository.findAll().size();

        // Create the RegistryBook with an existing ID
        registryBook.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistryBookMockMvc.perform(post("/api/registry-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registryBook)))
            .andExpect(status().isBadRequest());

        // Validate the RegistryBook in the database
        List<RegistryBook> registryBookList = registryBookRepository.findAll();
        assertThat(registryBookList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRegistryBooks() throws Exception {
        // Initialize the database
        registryBookRepository.saveAndFlush(registryBook);

        // Get all the registryBookList
        restRegistryBookMockMvc.perform(get("/api/registry-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registryBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].memberIdentification").value(hasItem(DEFAULT_MEMBER_IDENTIFICATION)));
    }
    
    @Test
    @Transactional
    public void getRegistryBook() throws Exception {
        // Initialize the database
        registryBookRepository.saveAndFlush(registryBook);

        // Get the registryBook
        restRegistryBookMockMvc.perform(get("/api/registry-books/{id}", registryBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registryBook.getId().intValue()))
            .andExpect(jsonPath("$.memberIdentification").value(DEFAULT_MEMBER_IDENTIFICATION));
    }

    @Test
    @Transactional
    public void getNonExistingRegistryBook() throws Exception {
        // Get the registryBook
        restRegistryBookMockMvc.perform(get("/api/registry-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegistryBook() throws Exception {
        // Initialize the database
        registryBookRepository.saveAndFlush(registryBook);

        int databaseSizeBeforeUpdate = registryBookRepository.findAll().size();

        // Update the registryBook
        RegistryBook updatedRegistryBook = registryBookRepository.findById(registryBook.getId()).get();
        // Disconnect from session so that the updates on updatedRegistryBook are not directly saved in db
        em.detach(updatedRegistryBook);
        updatedRegistryBook
            .memberIdentification(UPDATED_MEMBER_IDENTIFICATION);

        restRegistryBookMockMvc.perform(put("/api/registry-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRegistryBook)))
            .andExpect(status().isOk());

        // Validate the RegistryBook in the database
        List<RegistryBook> registryBookList = registryBookRepository.findAll();
        assertThat(registryBookList).hasSize(databaseSizeBeforeUpdate);
        RegistryBook testRegistryBook = registryBookList.get(registryBookList.size() - 1);
        assertThat(testRegistryBook.getMemberIdentification()).isEqualTo(UPDATED_MEMBER_IDENTIFICATION);
    }

    @Test
    @Transactional
    public void updateNonExistingRegistryBook() throws Exception {
        int databaseSizeBeforeUpdate = registryBookRepository.findAll().size();

        // Create the RegistryBook

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistryBookMockMvc.perform(put("/api/registry-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(registryBook)))
            .andExpect(status().isBadRequest());

        // Validate the RegistryBook in the database
        List<RegistryBook> registryBookList = registryBookRepository.findAll();
        assertThat(registryBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegistryBook() throws Exception {
        // Initialize the database
        registryBookRepository.saveAndFlush(registryBook);

        int databaseSizeBeforeDelete = registryBookRepository.findAll().size();

        // Delete the registryBook
        restRegistryBookMockMvc.perform(delete("/api/registry-books/{id}", registryBook.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RegistryBook> registryBookList = registryBookRepository.findAll();
        assertThat(registryBookList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
