package com.stalyon.poker.web.rest;


import com.stalyon.poker.service.LaunchService;
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
public class LaunchResource {

    @Autowired
    private LaunchService launchService;

    @GetMapping("/launch")
    public String getAllActions() {
        try {
            this.launchService.proceed();
            return "izOk";
        } catch (IOException e) {
            return "izNotOk";
        }
    }
}
