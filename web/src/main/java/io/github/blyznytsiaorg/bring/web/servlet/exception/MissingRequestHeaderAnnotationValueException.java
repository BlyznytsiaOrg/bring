package io.github.blyznytsiaorg.bring.web.servlet.exception;

import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestHeader;

/**
 * The MissingRequestHeaderAnnotationValueException class is a runtime exception that indicates
 * the absence of a required value for the {@code @RequestHeader} in a method parameter.
 *
 * @see RequestHeader
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class MissingRequestHeaderAnnotationValueException extends RuntimeException {
    public MissingRequestHeaderAnnotationValueException(String message) {
        super(message);
    }
}
