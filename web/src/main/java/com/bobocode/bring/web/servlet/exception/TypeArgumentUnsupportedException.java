package com.bobocode.bring.web.servlet.exception;

/**
 * The TypeArgumentUnsupportedException class is a runtime exception that indicates
 * the usage of an unsupported type argument.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class TypeArgumentUnsupportedException extends RuntimeException {

    public TypeArgumentUnsupportedException(String message) {
        super(message);
    }
}
