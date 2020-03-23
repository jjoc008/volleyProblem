package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.VolleyApp;
import co.edu.udistrital.volley.domain.PostalAddress;
import co.edu.udistrital.volley.repository.PostalAddressRepository;

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
 * Integration tests for the {@link PostalAddressResource} REST controller.
 */
@SpringBootTest(classes = VolleyApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PostalAddressResourceIT {

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_HOUSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RESIDENCE = "AAAAAAAAAA";
    private static final String UPDATED_RESIDENCE = "BBBBBBBBBB";

    @Autowired
    private PostalAddressRepository postalAddressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostalAddressMockMvc;

    private PostalAddress postalAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostalAddress createEntity(EntityManager em) {
        PostalAddress postalAddress = new PostalAddress()
            .street(DEFAULT_STREET)
            .houseNumber(DEFAULT_HOUSE_NUMBER)
            .zipCode(DEFAULT_ZIP_CODE)
            .residence(DEFAULT_RESIDENCE);
        return postalAddress;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostalAddress createUpdatedEntity(EntityManager em) {
        PostalAddress postalAddress = new PostalAddress()
            .street(UPDATED_STREET)
            .houseNumber(UPDATED_HOUSE_NUMBER)
            .zipCode(UPDATED_ZIP_CODE)
            .residence(UPDATED_RESIDENCE);
        return postalAddress;
    }

    @BeforeEach
    public void initTest() {
        postalAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostalAddress() throws Exception {
        int databaseSizeBeforeCreate = postalAddressRepository.findAll().size();

        // Create the PostalAddress
        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postalAddress)))
            .andExpect(status().isCreated());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeCreate + 1);
        PostalAddress testPostalAddress = postalAddressList.get(postalAddressList.size() - 1);
        assertThat(testPostalAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testPostalAddress.getHouseNumber()).isEqualTo(DEFAULT_HOUSE_NUMBER);
        assertThat(testPostalAddress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testPostalAddress.getResidence()).isEqualTo(DEFAULT_RESIDENCE);
    }

    @Test
    @Transactional
    public void createPostalAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postalAddressRepository.findAll().size();

        // Create the PostalAddress with an existing ID
        postalAddress.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostalAddressMockMvc.perform(post("/api/postal-addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postalAddress)))
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPostalAddresses() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        // Get all the postalAddressList
        restPostalAddressMockMvc.perform(get("/api/postal-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postalAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET)))
            .andExpect(jsonPath("$.[*].houseNumber").value(hasItem(DEFAULT_HOUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE)))
            .andExpect(jsonPath("$.[*].residence").value(hasItem(DEFAULT_RESIDENCE)));
    }
    
    @Test
    @Transactional
    public void getPostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        // Get the postalAddress
        restPostalAddressMockMvc.perform(get("/api/postal-addresses/{id}", postalAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postalAddress.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET))
            .andExpect(jsonPath("$.houseNumber").value(DEFAULT_HOUSE_NUMBER))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE))
            .andExpect(jsonPath("$.residence").value(DEFAULT_RESIDENCE));
    }

    @Test
    @Transactional
    public void getNonExistingPostalAddress() throws Exception {
        // Get the postalAddress
        restPostalAddressMockMvc.perform(get("/api/postal-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        int databaseSizeBeforeUpdate = postalAddressRepository.findAll().size();

        // Update the postalAddress
        PostalAddress updatedPostalAddress = postalAddressRepository.findById(postalAddress.getId()).get();
        // Disconnect from session so that the updates on updatedPostalAddress are not directly saved in db
        em.detach(updatedPostalAddress);
        updatedPostalAddress
            .street(UPDATED_STREET)
            .houseNumber(UPDATED_HOUSE_NUMBER)
            .zipCode(UPDATED_ZIP_CODE)
            .residence(UPDATED_RESIDENCE);

        restPostalAddressMockMvc.perform(put("/api/postal-addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPostalAddress)))
            .andExpect(status().isOk());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeUpdate);
        PostalAddress testPostalAddress = postalAddressList.get(postalAddressList.size() - 1);
        assertThat(testPostalAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testPostalAddress.getHouseNumber()).isEqualTo(UPDATED_HOUSE_NUMBER);
        assertThat(testPostalAddress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testPostalAddress.getResidence()).isEqualTo(UPDATED_RESIDENCE);
    }

    @Test
    @Transactional
    public void updateNonExistingPostalAddress() throws Exception {
        int databaseSizeBeforeUpdate = postalAddressRepository.findAll().size();

        // Create the PostalAddress

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostalAddressMockMvc.perform(put("/api/postal-addresses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(postalAddress)))
            .andExpect(status().isBadRequest());

        // Validate the PostalAddress in the database
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePostalAddress() throws Exception {
        // Initialize the database
        postalAddressRepository.saveAndFlush(postalAddress);

        int databaseSizeBeforeDelete = postalAddressRepository.findAll().size();

        // Delete the postalAddress
        restPostalAddressMockMvc.perform(delete("/api/postal-addresses/{id}", postalAddress.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostalAddress> postalAddressList = postalAddressRepository.findAll();
        assertThat(postalAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
