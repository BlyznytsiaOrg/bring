package com.bobocode.bring.core.bpp.impl.schedule;

import com.bobocode.bring.core.annotation.Bean;
import com.bobocode.bring.core.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Configures a custom schedule with a specific core pool size for executing scheduled tasks.
 * This class sets up a scheduled executor service with a defined core pool size and creates
 * a custom scheduler based on this executor service.
 * <p>
 * Example usage:
 * This configuration class can be used in applications requiring custom scheduling of tasks,
 * providing a configured scheduled executor service and a custom scheduler instance.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Configuration
public class CustomScheduleConfiguration {

    /** The core pool size for the scheduled executor service. */
    private static final int CORE_POOL_SIZE = 1;

    /**
     * Creates and returns a scheduled executor service with the defined core pool size.
     *
     * @return a ScheduledExecutorService instance
     * @see java.util.concurrent.Executors#newScheduledThreadPool(int)
     */
    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(CORE_POOL_SIZE);
    }

    /**
     * Creates and returns a custom scheduler based on the scheduled executor service.
     *
     * @return a CustomScheduler instance using the configured executor service
     * @see CustomScheduler
     */
    @Bean
    public CustomScheduler customScheduler() {
        return new CustomScheduler(scheduledExecutorService());
    }
}
