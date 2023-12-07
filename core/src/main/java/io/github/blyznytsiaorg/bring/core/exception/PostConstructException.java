package io.github.blyznytsiaorg.bring.core.exception;

/**
 * Exception thrown when method annotated with @PostConstruct violates rules of usage such as have parameters.
 * Extends RuntimeException.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class PostConstructException extends RuntimeException {
    public static final String POST_CONSTRUCT_EXCEPTION_MESSAGE = "@PostConstruct should be added to method without parameters";

    /**
     * Constructs a PostConstructException with the specified message.
     *
     * @param cause The Throwable cause for this exception
     */
    public PostConstructException(Throwable cause) {
        super(POST_CONSTRUCT_EXCEPTION_MESSAGE, cause);
    }
}
