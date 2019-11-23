package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.CashGame;
import com.stalyon.poker.repository.CashGameRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.stalyon.poker.domain.CashGame}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CashGameResource {

    private static final String ENTITY_NAME = "cashGame";
    private final Logger log = LoggerFactory.getLogger(CashGameResource.class);
    private final CashGameRepository cashGameRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CashGameResource(CashGameRepository cashGameRepository) {
        this.cashGameRepository = cashGameRepository;
    }

    /**
     * {@code POST  /cash-games} : Create a new cashGame.
     *
     * @param cashGame the cashGame to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cashGame, or with status {@code 400 (Bad Request)} if the cashGame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cash-games")
    public ResponseEntity<CashGame> createCashGame(@RequestBody CashGame cashGame) throws URISyntaxException {
        log.debug("REST request to save CashGame : {}", cashGame);
        if (cashGame.getId() != null) {
            throw new BadRequestAlertException("A new cashGame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CashGame result = cashGameRepository.save(cashGame);
        return ResponseEntity.created(new URI("/api/cash-games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cash-games} : Updates an existing cashGame.
     *
     * @param cashGame the cashGame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cashGame,
     * or with status {@code 400 (Bad Request)} if the cashGame is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cashGame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cash-games")
    public ResponseEntity<CashGame> updateCashGame(@RequestBody CashGame cashGame) throws URISyntaxException {
        log.debug("REST request to update CashGame : {}", cashGame);
        if (cashGame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CashGame result = cashGameRepository.save(cashGame);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cashGame.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cash-games} : get all the cashGames.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cashGames in body.
     */
    @GetMapping("/cash-games")
    public List<CashGame> getAllCashGames(@RequestParam(required = false) String filter) {
        if ("gamehistory-is-null".equals(filter)) {
            log.debug("REST request to get all CashGames where gameHistory is null");
            return StreamSupport
                .stream(cashGameRepository.findAll().spliterator(), false)
                .filter(cashGame -> cashGame.getGameHistory() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all CashGames");
        return cashGameRepository.findAll();
    }

    /**
     * {@code GET  /cash-games/:id} : get the "id" cashGame.
     *
     * @param id the id of the cashGame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cashGame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cash-games/{id}")
    public ResponseEntity<CashGame> getCashGame(@PathVariable Long id) {
        log.debug("REST request to get CashGame : {}", id);
        Optional<CashGame> cashGame = cashGameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cashGame);
    }

    /**
     * {@code DELETE  /cash-games/:id} : delete the "id" cashGame.
     *
     * @param id the id of the cashGame to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cash-games/{id}")
    public ResponseEntity<Void> deleteCashGame(@PathVariable Long id) {
        log.debug("REST request to delete CashGame : {}", id);
        cashGameRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
