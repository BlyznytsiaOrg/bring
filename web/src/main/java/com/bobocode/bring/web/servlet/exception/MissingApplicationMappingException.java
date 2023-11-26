package com.bobocode.bring.web.servlet.exception;

/**
 * The MissingApplicationMappingException class is a runtime exception that indicates
 * the absence of an explicit mapping for a particular request path in the application.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class MissingApplicationMappingException extends RuntimeException {

    public MissingApplicationMappingException(String message) {
        super(message);
    }
}
