package com.bobocode.bring.web.servlet.exception;

/**
 * The MethodArgumentTypeMismatchException class is a runtime exception that indicates
 * a failure to convert a method argument to the required type.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class MethodArgumentTypeMismatchException extends RuntimeException {
    public MethodArgumentTypeMismatchException(String message) {
        super(message);
    }
}
