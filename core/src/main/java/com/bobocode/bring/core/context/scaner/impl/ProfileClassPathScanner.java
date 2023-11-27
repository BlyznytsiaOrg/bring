package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.annotation.Profile;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;


/**
 * Implementation of ClassPathScanner that scans for classes annotated with @Profile using Reflections.
 * This scanner uses the Reflections library to scan the classpath and retrieve classes annotated with @Component.
 *
 * @see ClassPathScanner
 * @see Profile
 * @see org.reflections.Reflections
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@BeanProcessor
@AllArgsConstructor
@Slf4j
public class ProfileClassPathScanner implements ClassPathScanner {

    private final Reflections reflections;

    /**
     * Scans the classpath to retrieve classes annotated with @Profile.
     *
     * @return a set of classes annotated with @Profile
     */
    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(getAnnotation());
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return Profile.class;
    }
}
