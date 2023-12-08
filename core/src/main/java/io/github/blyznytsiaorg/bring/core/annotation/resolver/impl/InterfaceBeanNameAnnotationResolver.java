package io.github.blyznytsiaorg.bring.core.annotation.resolver.impl;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.annotation.resolver.AnnotationResolver;

/**
 * An implementation of AnnotationResolver designed for resolving bean names associated with interfaces.
 * This resolver checks if the provided class is an interface and returns its simple name as the resolved bean name.
 * <p>
 * Example usage:
 * This resolver is tailored to handle interfaces, providing a simple resolution mechanism
 * that utilizes the interface's simple name as the bean name.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@BeanProcessor
public class InterfaceBeanNameAnnotationResolver implements AnnotationResolver {

    /**
     * Checks if the resolver supports handling annotations on the specified class.
     * Determines support based on whether the provided class is an interface.
     *
     * @param clazz the class to check for being an interface
     * @return true if the class is an interface; false otherwise
     */
    @Override
    public boolean isSupported(Class<?> clazz) {
        return clazz.isInterface();
    }

    /**
     * Resolves the bean name for the provided interface.
     * Returns the simple name of the interface as the bean name.
     *
     * @param clazz the interface class for which to resolve the bean name
     * @return the resolved bean name (simple interface name)
     */
    @Override
    public String resolve(Class<?> clazz) {
        return getSimpleName(clazz);
    }
}
