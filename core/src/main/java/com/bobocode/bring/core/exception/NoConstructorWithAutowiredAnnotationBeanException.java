package com.bobocode.bring.core.exception;

/**
 * Exception thrown to indicate that no constructor with the Autowired annotation is found for a bean class
 * within a Bring Dependency Injection framework.
 */
public class NoConstructorWithAutowiredAnnotationBeanException extends RuntimeException {

    private static final String TWO_MANY_CONSTRUCTOR_FOUND = "No constructor with Autowired annotation for %s. \n Add Autowired to one of them %s";

    /**
     * Constructs a new NoConstructorWithAutowiredAnnotationBeanException with a message indicating the absence
     * of a constructor with the Autowired annotation for the specified class.
     *
     * @param clazz             The class for which no constructor with Autowired annotation is found
     * @param listOfConstructors String representation of the list of constructors found for the class
     *                          (to guide adding Autowired annotation to one of them)
     * @param <T>               The class type
     */
    public <T> NoConstructorWithAutowiredAnnotationBeanException(Class<T> clazz, String listOfConstructors) {
        super(String.format(TWO_MANY_CONSTRUCTOR_FOUND, clazz, listOfConstructors));
    }
}
