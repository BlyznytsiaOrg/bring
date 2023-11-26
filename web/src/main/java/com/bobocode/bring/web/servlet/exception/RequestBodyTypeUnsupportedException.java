package com.bobocode.bring.web.servlet.exception;

/**
 * The RequestBodyTypeUnsupportedException class is a runtime exception that indicates
 * an unsupported type for the @RequestBody annotation.
 *
 * @see com.bobocode.bring.web.servlet.annotation.RequestBody
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class RequestBodyTypeUnsupportedException extends RuntimeException {
    public RequestBodyTypeUnsupportedException(String message) {
        super(message);
    }
}
