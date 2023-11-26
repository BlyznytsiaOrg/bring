package com.bobocode.bring.web.servlet.exception;

/**
 * The MissingBringServletImplException class is a runtime exception that indicates
 * the absence of the implementation of the BringServlet interface in a RestController.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class MissingBringServletImplException extends RuntimeException {

    public MissingBringServletImplException(String message) {
        super(message);
    }
}
