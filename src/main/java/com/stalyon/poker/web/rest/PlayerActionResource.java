package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.PlayerAction;
import com.stalyon.poker.repository.PlayerActionRepository;
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
 * REST controller for managing {@link com.stalyon.poker.domain.PlayerAction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlayerActionResource {

    private final Logger log = LoggerFactory.getLogger(PlayerActionResource.class);

    private static final String ENTITY_NAME = "playerAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerActionRepository playerActionRepository;

    public PlayerActionResource(PlayerActionRepository playerActionRepository) {
        this.playerActionRepository = playerActionRepository;
    }

    /**
     * {@code POST  /player-actions} : Create a new playerAction.
     *
     * @param playerAction the playerAction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerAction, or with status {@code 400 (Bad Request)} if the playerAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-actions")
    public ResponseEntity<PlayerAction> createPlayerAction(@RequestBody PlayerAction playerAction) throws URISyntaxException {
        log.debug("REST request to save PlayerAction : {}", playerAction);
        if (playerAction.getId() != null) {
            throw new BadRequestAlertException("A new playerAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerAction result = playerActionRepository.save(playerAction);
        return ResponseEntity.created(new URI("/api/player-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-actions} : Updates an existing playerAction.
     *
     * @param playerAction the playerAction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerAction,
     * or with status {@code 400 (Bad Request)} if the playerAction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerAction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-actions")
    public ResponseEntity<PlayerAction> updatePlayerAction(@RequestBody PlayerAction playerAction) throws URISyntaxException {
        log.debug("REST request to update PlayerAction : {}", playerAction);
        if (playerAction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlayerAction result = playerActionRepository.save(playerAction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerAction.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /player-actions} : get all the playerActions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerActions in body.
     */
    @GetMapping("/player-actions")
    public List<PlayerAction> getAllPlayerActions() {
        log.debug("REST request to get all PlayerActions");
        return playerActionRepository.findAll();
    }

    /**
     * {@code GET  /player-actions/:id} : get the "id" playerAction.
     *
     * @param id the id of the playerAction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerAction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-actions/{id}")
    public ResponseEntity<PlayerAction> getPlayerAction(@PathVariable Long id) {
        log.debug("REST request to get PlayerAction : {}", id);
        Optional<PlayerAction> playerAction = playerActionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(playerAction);
    }

    /**
     * {@code DELETE  /player-actions/:id} : delete the "id" playerAction.
     *
     * @param id the id of the playerAction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-actions/{id}")
    public ResponseEntity<Void> deletePlayerAction(@PathVariable Long id) {
        log.debug("REST request to delete PlayerAction : {}", id);
        playerActionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
