package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.BettingRound;
import com.stalyon.poker.repository.BettingRoundRepository;
import com.stalyon.poker.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.stalyon.poker.domain.BettingRound}.
 */
@RestController
@RequestMapping("/api")
public class BettingRoundResource {

    private final Logger log = LoggerFactory.getLogger(BettingRoundResource.class);

    private static final String ENTITY_NAME = "bettingRound";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BettingRoundRepository bettingRoundRepository;

    public BettingRoundResource(BettingRoundRepository bettingRoundRepository) {
        this.bettingRoundRepository = bettingRoundRepository;
    }

    /**
     * {@code POST  /betting-rounds} : Create a new bettingRound.
     *
     * @param bettingRound the bettingRound to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bettingRound, or with status {@code 400 (Bad Request)} if the bettingRound has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/betting-rounds")
    public ResponseEntity<BettingRound> createBettingRound(@RequestBody BettingRound bettingRound) throws URISyntaxException {
        log.debug("REST request to save BettingRound : {}", bettingRound);
        if (bettingRound.getId() != null) {
            throw new BadRequestAlertException("A new bettingRound cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BettingRound result = bettingRoundRepository.save(bettingRound);
        return ResponseEntity.created(new URI("/api/betting-rounds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /betting-rounds} : Updates an existing bettingRound.
     *
     * @param bettingRound the bettingRound to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bettingRound,
     * or with status {@code 400 (Bad Request)} if the bettingRound is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bettingRound couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/betting-rounds")
    public ResponseEntity<BettingRound> updateBettingRound(@RequestBody BettingRound bettingRound) throws URISyntaxException {
        log.debug("REST request to update BettingRound : {}", bettingRound);
        if (bettingRound.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BettingRound result = bettingRoundRepository.save(bettingRound);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bettingRound.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /betting-rounds} : get all the bettingRounds.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bettingRounds in body.
     */
    @GetMapping("/betting-rounds")
    public List<BettingRound> getAllBettingRounds() {
        log.debug("REST request to get all BettingRounds");
        return bettingRoundRepository.findAll();
    }

    /**
     * {@code GET  /betting-rounds/:id} : get the "id" bettingRound.
     *
     * @param id the id of the bettingRound to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bettingRound, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/betting-rounds/{id}")
    public ResponseEntity<BettingRound> getBettingRound(@PathVariable Long id) {
        log.debug("REST request to get BettingRound : {}", id);
        Optional<BettingRound> bettingRound = bettingRoundRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bettingRound);
    }

    /**
     * {@code DELETE  /betting-rounds/:id} : delete the "id" bettingRound.
     *
     * @param id the id of the bettingRound to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/betting-rounds/{id}")
    public ResponseEntity<Void> deleteBettingRound(@PathVariable Long id) {
        log.debug("REST request to delete BettingRound : {}", id);
        bettingRoundRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
