package io.github.blyznytsiaorg.bring.core.exception;

/**
 * Exception thrown to indicate limitations or issues during the construction of a BeanPostProcessor
 * within a Bring Dependency Injection framework.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class BeanPostProcessorConstructionLimitationException extends RuntimeException {
    /**
     * Constructs a new BeanPostProcessorConstructionLimitationException with the specified detail message.
     *
     * @param message The detail message describing the limitation or issue encountered
     */
    public BeanPostProcessorConstructionLimitationException(String message) {
        super(message);
    }
}
