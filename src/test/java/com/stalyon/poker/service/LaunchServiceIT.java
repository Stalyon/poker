package com.stalyon.poker.service;

import com.stalyon.poker.PokerApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for {@link LaunchService}.
 */
@SpringBootTest(classes = PokerApp.class)
public class LaunchServiceIT {

    @Autowired
    private LaunchService launchService;

    @Test
    public void proceedTest() throws Exception {
        this.launchService.proceed();
    }
}
