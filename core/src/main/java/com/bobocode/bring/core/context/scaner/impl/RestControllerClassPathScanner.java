package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Set;


/**
 * Implementation of ClassPathScanner that scans for classes annotated with @RestController using Reflections.
 * This scanner uses the Reflections library to scan the classpath and retrieve classes annotated with @Component.
 *
 * @see ClassPathScanner
 * @see RestController
 * @see org.reflections.Reflections
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@AllArgsConstructor
@Slf4j
public class RestControllerClassPathScanner implements ClassPathScanner {

    /** The Reflections instance used for scanning classes. */
    private final Reflections reflections;

    /**
     * Scans the classpath to retrieve classes annotated with @RestController.
     *
     * @return a set of classes annotated with @RestController
     */
    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(RestController.class);
    }
    
}
