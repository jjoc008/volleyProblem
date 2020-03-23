package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.domain.Letter;
import co.edu.udistrital.volley.repository.LetterRepository;
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
 * REST controller for managing {@link co.edu.udistrital.volley.domain.Letter}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LetterResource {

    private final Logger log = LoggerFactory.getLogger(LetterResource.class);

    private static final String ENTITY_NAME = "volleyLetter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LetterRepository letterRepository;

    public LetterResource(LetterRepository letterRepository) {
        this.letterRepository = letterRepository;
    }

    /**
     * {@code POST  /letters} : Create a new letter.
     *
     * @param letter the letter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new letter, or with status {@code 400 (Bad Request)} if the letter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/letters")
    public ResponseEntity<Letter> createLetter(@RequestBody Letter letter) throws URISyntaxException {
        log.debug("REST request to save Letter : {}", letter);
        if (letter.getId() != null) {
            throw new BadRequestAlertException("A new letter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Letter result = letterRepository.save(letter);
        return ResponseEntity.created(new URI("/api/letters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /letters} : Updates an existing letter.
     *
     * @param letter the letter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated letter,
     * or with status {@code 400 (Bad Request)} if the letter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the letter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/letters")
    public ResponseEntity<Letter> updateLetter(@RequestBody Letter letter) throws URISyntaxException {
        log.debug("REST request to update Letter : {}", letter);
        if (letter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Letter result = letterRepository.save(letter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, letter.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /letters} : get all the letters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of letters in body.
     */
    @GetMapping("/letters")
    public List<Letter> getAllLetters() {
        log.debug("REST request to get all Letters");
        return letterRepository.findAll();
    }

    /**
     * {@code GET  /letters/:id} : get the "id" letter.
     *
     * @param id the id of the letter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the letter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/letters/{id}")
    public ResponseEntity<Letter> getLetter(@PathVariable Long id) {
        log.debug("REST request to get Letter : {}", id);
        Optional<Letter> letter = letterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(letter);
    }

    /**
     * {@code DELETE  /letters/:id} : delete the "id" letter.
     *
     * @param id the id of the letter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/letters/{id}")
    public ResponseEntity<Void> deleteLetter(@PathVariable Long id) {
        log.debug("REST request to delete Letter : {}", id);
        letterRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
