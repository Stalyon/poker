package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.ParseHistory;
import com.stalyon.poker.repository.ParseHistoryRepository;
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
 * REST controller for managing {@link com.stalyon.poker.domain.ParseHistory}.
 */
@RestController
@RequestMapping("/api")
public class ParseHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ParseHistoryResource.class);

    private static final String ENTITY_NAME = "parseHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParseHistoryRepository parseHistoryRepository;

    public ParseHistoryResource(ParseHistoryRepository parseHistoryRepository) {
        this.parseHistoryRepository = parseHistoryRepository;
    }

    /**
     * {@code POST  /parse-histories} : Create a new parseHistory.
     *
     * @param parseHistory the parseHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parseHistory, or with status {@code 400 (Bad Request)} if the parseHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parse-histories")
    public ResponseEntity<ParseHistory> createParseHistory(@RequestBody ParseHistory parseHistory) throws URISyntaxException {
        log.debug("REST request to save ParseHistory : {}", parseHistory);
        if (parseHistory.getId() != null) {
            throw new BadRequestAlertException("A new parseHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParseHistory result = parseHistoryRepository.save(parseHistory);
        return ResponseEntity.created(new URI("/api/parse-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parse-histories} : Updates an existing parseHistory.
     *
     * @param parseHistory the parseHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parseHistory,
     * or with status {@code 400 (Bad Request)} if the parseHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parseHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parse-histories")
    public ResponseEntity<ParseHistory> updateParseHistory(@RequestBody ParseHistory parseHistory) throws URISyntaxException {
        log.debug("REST request to update ParseHistory : {}", parseHistory);
        if (parseHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParseHistory result = parseHistoryRepository.save(parseHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parseHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /parse-histories} : get all the parseHistories.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parseHistories in body.
     */
    @GetMapping("/parse-histories")
    public List<ParseHistory> getAllParseHistories() {
        log.debug("REST request to get all ParseHistories");
        return parseHistoryRepository.findAll();
    }

    /**
     * {@code GET  /parse-histories/:id} : get the "id" parseHistory.
     *
     * @param id the id of the parseHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parseHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parse-histories/{id}")
    public ResponseEntity<ParseHistory> getParseHistory(@PathVariable Long id) {
        log.debug("REST request to get ParseHistory : {}", id);
        Optional<ParseHistory> parseHistory = parseHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(parseHistory);
    }

    /**
     * {@code DELETE  /parse-histories/:id} : delete the "id" parseHistory.
     *
     * @param id the id of the parseHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parse-histories/{id}")
    public ResponseEntity<Void> deleteParseHistory(@PathVariable Long id) {
        log.debug("REST request to delete ParseHistory : {}", id);
        parseHistoryRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
