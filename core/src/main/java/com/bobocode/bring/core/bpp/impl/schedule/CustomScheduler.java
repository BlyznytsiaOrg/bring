package com.bobocode.bring.core.bpp.impl.schedule;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomScheduler {

    private final ScheduledExecutorService scheduledExecutorService;

    public CustomScheduler(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public void scheduleTask(Runnable command,  long initialDelay, long period, TimeUnit timeUnit) {
        scheduledExecutorService.scheduleAtFixedRate(command, initialDelay, period, timeUnit);
    }

    public void shutdown() {
        scheduledExecutorService.shutdown();
    }

    public void shutdownNow() {
        scheduledExecutorService.shutdownNow();
    }
}
