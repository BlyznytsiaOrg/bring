package io.github.blyznytsiaorg.bring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Annotation used to mark a method as a scheduled task. This annotation is part of the Bring Framework's scheduling
 * support, allowing the method to be automatically invoked at specified intervals.
 *
 * <p>The annotated method must adhere to the requirements for a scheduled task method, including having no parameters
 * and a void return type.
 *
 * <p>You can customize the scheduling behavior by providing values for the {@code value}, {@code initialDelay},
 * {@code period}, and {@code timeUnit} attributes. If not specified, the default values will be used.
 *
 *
 * <p><strong>Usage Example:</strong>
 * <pre>
 * {@code
 *  @Service
 *  public class MyScheduledService {
 *
 *      @ScheduledTask(value = "myTask", initialDelay = 1000, period = 5000, timeUnit = TimeUnit.MILLISECONDS)
 *      public void myScheduledMethod() {
 *          // Task logic
 *      }
 *  }
 * }
 * </pre>
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledTask {
    /**
     * The name of the scheduled task. If not specified, the method name will be used as the task name.
     *
     * @return the name of the scheduled task
     */
    String value() default "";
    /**
     * The initial delay before the first execution of the task, in milliseconds.
     *
     * @return the initial delay in milliseconds
     */
    long initialDelay() default 1000;
    /**
     * The fixed rate between consecutive executions of the task, in milliseconds.
     *
     * @return the period between executions in milliseconds
     */
    long period() default 5000;
    /**
     * The time unit for the initial delay and period attributes. Defaults to {@code TimeUnit.MILLISECONDS}.
     *
     * @return the time unit for the initial delay and period
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
