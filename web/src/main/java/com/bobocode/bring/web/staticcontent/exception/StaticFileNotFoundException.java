package com.bobocode.bring.web.staticcontent.exception;

import com.bobocode.bring.web.servlet.annotation.ResponseStatus;
import com.bobocode.bring.web.servlet.http.HttpStatus;

/**
 * Exception thrown when a static file is not found.
 * This exception is annotated with {@link ResponseStatus} to indicate the corresponding
 * HTTP status code (400 Bad Request).
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StaticFileNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code StaticFileNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     */
    public StaticFileNotFoundException(String message) {
        super(message);
    }

    public StaticFileNotFoundException(String message,
                                       Throwable cause,
                                       boolean enableSuppression,
                                       boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
