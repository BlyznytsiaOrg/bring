package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.anotation.BeanProcessor;
import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;


/**
 * Implementation of ClassPathScanner that scans for classes annotated with @Service using Reflections.
 * This scanner uses the Reflections library to scan the classpath and retrieve classes annotated with @Component.
 *
 * @see ClassPathScanner
 * @see Service
 * @see org.reflections.Reflections
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@BeanProcessor
@AllArgsConstructor
@Slf4j
public class ServiceClassPathScanner implements ClassPathScanner {

    /*The Reflections instance used for scanning classes. */
    private final Reflections reflections;

    /**
     * Scans the classpath to retrieve classes annotated with @Service.
     *
     * @return a set of classes annotated with @Service
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
        return Service.class;
    }

}
