package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.domain.PostalAddress;
import co.edu.udistrital.volley.repository.PostalAddressRepository;
import co.edu.udistrital.volley.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link co.edu.udistrital.volley.domain.PostalAddress}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PostalAddressResource {

    private final Logger log = LoggerFactory.getLogger(PostalAddressResource.class);

    private static final String ENTITY_NAME = "volleyPostalAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostalAddressRepository postalAddressRepository;

    public PostalAddressResource(PostalAddressRepository postalAddressRepository) {
        this.postalAddressRepository = postalAddressRepository;
    }

    /**
     * {@code POST  /postal-addresses} : Create a new postalAddress.
     *
     * @param postalAddress the postalAddress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postalAddress, or with status {@code 400 (Bad Request)} if the postalAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/postal-addresses")
    public ResponseEntity<PostalAddress> createPostalAddress(@RequestBody PostalAddress postalAddress) throws URISyntaxException {
        log.debug("REST request to save PostalAddress : {}", postalAddress);
        if (postalAddress.getId() != null) {
            throw new BadRequestAlertException("A new postalAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostalAddress result = postalAddressRepository.save(postalAddress);
        return ResponseEntity.created(new URI("/api/postal-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /postal-addresses} : Updates an existing postalAddress.
     *
     * @param postalAddress the postalAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postalAddress,
     * or with status {@code 400 (Bad Request)} if the postalAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postalAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/postal-addresses")
    public ResponseEntity<PostalAddress> updatePostalAddress(@RequestBody PostalAddress postalAddress) throws URISyntaxException {
        log.debug("REST request to update PostalAddress : {}", postalAddress);
        if (postalAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PostalAddress result = postalAddressRepository.save(postalAddress);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, postalAddress.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /postal-addresses} : get all the postalAddresses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postalAddresses in body.
     */
    @GetMapping("/postal-addresses")
    public List<PostalAddress> getAllPostalAddresses() {
        log.debug("REST request to get all PostalAddresses");
        return postalAddressRepository.findAll();
    }

    /**
     * {@code GET  /postal-addresses/:id} : get the "id" postalAddress.
     *
     * @param id the id of the postalAddress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postalAddress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/postal-addresses/{id}")
    public ResponseEntity<PostalAddress> getPostalAddress(@PathVariable Long id) {
        log.debug("REST request to get PostalAddress : {}", id);
        Optional<PostalAddress> postalAddress = postalAddressRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(postalAddress);
    }

    /**
     * {@code DELETE  /postal-addresses/:id} : delete the "id" postalAddress.
     *
     * @param id the id of the postalAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/postal-addresses/{id}")
    public ResponseEntity<Void> deletePostalAddress(@PathVariable Long id) {
        log.debug("REST request to delete PostalAddress : {}", id);
        postalAddressRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
