package com.bobocode.bring.core.bpp.impl.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CustomScheduleConfiguration {

    private static final int CORE_POOL_SIZE = 1;

    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(CORE_POOL_SIZE);
    }

    public CustomScheduler customScheduler() {
        return new CustomScheduler(scheduledExecutorService());
    }
}
