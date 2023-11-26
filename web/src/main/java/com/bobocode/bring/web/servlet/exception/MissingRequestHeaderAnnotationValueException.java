package com.bobocode.bring.web.servlet.exception;

/**
 * The MissingRequestHeaderAnnotationValueException class is a runtime exception that indicates
 * the absence of a required value for the {@code @RequestHeader} in a method parameter.
 *
 * @see com.bobocode.bring.web.servlet.annotation.RequestHeader
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class MissingRequestHeaderAnnotationValueException extends RuntimeException {
    public MissingRequestHeaderAnnotationValueException(String message) {
        super(message);
    }
}
