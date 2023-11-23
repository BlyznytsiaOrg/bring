package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.anotation.Profile;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;


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
@Slf4j
public class ProfileClassPathScanner extends AbstractClassPathScanner {


    public ProfileClassPathScanner(Reflections reflections) {
        super(reflections);
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return Profile.class;
    }
}
