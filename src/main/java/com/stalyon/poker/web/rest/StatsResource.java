package com.stalyon.poker.web.rest;

import com.stalyon.poker.service.StatsService;
import com.stalyon.poker.web.dto.RequestStatsDto;
import com.stalyon.poker.web.dto.StatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller.
 */
@RestController
@RequestMapping("/api")
public class StatsResource {

    @Autowired
    private StatsService statsService;

    @PostMapping("/stats")
    public StatsDto get(@RequestBody RequestStatsDto requestStats) {
        return this.statsService.getStats(requestStats);
    }
}
