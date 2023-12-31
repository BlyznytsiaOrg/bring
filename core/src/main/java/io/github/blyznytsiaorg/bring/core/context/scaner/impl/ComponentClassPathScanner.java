package io.github.blyznytsiaorg.bring.core.context.scaner.impl;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.annotation.Component;
import io.github.blyznytsiaorg.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Implementation of ClassPathScanner that scans for classes annotated with @Component using Reflections.
 * This scanner uses the Reflections library to scan the classpath and retrieve classes annotated with @Component.
 *
 * @see ClassPathScanner
 * @see Component
 * @see org.reflections.Reflections
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@BeanProcessor
@AllArgsConstructor
@Slf4j
public class ComponentClassPathScanner implements ClassPathScanner {

    /** The Reflections instance used for scanning classes. */
    private final Reflections reflections;

    /**
     * Scans the classpath to retrieve classes annotated with @Component.
     *
     * @return a set of classes annotated with @Component
     */
    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(getAnnotation());
    }

    /**
     * Retrieves the annotation type scanned by this class path scanner.
     *
     * @return The Class object representing the annotation scanned by this class path scanner.
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return Component.class;
    }
}
