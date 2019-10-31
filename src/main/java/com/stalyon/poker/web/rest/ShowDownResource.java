package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.ShowDown;
import com.stalyon.poker.repository.ShowDownRepository;
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
 * REST controller for managing {@link com.stalyon.poker.domain.ShowDown}.
 */
@RestController
@RequestMapping("/api")
public class ShowDownResource {

    private final Logger log = LoggerFactory.getLogger(ShowDownResource.class);

    private static final String ENTITY_NAME = "showDown";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShowDownRepository showDownRepository;

    public ShowDownResource(ShowDownRepository showDownRepository) {
        this.showDownRepository = showDownRepository;
    }

    /**
     * {@code POST  /show-downs} : Create a new showDown.
     *
     * @param showDown the showDown to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new showDown, or with status {@code 400 (Bad Request)} if the showDown has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/show-downs")
    public ResponseEntity<ShowDown> createShowDown(@RequestBody ShowDown showDown) throws URISyntaxException {
        log.debug("REST request to save ShowDown : {}", showDown);
        if (showDown.getId() != null) {
            throw new BadRequestAlertException("A new showDown cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShowDown result = showDownRepository.save(showDown);
        return ResponseEntity.created(new URI("/api/show-downs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /show-downs} : Updates an existing showDown.
     *
     * @param showDown the showDown to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated showDown,
     * or with status {@code 400 (Bad Request)} if the showDown is not valid,
     * or with status {@code 500 (Internal Server Error)} if the showDown couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/show-downs")
    public ResponseEntity<ShowDown> updateShowDown(@RequestBody ShowDown showDown) throws URISyntaxException {
        log.debug("REST request to update ShowDown : {}", showDown);
        if (showDown.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShowDown result = showDownRepository.save(showDown);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, showDown.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /show-downs} : get all the showDowns.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of showDowns in body.
     */
    @GetMapping("/show-downs")
    public List<ShowDown> getAllShowDowns() {
        log.debug("REST request to get all ShowDowns");
        return showDownRepository.findAll();
    }

    /**
     * {@code GET  /show-downs/:id} : get the "id" showDown.
     *
     * @param id the id of the showDown to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the showDown, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/show-downs/{id}")
    public ResponseEntity<ShowDown> getShowDown(@PathVariable Long id) {
        log.debug("REST request to get ShowDown : {}", id);
        Optional<ShowDown> showDown = showDownRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(showDown);
    }

    /**
     * {@code DELETE  /show-downs/:id} : delete the "id" showDown.
     *
     * @param id the id of the showDown to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/show-downs/{id}")
    public ResponseEntity<Void> deleteShowDown(@PathVariable Long id) {
        log.debug("REST request to delete ShowDown : {}", id);
        showDownRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
