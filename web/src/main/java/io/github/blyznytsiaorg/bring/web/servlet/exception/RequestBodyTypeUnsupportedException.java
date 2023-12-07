package io.github.blyznytsiaorg.bring.web.servlet.exception;

import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestBody;

/**
 * The RequestBodyTypeUnsupportedException class is a runtime exception that indicates
 * an unsupported type for the @RequestBody annotation.
 *
 * @see RequestBody
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class RequestBodyTypeUnsupportedException extends RuntimeException {
    public RequestBodyTypeUnsupportedException(String message) {
        super(message);
    }
}
