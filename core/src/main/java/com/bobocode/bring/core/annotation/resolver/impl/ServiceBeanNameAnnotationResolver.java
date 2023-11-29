package com.bobocode.bring.core.annotation.resolver.impl;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.annotation.Service;
import com.bobocode.bring.core.annotation.resolver.AnnotationResolver;

/**
 * An implementation of AnnotationResolver specifically designed for resolving
 * bean names associated with classes annotated with @Service.
 * This resolver checks for the presence of the Service annotation on a class
 * and extracts the associated value or default class name as the bean name.
 * <p>
 * Example usage:
 * This resolver is tailored to handle classes annotated with @Service,
 * allowing extraction of a specified value from the annotation or using the class's simple name as the default bean name.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@BeanProcessor
public class ServiceBeanNameAnnotationResolver implements AnnotationResolver {

    /**
     * Checks if the resolver supports handling annotations on the specified class.
     *
     * @param clazz the class to check for Service annotation
     * @return true if the Service annotation is present on the class; false otherwise
     */
    @Override
    public boolean isSupported(Class<?> clazz) {
        return  clazz.getAnnotation(Service.class) != null;
    }

    /**
     * Resolves the bean name based on the @Service annotation.
     * If the Service annotation includes a value, that value is returned as the bean name.
     * Otherwise, the simple class name is used as the default bean name.
     *
     * @param clazz the class for which to resolve the bean name
     * @return the resolved bean name
     */
    @Override
    public String resolve(Class<?> clazz) {
        String value = clazz.getAnnotation(Service.class).value();
        return value.isEmpty() ? getSimpleName(clazz) : value;
    }
    
}
