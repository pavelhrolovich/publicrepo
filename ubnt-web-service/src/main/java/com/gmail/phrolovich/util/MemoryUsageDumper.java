package com.gmail.phrolovich.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MemoryUsageDumper {

    @Scheduled(fixedRate = 60000)
    public void printMemoryUsage() {
        double usedMemoryKb = (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
        double freeMemoryKb = (double) (Runtime.getRuntime().freeMemory()) / 1024;
        double totalMemoryKb = (double) (Runtime.getRuntime().totalMemory()) / 1024;
        log.info("Used " + usedMemoryKb + " of " + totalMemoryKb + ", available memory: " + freeMemoryKb );
    }
}
