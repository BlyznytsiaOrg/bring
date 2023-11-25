package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.BeanProcessor;
import com.bobocode.bring.core.anotation.Configuration;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

@BeanProcessor
public class ConfigurationBeanNameAnnotationResolver implements AnnotationResolver {
    
    @Override
    public boolean isSupported(Class<?> clazz) {
        return clazz.getAnnotation(Configuration.class) != null;
    }

    @Override
    public String resolve(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
}
