package com.bobocode.bring.core.annotation.resolver.impl;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.annotation.Configuration;
import com.bobocode.bring.core.annotation.resolver.AnnotationResolver;

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
