package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import org.reflections.Reflections;

import java.util.Set;

@AllArgsConstructor
public abstract class AbstractClassPathScanner implements ClassPathScanner  {

    /** The Reflections instance used for scanning classes. */
    private final Reflections reflections;

    /**
     * Scans the classpath to retrieve classes annotated with @Component.
     *
     * @return a set of classes annotated with getAnnotation()
     */
    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(getAnnotation());
    }
}
