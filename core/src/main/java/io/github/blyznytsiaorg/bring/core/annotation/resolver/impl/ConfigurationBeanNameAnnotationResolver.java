package io.github.blyznytsiaorg.bring.core.annotation.resolver.impl;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.annotation.Configuration;
import io.github.blyznytsiaorg.bring.core.annotation.resolver.AnnotationResolver;

/**
 * This class implements the AnnotationResolver interface to resolve
 * the name of a configuration bean based on the presence of the Configuration annotation.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@BeanProcessor
public class ConfigurationBeanNameAnnotationResolver implements AnnotationResolver {

    /**
     * Checks if the provided class is supported for resolving.
     *
     * @param clazz The class to be checked for support.
     * @return {@code true} if the class has the Configuration annotation, {@code false} otherwise.
     */
    @Override
    public boolean isSupported(Class<?> clazz) {
        return clazz.getAnnotation(Configuration.class) != null;
    }

    /**
     * Resolves the name of the configuration bean based on the provided class.
     *
     * @param clazz The class for which the name of the configuration bean needs to be resolved.
     * @return A String representing the simple name of the class.
     */
    @Override
    public String resolve(Class<?> clazz) {
        return getSimpleName(clazz);
    }
    
}
