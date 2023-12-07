package io.github.blyznytsiaorg.bring.core.context.scaner.impl;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.annotation.Configuration;
import io.github.blyznytsiaorg.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * This class implements the ClassPathScanner interface to scan for classes annotated with Configuration.
 * It uses Reflections to perform classpath scanning.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@BeanProcessor
@AllArgsConstructor
@Slf4j
public class ConfigurationClassPathScanner implements ClassPathScanner {

    private final Reflections reflections;

    /**
     * Scans the classpath for classes annotated with Configuration.
     *
     * @return A Set of Class objects representing types annotated with Configuration.
     */
    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(getAnnotation());
    }

    /**
     * Gets the annotation type used for scanning.
     *
     * @return The Class object representing the Configuration annotation.
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return Configuration.class;
    }

}
