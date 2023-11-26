package com.bobocode.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP {@code PATCH} requests onto specific handler
 * methods.
 *
 * @see GetMapping
 * @see PostMapping
 * @see PutMapping
 * @see DeleteMapping
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PatchMapping {

    /**
     * The URL path or pattern for mapping HTTP PATCH requests to the annotated method.
     *
     * @return the URL path or pattern for PATCH requests
     */
    String path() default "";
}
