package com.stalyon.poker.web.rest;

import com.stalyon.poker.service.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller.
 */
@RestController
@RequestMapping("/api")
public class LiveResource {

    @Autowired
    private LiveService liveService;

    @GetMapping("/live/launch")
    public void launch() {
        this.liveService.launch();
    }

    @GetMapping("/live/stop")
    public void stop() {
        this.liveService.stop();
    }
}
