package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.Action;
import com.stalyon.poker.repository.ActionRepository;
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
 * REST controller for managing {@link com.stalyon.poker.domain.Action}.
 */
@RestController
@RequestMapping("/api")
public class ActionResource {

    private final Logger log = LoggerFactory.getLogger(ActionResource.class);

    private static final String ENTITY_NAME = "action";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActionRepository actionRepository;

    public ActionResource(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    /**
     * {@code POST  /actions} : Create a new action.
     *
     * @param action the action to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new action, or with status {@code 400 (Bad Request)} if the action has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/actions")
    public ResponseEntity<Action> createAction(@RequestBody Action action) throws URISyntaxException {
        log.debug("REST request to save Action : {}", action);
        if (action.getId() != null) {
            throw new BadRequestAlertException("A new action cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Action result = actionRepository.save(action);
        return ResponseEntity.created(new URI("/api/actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /actions} : Updates an existing action.
     *
     * @param action the action to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated action,
     * or with status {@code 400 (Bad Request)} if the action is not valid,
     * or with status {@code 500 (Internal Server Error)} if the action couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/actions")
    public ResponseEntity<Action> updateAction(@RequestBody Action action) throws URISyntaxException {
        log.debug("REST request to update Action : {}", action);
        if (action.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Action result = actionRepository.save(action);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, action.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /actions} : get all the actions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actions in body.
     */
    @GetMapping("/actions")
    public List<Action> getAllActions() {
        log.debug("REST request to get all Actions");
        return actionRepository.findAll();
    }

    /**
     * {@code GET  /actions/:id} : get the "id" action.
     *
     * @param id the id of the action to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the action, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/actions/{id}")
    public ResponseEntity<Action> getAction(@PathVariable Long id) {
        log.debug("REST request to get Action : {}", id);
        Optional<Action> action = actionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(action);
    }

    /**
     * {@code DELETE  /actions/:id} : delete the "id" action.
     *
     * @param id the id of the action to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/actions/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable Long id) {
        log.debug("REST request to delete Action : {}", id);
        actionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
