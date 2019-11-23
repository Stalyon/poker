package com.stalyon.poker;

import com.stalyon.poker.service.DatasService;
import com.stalyon.poker.service.StatsService;
import com.stalyon.poker.service.WatcherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

    @Autowired
    private DatasService datasService;

    @Autowired
    private StatsService statsService;

    @Autowired
    private WatcherService watcherService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Updating statistics");
        this.statsService.update();

        log.info("Updating datas");
        this.datasService.update();

        log.info("Launching watcher for stats");
        this.watcherService.launchStatsWatcher();

        log.info("Launching watcher for live mode");
        this.watcherService.launchLiveWatcher();
    }
}

