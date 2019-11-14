package com.stalyon.poker.web.rest;

import com.stalyon.poker.service.DatasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * REST controller.
 */
@RestController
@RequestMapping("/api")
public class DatasResource {

    @Autowired
    private DatasService datasService;

    @GetMapping("/datas")
    public void update() throws IOException {
        this.datasService.update();
    }
}
