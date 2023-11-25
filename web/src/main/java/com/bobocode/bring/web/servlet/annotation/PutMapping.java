package com.bobocode.bring.web.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP {@code PUT} requests onto specific handler
 * methods.
 *
 * @see GetMapping
 * @see PostMapping
 * @see DeleteMapping
 * @see PatchMapping
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PutMapping {

    /**
     * The URL path or pattern for mapping HTTP PUT requests to the annotated method.
     *
     * @return the URL path or pattern for PUT requests
     */
    String path() default "";
}
