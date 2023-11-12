package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

public class ServiceBeanNameAnnotationResolver implements AnnotationResolver {
    @Override
    public boolean isSupported(Class<?> clazz) {
        return  clazz.getAnnotation(Service.class) != null;
    }

    @Override
    public String resolve(Class<?> clazz) {
        String value = clazz.getAnnotation(Service.class).value();
        return value.isEmpty() ? clazz.getSimpleName() : value;
    }
    
}
