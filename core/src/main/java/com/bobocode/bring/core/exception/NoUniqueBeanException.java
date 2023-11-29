package com.bobocode.bring.core.exception;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exception thrown to indicate that no unique bean is available or multiple beans match the expected type
 * within a Bring Dependency Injection framework.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class NoUniqueBeanException extends RuntimeException {

    /**
     * Constructs a new NoUniqueBeanException with a message indicating that no unique bean of the specified type is available.
     *
     * @param clazz The class type for which no unique bean is found
     * @param <T>   The class type
     */
    public <T> NoUniqueBeanException(Class<T> clazz) {
        super(String.format("No unique bean exception %s", clazz));
    }

    /**
     * Constructs a new NoUniqueBeanException with a message indicating that a bean with the given name already exists.
     *
     * @param beanName The name of the bean that already exists
     */
    public NoUniqueBeanException(String beanName) {
        super(String.format("Bean with name %s already exists", beanName));
    }

    /**
     * Constructs a new NoUniqueBeanException with a message indicating that no qualifying bean of a certain type is available.
     *
     * @param clazz           The class type for which no qualifying bean is found
     * @param implementations The list of implementations found for the specified type
     * @param <T>             The class type
     */
    public <T> NoUniqueBeanException(Class<T> clazz, List<String> implementations) {
        super(String.format("No qualifying bean of type '%s' available: expected single matching bean but found %s: [%s]",
                clazz, implementations.size(), implementations.stream().sorted().collect(Collectors.joining(","))));
    }
}
