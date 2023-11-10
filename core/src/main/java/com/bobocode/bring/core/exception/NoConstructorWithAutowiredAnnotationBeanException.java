package com.bobocode.bring.core.exception;

public class NoConstructorWithAutowiredAnnotationBeanException extends RuntimeException {

    private static final String CONSTRUCTOR_NOT_FOUND = "No constructor with Autowired annotation for %s.";

    private static final String TWO_MANY_CONSTRUCTOR_FOUND = "No constructor with Autowired annotation for %s. \n Add Autowired to one of them %s";

    public <T> NoConstructorWithAutowiredAnnotationBeanException(Class<T> clazz, String listOfConstructors) {
        super(String.format(TWO_MANY_CONSTRUCTOR_FOUND, clazz, listOfConstructors));
    }

    public <T> NoConstructorWithAutowiredAnnotationBeanException(Class<T> clazz) {
        super(String.format(CONSTRUCTOR_NOT_FOUND, clazz));
    }
}
