package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

/**
 * An implementation of AnnotationResolver specifically designed for resolving
 * bean names associated with classes annotated with @RestController.
 * This resolver checks for the presence of the RestController annotation on a class
 * and returns the simple class name as the resolved bean name.
 * <p>
 * Example usage:
 * This resolver is intended to handle classes annotated with @RestController,
 * providing a straightforward resolution by using the class's simple name as the bean name.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class RestControllerBeanNameAnnotationResolver implements AnnotationResolver {

    /**
     * Checks if the resolver supports handling annotations on the specified class.
     *
     * @param clazz the class to check for RestController annotation
     * @return true if the RestController annotation is present on the class; false otherwise
     */
    @Override
    public boolean isSupported(Class<?> clazz) {
        return  clazz.getAnnotation(RestController.class) != null;
    }

    /**
     * Resolves the bean name based on the @RestController annotation.
     * Returns the simple class name as the bean name.
     *
     * @param clazz the class for which to resolve the bean name
     * @return the resolved bean name (simple class name)
     */
    @Override
    public String resolve(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
}
