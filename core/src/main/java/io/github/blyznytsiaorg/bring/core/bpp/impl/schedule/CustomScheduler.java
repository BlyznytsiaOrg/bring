package io.github.blyznytsiaorg.bring.core.bpp.impl.schedule;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Represents a custom scheduler utilizing a ScheduledExecutorService for task scheduling.
 * This class wraps a ScheduledExecutorService and provides methods to schedule tasks,
 * as well as to shut down the underlying executor service.
 * <p>
 * Example usage:
 * Instances of this class can be used to schedule tasks with a defined initial delay,
 * period, and time unit using the wrapped ScheduledExecutorService.
 *
 * @see java.util.concurrent.ScheduledExecutorService
 * @see java.util.concurrent.TimeUnit
 */
public class CustomScheduler {

    /** The wrapped ScheduledExecutorService for task scheduling. */
    private final ScheduledExecutorService scheduledExecutorService;

    /**
     * Constructs a CustomScheduler with a provided ScheduledExecutorService instance.
     *
     * @param scheduledExecutorService the ScheduledExecutorService to be used for scheduling tasks
     */
    public CustomScheduler(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    /**
     * Schedules a task to run at a fixed rate defined by the initial delay, period, and time unit.
     *
     * @param command      the task to be executed
     * @param initialDelay the initial delay before the first execution
     * @param period       the period between successive executions
     * @param timeUnit     the time unit for the initial delay and period
     * @see java.util.concurrent.ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)
     */
    public void scheduleTask(Runnable command,  long initialDelay, long period, TimeUnit timeUnit) {
        scheduledExecutorService.scheduleAtFixedRate(command, initialDelay, period, timeUnit);
    }

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed,
     * but no new tasks will be accepted.
     *
     * @see java.util.concurrent.ExecutorService#shutdown()
     */
    public void shutdown() {
        scheduledExecutorService.shutdown();
    }

    /**
     * Attempts to stop all actively executing tasks, halts the processing of waiting tasks,
     * and returns a list of the tasks that were awaiting execution.
     *
     * @see java.util.concurrent.ExecutorService#shutdownNow()
     */
    public void shutdownNow() {
        scheduledExecutorService.shutdownNow();
    }
}
