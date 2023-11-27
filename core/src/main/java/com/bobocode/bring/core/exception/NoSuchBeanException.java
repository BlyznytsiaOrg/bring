package com.bobocode.bring.core.exception;

/**
 * Exception thrown to indicate that a requested bean does not exist within a Bring Dependency Injection framework.
 */
public class NoSuchBeanException extends RuntimeException{

    private static final String MESSAGE = "Not such bean found exception %s";

    /**
     * Constructs a new NoSuchBeanException with a message indicating the absence of a bean of the specified type.
     *
     * @param clazz The class type for which the bean is not found
     * @param <T>   The class type
     */
    public <T> NoSuchBeanException(Class<T> clazz) {
        super(String.format(MESSAGE, clazz));
    }

    /**
     * Constructs a new NoSuchBeanException with a custom detail message.
     *
     * @param message The custom message explaining the exception
     */
    public NoSuchBeanException(String message) {
        super(message);
    }
}
