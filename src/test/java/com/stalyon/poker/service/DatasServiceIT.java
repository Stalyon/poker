package com.stalyon.poker.service;

import com.stalyon.poker.PokerApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for {@link DatasService}.
 */
@SpringBootTest(classes = PokerApp.class)
public class DatasServiceIT {

    @Autowired
    private DatasService datasService;

    @Test
    public void proceedTest() throws Exception {
        this.datasService.update();
    }
}
