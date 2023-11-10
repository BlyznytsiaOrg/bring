package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

public class InterfaceBeanNameAnnotationResolver implements AnnotationResolver {
    @Override
    public boolean isSupported(Class<?> clazz) {
        return clazz.isInterface();
    }

    @Override
    public String resolve(Class<?> clazz) {
        return clazz.getSimpleName();
    }
}
