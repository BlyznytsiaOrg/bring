package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;


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
@Slf4j
public class RestControllerClassPathScanner extends AbstractClassPathScanner {


    public RestControllerClassPathScanner(Reflections reflections) {
        super(reflections);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return RestController.class;
    }
}
