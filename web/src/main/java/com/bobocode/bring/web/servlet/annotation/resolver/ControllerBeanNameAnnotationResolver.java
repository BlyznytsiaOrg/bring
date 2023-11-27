package com.bobocode.bring.web.servlet.annotation.resolver;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.annotation.resolver.AnnotationResolver;
import com.bobocode.bring.web.servlet.annotation.Controller;


/**
 * An implementation of AnnotationResolver used for resolving bean names associated with classes annotated with @Controller.
 * This resolver checks for the presence of the Controller annotation on a class
 * and returns the simple class name as the resolved bean name.
 * <p>
 * Example usage:
 * This resolver is intended to handle classes annotated with @Controller,
 * providing a straightforward resolution by using the class's simple name as the bean name.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@BeanProcessor
public class ControllerBeanNameAnnotationResolver implements AnnotationResolver {

    /**
     * Checks if the resolver supports handling annotations on the specified class.
     *
     * @param clazz the class to check for Controller annotation
     * @return true if the Controller annotation is present on the class; false otherwise
     */
    @Override
    public boolean isSupported(Class<?> clazz) {
        return  clazz.getAnnotation(Controller.class) != null;
    }

    /**
     * Resolves the bean name based on the @Controller annotation.
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
