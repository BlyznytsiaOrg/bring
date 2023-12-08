package io.github.blyznytsiaorg.bring.core.exception;

/**
 * General-purpose exception used to wrap and propagate other exceptions within a system.
 * Extends RuntimeException and allows passing a cause (Throwable) to provide context.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class BringGeneralException extends RuntimeException {

    /**
     * Constructs a new BringGeneralException with the specified cause.
     *
     * @param cause The Throwable cause for this exception
     */
    public BringGeneralException(Throwable cause) {
        super(cause);
    }
    
}
