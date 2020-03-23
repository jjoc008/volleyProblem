package co.edu.udistrital.volley.web.rest;

import co.edu.udistrital.volley.domain.MemberCard;
import co.edu.udistrital.volley.repository.MemberCardRepository;
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
 * REST controller for managing {@link co.edu.udistrital.volley.domain.MemberCard}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MemberCardResource {

    private final Logger log = LoggerFactory.getLogger(MemberCardResource.class);

    private static final String ENTITY_NAME = "volleyMemberCard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberCardRepository memberCardRepository;

    public MemberCardResource(MemberCardRepository memberCardRepository) {
        this.memberCardRepository = memberCardRepository;
    }

    /**
     * {@code POST  /member-cards} : Create a new memberCard.
     *
     * @param memberCard the memberCard to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberCard, or with status {@code 400 (Bad Request)} if the memberCard has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-cards")
    public ResponseEntity<MemberCard> createMemberCard(@RequestBody MemberCard memberCard) throws URISyntaxException {
        log.debug("REST request to save MemberCard : {}", memberCard);
        if (memberCard.getId() != null) {
            throw new BadRequestAlertException("A new memberCard cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberCard result = memberCardRepository.save(memberCard);
        return ResponseEntity.created(new URI("/api/member-cards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-cards} : Updates an existing memberCard.
     *
     * @param memberCard the memberCard to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberCard,
     * or with status {@code 400 (Bad Request)} if the memberCard is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberCard couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-cards")
    public ResponseEntity<MemberCard> updateMemberCard(@RequestBody MemberCard memberCard) throws URISyntaxException {
        log.debug("REST request to update MemberCard : {}", memberCard);
        if (memberCard.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MemberCard result = memberCardRepository.save(memberCard);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, memberCard.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /member-cards} : get all the memberCards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberCards in body.
     */
    @GetMapping("/member-cards")
    public List<MemberCard> getAllMemberCards() {
        log.debug("REST request to get all MemberCards");
        return memberCardRepository.findAll();
    }

    /**
     * {@code GET  /member-cards/:id} : get the "id" memberCard.
     *
     * @param id the id of the memberCard to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberCard, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-cards/{id}")
    public ResponseEntity<MemberCard> getMemberCard(@PathVariable Long id) {
        log.debug("REST request to get MemberCard : {}", id);
        Optional<MemberCard> memberCard = memberCardRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(memberCard);
    }

    /**
     * {@code DELETE  /member-cards/:id} : delete the "id" memberCard.
     *
     * @param id the id of the memberCard to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-cards/{id}")
    public ResponseEntity<Void> deleteMemberCard(@PathVariable Long id) {
        log.debug("REST request to delete MemberCard : {}", id);
        memberCardRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
