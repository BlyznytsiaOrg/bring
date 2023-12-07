package io.github.blyznytsiaorg.bring.web.servlet.exception;

import io.github.blyznytsiaorg.bring.web.servlet.annotation.ResponseStatus;
import io.github.blyznytsiaorg.bring.web.servlet.http.HttpStatus;

/**
 * The MissingApplicationMappingException class is a runtime exception that indicates
 * the absence of an explicit mapping for a particular request path in the application.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MissingApplicationMappingException extends RuntimeException {

    public MissingApplicationMappingException(String message) {
        super(message);
    }

    public MissingApplicationMappingException(String message,
                                              Throwable cause,
                                              boolean enableSuppression,
                                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
