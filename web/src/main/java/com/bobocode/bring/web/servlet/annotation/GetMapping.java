package com.bobocode.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP {@code GET} requests onto specific handler
 * methods.
 *
 * @see DeleteMapping
 * @see PostMapping
 * @see PutMapping
 * @see PatchMapping
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {

    /**
     * The URL path or pattern for mapping HTTP GET requests to the annotated method.
     *
     * @return the URL path or pattern for GET requests
     */
    String path() default "";
}
