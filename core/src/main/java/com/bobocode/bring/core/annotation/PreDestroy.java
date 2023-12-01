package com.bobocode.bring.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>The {@code PreDestroy} annotation is used to signal that the annotated method
 * should be executed before the container removes the instance of the class
 * in which the method is declared. This is typically used for cleanup operations
 * or releasing resources held by the instance. </p>
 * <p>
 * The annotated method must have no parameters, must be non-static and return void.
 * It is invoked before the bean is destroyed, allowing the bean to perform necessary
 * cleanup tasks.
 * If multiple methods are annotated with {@code @PostConstruct} within a single
 * class, the order of execution is not guaranteed.</p>
 *
 * <p>Example of usage:</p>
 * <pre>
 * {@code
 *
 *    @PreDestroy
 *    public void cleanup() {
 *        // Perform cleanup operations or release resources here
 *    }
 *
 * }</pre>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreDestroy {

}
