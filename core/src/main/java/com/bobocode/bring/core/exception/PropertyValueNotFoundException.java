package com.bobocode.bring.core.exception;

/**
 * Exception thrown to indicate that a property value is not found within a Bring Dependency Injection framework.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class PropertyValueNotFoundException extends RuntimeException {

    /**
     * Constructs a new PropertyValueNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining the specific property value not being found
     */
    public PropertyValueNotFoundException(String message) {
        super(message);
    }
}
