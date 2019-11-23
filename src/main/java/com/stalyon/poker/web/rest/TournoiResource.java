package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.Tournoi;
import com.stalyon.poker.repository.TournoiRepository;
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
 * REST controller for managing {@link com.stalyon.poker.domain.Tournoi}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TournoiResource {

    private static final String ENTITY_NAME = "tournoi";
    private final Logger log = LoggerFactory.getLogger(TournoiResource.class);
    private final TournoiRepository tournoiRepository;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public TournoiResource(TournoiRepository tournoiRepository) {
        this.tournoiRepository = tournoiRepository;
    }

    /**
     * {@code POST  /tournois} : Create a new tournoi.
     *
     * @param tournoi the tournoi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tournoi, or with status {@code 400 (Bad Request)} if the tournoi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tournois")
    public ResponseEntity<Tournoi> createTournoi(@RequestBody Tournoi tournoi) throws URISyntaxException {
        log.debug("REST request to save Tournoi : {}", tournoi);
        if (tournoi.getId() != null) {
            throw new BadRequestAlertException("A new tournoi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tournoi result = tournoiRepository.save(tournoi);
        return ResponseEntity.created(new URI("/api/tournois/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tournois} : Updates an existing tournoi.
     *
     * @param tournoi the tournoi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tournoi,
     * or with status {@code 400 (Bad Request)} if the tournoi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tournoi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tournois")
    public ResponseEntity<Tournoi> updateTournoi(@RequestBody Tournoi tournoi) throws URISyntaxException {
        log.debug("REST request to update Tournoi : {}", tournoi);
        if (tournoi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tournoi result = tournoiRepository.save(tournoi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tournoi.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tournois} : get all the tournois.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tournois in body.
     */
    @GetMapping("/tournois")
    public List<Tournoi> getAllTournois(@RequestParam(required = false) String filter) {
        if ("gamehistory-is-null".equals(filter)) {
            log.debug("REST request to get all Tournois where gameHistory is null");
            return StreamSupport
                .stream(tournoiRepository.findAll().spliterator(), false)
                .filter(tournoi -> tournoi.getGameHistory() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Tournois");
        return tournoiRepository.findAll();
    }

    /**
     * {@code GET  /tournois/:id} : get the "id" tournoi.
     *
     * @param id the id of the tournoi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tournoi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tournois/{id}")
    public ResponseEntity<Tournoi> getTournoi(@PathVariable Long id) {
        log.debug("REST request to get Tournoi : {}", id);
        Optional<Tournoi> tournoi = tournoiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tournoi);
    }

    /**
     * {@code DELETE  /tournois/:id} : delete the "id" tournoi.
     *
     * @param id the id of the tournoi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tournois/{id}")
    public ResponseEntity<Void> deleteTournoi(@PathVariable Long id) {
        log.debug("REST request to delete Tournoi : {}", id);
        tournoiRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
