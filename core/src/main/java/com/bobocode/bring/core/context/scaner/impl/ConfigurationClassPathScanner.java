package com.bobocode.bring.core.context.scaner.impl;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.annotation.Configuration;
import com.bobocode.bring.core.context.scaner.ClassPathScanner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

@BeanProcessor
@AllArgsConstructor
@Slf4j
public class ConfigurationClassPathScanner implements ClassPathScanner {

    private final Reflections reflections;
    
    @Override
    public Set<Class<?>> scan() {
        return reflections.getTypesAnnotatedWith(getAnnotation());
    }

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return Configuration.class;
    }

}
