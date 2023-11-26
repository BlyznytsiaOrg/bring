package com.bobocode.bring.web.servlet.exception;

/**
 * The MissingRequestParamException class is a runtime exception that indicates
 * the absence of a required request parameter for a method parameter type annotated
 * with {@code @RequestParam}.
 *
 * @see com.bobocode.bring.web.servlet.annotation.RequestParam
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class MissingRequestParamException extends RuntimeException {
    public MissingRequestParamException(String message) {
        super(message);
    }
}
