package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.Hand;
import com.stalyon.poker.repository.HandRepository;
import com.stalyon.poker.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.stalyon.poker.domain.Hand}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HandResource {

    private static final String ENTITY_NAME = "hand";
    private final Logger log = LoggerFactory.getLogger(HandResource.class);
    private final HandRepository handRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public HandResource(HandRepository handRepository) {
        this.handRepository = handRepository;
    }

    /**
     * {@code POST  /hands} : Create a new hand.
     *
     * @param hand the hand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hand, or with status {@code 400 (Bad Request)} if the hand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hands")
    public ResponseEntity<Hand> createHand(@RequestBody Hand hand) throws URISyntaxException {
        log.debug("REST request to save Hand : {}", hand);
        if (hand.getId() != null) {
            throw new BadRequestAlertException("A new hand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hand result = handRepository.save(hand);
        return ResponseEntity.created(new URI("/api/hands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hands} : Updates an existing hand.
     *
     * @param hand the hand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hand,
     * or with status {@code 400 (Bad Request)} if the hand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hands")
    public ResponseEntity<Hand> updateHand(@RequestBody Hand hand) throws URISyntaxException {
        log.debug("REST request to update Hand : {}", hand);
        if (hand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Hand result = handRepository.save(hand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, hand.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hands} : get all the hands.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hands in body.
     */
    @GetMapping("/hands")
    public List<Hand> getAllHands() {
        log.debug("REST request to get all Hands");
        return handRepository.findAll();
    }

    /**
     * {@code GET  /hands/:id} : get the "id" hand.
     *
     * @param id the id of the hand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hands/{id}")
    public ResponseEntity<Hand> getHand(@PathVariable Long id) {
        log.debug("REST request to get Hand : {}", id);
        Optional<Hand> hand = handRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(hand);
    }

    /**
     * {@code DELETE  /hands/:id} : delete the "id" hand.
     *
     * @param id the id of the hand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hands/{id}")
    public ResponseEntity<Void> deleteHand(@PathVariable Long id) {
        log.debug("REST request to delete Hand : {}", id);
        handRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
