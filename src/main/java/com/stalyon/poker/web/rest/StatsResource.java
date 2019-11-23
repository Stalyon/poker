package com.stalyon.poker.web.rest;

import com.stalyon.poker.service.StatsService;
import com.stalyon.poker.web.dto.StatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller.
 */
@RestController
@RequestMapping("/api")
public class StatsResource {

    @Autowired
    private StatsService statsService;

    @GetMapping("/stats")
    public StatsDto get() {
        return this.statsService.getStats();
    }
}
