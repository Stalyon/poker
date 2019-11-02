package com.stalyon.poker.service;

import com.stalyon.poker.PokerApp;
import com.stalyon.poker.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for {@link MailService}.
 */
@SpringBootTest(classes = PokerApp.class)
public class LaunchServiceIT {

    @Autowired
    private LaunchService launchService;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void proceedTest() throws Exception {
        this.launchService.proceed();
    }
}
