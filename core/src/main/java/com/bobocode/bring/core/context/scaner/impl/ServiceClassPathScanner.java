package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;


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
@Slf4j
public class ServiceClassPathScanner extends AbstractClassPathScanner {

    public ServiceClassPathScanner(Reflections reflections) {
        super(reflections);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return Service.class;
    }
}
