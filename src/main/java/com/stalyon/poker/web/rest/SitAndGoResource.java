package com.stalyon.poker.web.rest;

import com.stalyon.poker.domain.SitAndGo;
import com.stalyon.poker.repository.SitAndGoRepository;
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
 * REST controller for managing {@link com.stalyon.poker.domain.SitAndGo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SitAndGoResource {

    private final Logger log = LoggerFactory.getLogger(SitAndGoResource.class);

    private static final String ENTITY_NAME = "sitAndGo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SitAndGoRepository sitAndGoRepository;

    public SitAndGoResource(SitAndGoRepository sitAndGoRepository) {
        this.sitAndGoRepository = sitAndGoRepository;
    }

    /**
     * {@code POST  /sit-and-gos} : Create a new sitAndGo.
     *
     * @param sitAndGo the sitAndGo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sitAndGo, or with status {@code 400 (Bad Request)} if the sitAndGo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sit-and-gos")
    public ResponseEntity<SitAndGo> createSitAndGo(@RequestBody SitAndGo sitAndGo) throws URISyntaxException {
        log.debug("REST request to save SitAndGo : {}", sitAndGo);
        if (sitAndGo.getId() != null) {
            throw new BadRequestAlertException("A new sitAndGo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SitAndGo result = sitAndGoRepository.save(sitAndGo);
        return ResponseEntity.created(new URI("/api/sit-and-gos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sit-and-gos} : Updates an existing sitAndGo.
     *
     * @param sitAndGo the sitAndGo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sitAndGo,
     * or with status {@code 400 (Bad Request)} if the sitAndGo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sitAndGo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sit-and-gos")
    public ResponseEntity<SitAndGo> updateSitAndGo(@RequestBody SitAndGo sitAndGo) throws URISyntaxException {
        log.debug("REST request to update SitAndGo : {}", sitAndGo);
        if (sitAndGo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SitAndGo result = sitAndGoRepository.save(sitAndGo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sitAndGo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sit-and-gos} : get all the sitAndGos.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sitAndGos in body.
     */
    @GetMapping("/sit-and-gos")
    public List<SitAndGo> getAllSitAndGos(@RequestParam(required = false) String filter) {
        if ("gamehistory-is-null".equals(filter)) {
            log.debug("REST request to get all SitAndGos where gameHistory is null");
            return StreamSupport
                .stream(sitAndGoRepository.findAll().spliterator(), false)
                .filter(sitAndGo -> sitAndGo.getGameHistory() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all SitAndGos");
        return sitAndGoRepository.findAll();
    }

    /**
     * {@code GET  /sit-and-gos/:id} : get the "id" sitAndGo.
     *
     * @param id the id of the sitAndGo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sitAndGo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sit-and-gos/{id}")
    public ResponseEntity<SitAndGo> getSitAndGo(@PathVariable Long id) {
        log.debug("REST request to get SitAndGo : {}", id);
        Optional<SitAndGo> sitAndGo = sitAndGoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sitAndGo);
    }

    /**
     * {@code DELETE  /sit-and-gos/:id} : delete the "id" sitAndGo.
     *
     * @param id the id of the sitAndGo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sit-and-gos/{id}")
    public ResponseEntity<Void> deleteSitAndGo(@PathVariable Long id) {
        log.debug("REST request to delete SitAndGo : {}", id);
        sitAndGoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
