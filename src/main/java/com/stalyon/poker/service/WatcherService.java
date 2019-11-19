package com.stalyon.poker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

@Component
public class WatcherService {

    private static final Logger log = LoggerFactory.getLogger(WatcherService.class);

    private boolean isRunning;

    @Value("${poker.histo-path}")
    private String histoPath;

    @Autowired
    private DatasService datasService;

    @Async
    public void launchWatcher() {
        Path filePath = Paths.get(this.histoPath);

        WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();

            filePath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY);
            isRunning = true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return;
        }

        while (isRunning) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path path = (Path) event.context();

                if ((kind.equals(StandardWatchEventKinds.ENTRY_CREATE)
                        || kind.equals(StandardWatchEventKinds.ENTRY_MODIFY))
                    && path.toFile().getName().endsWith(".txt")
                    && !path.toFile().getName().endsWith("_summary.txt")) {
                    try {
                        this.datasService.treatFile(path.toFile().getName(), true);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
            key.reset();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }
}
