package io.github.blyznytsiaorg.bring.web.servlet.exception;

import io.github.blyznytsiaorg.bring.web.servlet.annotation.RequestParam;

/**
 * The MissingRequestParamException class is a runtime exception that indicates
 * the absence of a required request parameter for a method parameter type annotated
 * with {@code @RequestParam}.
 *
 * @see RequestParam
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class MissingRequestParamException extends RuntimeException {
    public MissingRequestParamException(String message) {
        super(message);
    }
}
