package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.domain.RegistryBook;
import co.edu.udistrital.volley.repository.RegistryBookRepository;
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
 * REST controller for managing {@link co.edu.udistrital.volley.domain.RegistryBook}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RegistryBookResource {

    private final Logger log = LoggerFactory.getLogger(RegistryBookResource.class);

    private static final String ENTITY_NAME = "volleyRegistryBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistryBookRepository registryBookRepository;

    public RegistryBookResource(RegistryBookRepository registryBookRepository) {
        this.registryBookRepository = registryBookRepository;
    }

    /**
     * {@code POST  /registry-books} : Create a new registryBook.
     *
     * @param registryBook the registryBook to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registryBook, or with status {@code 400 (Bad Request)} if the registryBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/registry-books")
    public ResponseEntity<RegistryBook> createRegistryBook(@RequestBody RegistryBook registryBook) throws URISyntaxException {
        log.debug("REST request to save RegistryBook : {}", registryBook);
        if (registryBook.getId() != null) {
            throw new BadRequestAlertException("A new registryBook cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegistryBook result = registryBookRepository.save(registryBook);
        return ResponseEntity.created(new URI("/api/registry-books/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /registry-books} : Updates an existing registryBook.
     *
     * @param registryBook the registryBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registryBook,
     * or with status {@code 400 (Bad Request)} if the registryBook is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registryBook couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/registry-books")
    public ResponseEntity<RegistryBook> updateRegistryBook(@RequestBody RegistryBook registryBook) throws URISyntaxException {
        log.debug("REST request to update RegistryBook : {}", registryBook);
        if (registryBook.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegistryBook result = registryBookRepository.save(registryBook);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, registryBook.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /registry-books} : get all the registryBooks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registryBooks in body.
     */
    @GetMapping("/registry-books")
    public List<RegistryBook> getAllRegistryBooks() {
        log.debug("REST request to get all RegistryBooks");
        return registryBookRepository.findAll();
    }

    /**
     * {@code GET  /registry-books/:id} : get the "id" registryBook.
     *
     * @param id the id of the registryBook to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registryBook, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/registry-books/{id}")
    public ResponseEntity<RegistryBook> getRegistryBook(@PathVariable Long id) {
        log.debug("REST request to get RegistryBook : {}", id);
        Optional<RegistryBook> registryBook = registryBookRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(registryBook);
    }

    /**
     * {@code DELETE  /registry-books/:id} : delete the "id" registryBook.
     *
     * @param id the id of the registryBook to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/registry-books/{id}")
    public ResponseEntity<Void> deleteRegistryBook(@PathVariable Long id) {
        log.debug("REST request to delete RegistryBook : {}", id);
        registryBookRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
