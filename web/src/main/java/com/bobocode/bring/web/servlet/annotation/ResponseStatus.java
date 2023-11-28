package com.bobocode.bring.web.servlet.annotation;

import com.bobocode.bring.web.servlet.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking a class or method with the desired HTTP response status code and optional reason.
 * When applied to a method or exception, the specified HTTP status code and reason will be used for the response.
 * When both {@code ResponseEntity} and {@code @ResponseStatus} are used in a controller method,
 * the HTTP status from {@code ResponseEntity} takes precedence over the one specified by {@code @ResponseStatus}.
 * <p>
 * Example usage:
 * <p>
 * 1. Applied to a method:
 * <p>
 *    {@code
 *    @ResponseStatus(HttpStatus.CREATED)
 *    public String createResource() {
 *        // Method implementation
 *    }
 *    }
 *
 *    This sets the HTTP response status code to 201 (Created) for the annotated method.
 * <p>
 * 2. Custom Exception Usage:
 * <p>
 *    {@code
 *    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
 *    public class CustomAppException extends RuntimeException {
 * <p>
 *        // Constructors...
 * <p>
 *    }
 *    }
 *
 *    {@code
 *    @GetMapping("/example")
 *    public String exampleEndpoint() {
 *        // Some logic that might throw the custom exception
 *        if (true) {
 *            throw new CustomAppException("Custom exception occurred.");
 *        }
 * <p>
 *        // Rest of the method logic
 *        return "Success";
 *    }
 *    }
 *
 *    In this example, if the condition is met and the CustomAppException is thrown, the response will have a
 *    status of 500 (INTERNAL_SERVER_ERROR). You can customize the CustomAppException class to include any
 *    additional information you need in your application-specific exceptions.
 * <p>
 * Note: If a reason is provided, it will be included in the response alongside the status code.
 *
 * @see com.bobocode.bring.web.servlet.http.ResponseEntity
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ResponseStatus {

    /**
     * The HTTP response status code.
     *
     * @return The HTTP status code (default is INTERNAL_SERVER_ERROR).
     */
    HttpStatus value() default HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * The optional reason associated with the response status.
     *
     * @return The reason for the response status (default is an empty string).
     */
    String reason() default "";
}
