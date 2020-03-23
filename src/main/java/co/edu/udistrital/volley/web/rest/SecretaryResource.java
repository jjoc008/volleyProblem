package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.domain.Secretary;
import co.edu.udistrital.volley.repository.SecretaryRepository;
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
 * REST controller for managing {@link co.edu.udistrital.volley.domain.Secretary}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SecretaryResource {

    private final Logger log = LoggerFactory.getLogger(SecretaryResource.class);

    private static final String ENTITY_NAME = "volleySecretary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SecretaryRepository secretaryRepository;

    public SecretaryResource(SecretaryRepository secretaryRepository) {
        this.secretaryRepository = secretaryRepository;
    }

    /**
     * {@code POST  /secretaries} : Create a new secretary.
     *
     * @param secretary the secretary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new secretary, or with status {@code 400 (Bad Request)} if the secretary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/secretaries")
    public ResponseEntity<Secretary> createSecretary(@RequestBody Secretary secretary) throws URISyntaxException {
        log.debug("REST request to save Secretary : {}", secretary);
        if (secretary.getId() != null) {
            throw new BadRequestAlertException("A new secretary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Secretary result = secretaryRepository.save(secretary);
        return ResponseEntity.created(new URI("/api/secretaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /secretaries} : Updates an existing secretary.
     *
     * @param secretary the secretary to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated secretary,
     * or with status {@code 400 (Bad Request)} if the secretary is not valid,
     * or with status {@code 500 (Internal Server Error)} if the secretary couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/secretaries")
    public ResponseEntity<Secretary> updateSecretary(@RequestBody Secretary secretary) throws URISyntaxException {
        log.debug("REST request to update Secretary : {}", secretary);
        if (secretary.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Secretary result = secretaryRepository.save(secretary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, secretary.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /secretaries} : get all the secretaries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of secretaries in body.
     */
    @GetMapping("/secretaries")
    public List<Secretary> getAllSecretaries() {
        log.debug("REST request to get all Secretaries");
        return secretaryRepository.findAll();
    }

    /**
     * {@code GET  /secretaries/:id} : get the "id" secretary.
     *
     * @param id the id of the secretary to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the secretary, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/secretaries/{id}")
    public ResponseEntity<Secretary> getSecretary(@PathVariable Long id) {
        log.debug("REST request to get Secretary : {}", id);
        Optional<Secretary> secretary = secretaryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(secretary);
    }

    /**
     * {@code DELETE  /secretaries/:id} : delete the "id" secretary.
     *
     * @param id the id of the secretary to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/secretaries/{id}")
    public ResponseEntity<Void> deleteSecretary(@PathVariable Long id) {
        log.debug("REST request to delete Secretary : {}", id);
        secretaryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
