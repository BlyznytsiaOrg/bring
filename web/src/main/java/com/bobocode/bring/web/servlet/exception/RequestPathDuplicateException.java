package com.bobocode.bring.web.servlet.exception;

/**
 * The RequestPathDuplicateException class is a runtime exception that indicates
 * the presence of duplicate request paths in the application.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class RequestPathDuplicateException extends RuntimeException {
    public RequestPathDuplicateException(String message) {
        super(message);
    }
}
