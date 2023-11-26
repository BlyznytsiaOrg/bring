package com.bobocode.bring.web.servlet.annotation;

import com.bobocode.bring.web.servlet.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate the desired HTTP response status for a controller method
 * or a controller class.
 *
 * <p>
 * If the {@code value} attribute is not specified, the default response status is
 * {@code HttpStatus.INTERNAL_SERVER_ERROR}.
 * </p>
 *
 * @see HttpStatus
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ResponseStatus {
    HttpStatus value() default HttpStatus.INTERNAL_SERVER_ERROR;

    String reason() default "";
}
