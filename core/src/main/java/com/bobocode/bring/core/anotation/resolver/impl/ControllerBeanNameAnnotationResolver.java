package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.Controller;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

public class ControllerBeanNameAnnotationResolver implements AnnotationResolver {
    @Override
    public boolean isSupported(Class<?> clazz) {
        return  clazz.getAnnotation(Controller.class) != null;
    }

    @Override
    public String resolve(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
}
