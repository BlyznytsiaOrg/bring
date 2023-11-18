package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.RestController;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

public class RestControllerBeanNameAnnotationResolver implements AnnotationResolver {
    @Override
    public boolean isSupported(Class<?> clazz) {
        return  clazz.getAnnotation(RestController.class) != null;
    }

    @Override
    public String resolve(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
}
