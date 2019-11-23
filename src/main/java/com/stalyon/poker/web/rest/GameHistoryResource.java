package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.GameHistory;
import com.stalyon.poker.repository.GameHistoryRepository;
import com.stalyon.poker.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.stalyon.poker.domain.GameHistory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GameHistoryResource {

    private static final String ENTITY_NAME = "gameHistory";
    private final Logger log = LoggerFactory.getLogger(GameHistoryResource.class);
    private final GameHistoryRepository gameHistoryRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public GameHistoryResource(GameHistoryRepository gameHistoryRepository) {
        this.gameHistoryRepository = gameHistoryRepository;
    }

    /**
     * {@code POST  /game-histories} : Create a new gameHistory.
     *
     * @param gameHistory the gameHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameHistory, or with status {@code 400 (Bad Request)} if the gameHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-histories")
    public ResponseEntity<GameHistory> createGameHistory(@Valid @RequestBody GameHistory gameHistory) throws URISyntaxException {
        log.debug("REST request to save GameHistory : {}", gameHistory);
        if (gameHistory.getId() != null) {
            throw new BadRequestAlertException("A new gameHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameHistory result = gameHistoryRepository.save(gameHistory);
        return ResponseEntity.created(new URI("/api/game-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-histories} : Updates an existing gameHistory.
     *
     * @param gameHistory the gameHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameHistory,
     * or with status {@code 400 (Bad Request)} if the gameHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-histories")
    public ResponseEntity<GameHistory> updateGameHistory(@Valid @RequestBody GameHistory gameHistory) throws URISyntaxException {
        log.debug("REST request to update GameHistory : {}", gameHistory);
        if (gameHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GameHistory result = gameHistoryRepository.save(gameHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /game-histories} : get all the gameHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameHistories in body.
     */
    @GetMapping("/game-histories")
    public List<GameHistory> getAllGameHistories() {
        log.debug("REST request to get all GameHistories");
        return gameHistoryRepository.findAll();
    }

    /**
     * {@code GET  /game-histories/:id} : get the "id" gameHistory.
     *
     * @param id the id of the gameHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-histories/{id}")
    public ResponseEntity<GameHistory> getGameHistory(@PathVariable Long id) {
        log.debug("REST request to get GameHistory : {}", id);
        Optional<GameHistory> gameHistory = gameHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gameHistory);
    }

    /**
     * {@code DELETE  /game-histories/:id} : delete the "id" gameHistory.
     *
     * @param id the id of the gameHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-histories/{id}")
    public ResponseEntity<Void> deleteGameHistory(@PathVariable Long id) {
        log.debug("REST request to delete GameHistory : {}", id);
        gameHistoryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
