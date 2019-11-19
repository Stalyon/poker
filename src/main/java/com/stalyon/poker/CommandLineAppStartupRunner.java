package com.stalyon.poker;

import com.stalyon.poker.service.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

    @Autowired
    private StatsService statsService;

    @Override
    public void run(String...args) throws Exception {
        log.info("Updating statistics");
        this.statsService.update();
    }
}

