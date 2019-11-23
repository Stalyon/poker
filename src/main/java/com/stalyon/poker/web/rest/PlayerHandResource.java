package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.PlayerHand;
import com.stalyon.poker.repository.PlayerHandRepository;
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
 * REST controller for managing {@link com.stalyon.poker.domain.PlayerHand}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlayerHandResource {

    private static final String ENTITY_NAME = "playerHand";
    private final Logger log = LoggerFactory.getLogger(PlayerHandResource.class);
    private final PlayerHandRepository playerHandRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PlayerHandResource(PlayerHandRepository playerHandRepository) {
        this.playerHandRepository = playerHandRepository;
    }

    /**
     * {@code POST  /player-hands} : Create a new playerHand.
     *
     * @param playerHand the playerHand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerHand, or with status {@code 400 (Bad Request)} if the playerHand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-hands")
    public ResponseEntity<PlayerHand> createPlayerHand(@RequestBody PlayerHand playerHand) throws URISyntaxException {
        log.debug("REST request to save PlayerHand : {}", playerHand);
        if (playerHand.getId() != null) {
            throw new BadRequestAlertException("A new playerHand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerHand result = playerHandRepository.save(playerHand);
        return ResponseEntity.created(new URI("/api/player-hands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-hands} : Updates an existing playerHand.
     *
     * @param playerHand the playerHand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerHand,
     * or with status {@code 400 (Bad Request)} if the playerHand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerHand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-hands")
    public ResponseEntity<PlayerHand> updatePlayerHand(@RequestBody PlayerHand playerHand) throws URISyntaxException {
        log.debug("REST request to update PlayerHand : {}", playerHand);
        if (playerHand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlayerHand result = playerHandRepository.save(playerHand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerHand.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /player-hands} : get all the playerHands.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerHands in body.
     */
    @GetMapping("/player-hands")
    public List<PlayerHand> getAllPlayerHands() {
        log.debug("REST request to get all PlayerHands");
        return playerHandRepository.findAll();
    }

    /**
     * {@code GET  /player-hands/:id} : get the "id" playerHand.
     *
     * @param id the id of the playerHand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerHand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-hands/{id}")
    public ResponseEntity<PlayerHand> getPlayerHand(@PathVariable Long id) {
        log.debug("REST request to get PlayerHand : {}", id);
        Optional<PlayerHand> playerHand = playerHandRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(playerHand);
    }

    /**
     * {@code DELETE  /player-hands/:id} : delete the "id" playerHand.
     *
     * @param id the id of the playerHand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-hands/{id}")
    public ResponseEntity<Void> deletePlayerHand(@PathVariable Long id) {
        log.debug("REST request to delete PlayerHand : {}", id);
        playerHandRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
