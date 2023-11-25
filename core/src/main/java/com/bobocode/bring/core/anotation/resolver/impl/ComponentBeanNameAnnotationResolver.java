package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.BeanProcessor;
import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Qualifier;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

/**
 * An implementation of AnnotationResolver specifically designed for resolving
 * component bean names based on the Component annotation.
 * This resolver checks for the presence of the Component annotation on a class
 * and extracts the associated value or default class name as the bean name.
 * <p>
 * Example usage:
 * This resolver can be used in a framework or system where component names
 * are defined using the Component annotation, allowing automatic resolution
 * of bean names based on annotated classes.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@BeanProcessor
public class ComponentBeanNameAnnotationResolver implements AnnotationResolver {

    /**
     * Checks if the resolver supports handling annotations on the specified class.
     *
     * @param clazz the class to check for Component annotation
     * @return true if the Component annotation is present on the class; false otherwise
     */
    @Override
    public boolean isSupported(Class<?> clazz) {
        return clazz.getAnnotation(Component.class) != null;
    }

    /**
     * Resolves the component bean name based on the Component annotation.
     * If the Component annotation includes a value, that value is returned as the bean name.
     * Otherwise, the simple class name is used as the default bean name.
     *
     * @param clazz the class for which to resolve the component bean name
     * @return the resolved component bean name
     */
    @Override
    public String resolve(Class<?> clazz) {
        String value = clazz.getAnnotation(Component.class).value();
        String qualifier = getQualifier(clazz);
        // qualifier -> Component.value -> className
        return qualifier != null ? qualifier : value.isEmpty() ? clazz.getSimpleName() : value;
    }

    private String getQualifier (Class<?> clazz) {
        return clazz.isAnnotationPresent(Qualifier.class) ? clazz.getAnnotation(Qualifier.class).value() : null;
    }
}

