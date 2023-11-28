package com.bobocode.bring.core.exception;

/**
 * Exception thrown when a required annotation is missing on a bean.
 * Extends RuntimeException.
 */
public class BeanAnnotationMissingException extends RuntimeException {

    /**
     * Constructs a BeanAnnotationMissingException with the specified message.
     *
     * @param message The detail message for the exception.
     * @param args    Arguments to be formatted into the message string.
     */
    public BeanAnnotationMissingException(String message, Object... args) {
        super(String.format(message, args));
    }
    
}
