package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Qualifier;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

public class ComponentBeanNameAnnotationResolver implements AnnotationResolver {
    
    @Override
    public boolean isSupported(Class<?> clazz) {
        return  clazz.getAnnotation(Component.class) != null;
    }

    @Override
    public String resolve(Class<?> clazz) {
        String value = clazz.getAnnotation(Component.class).value();
        String qualifier = getQualifier(clazz);
        // qualifier -> Component.value -> className
        return qualifier != null ? qualifier : value.isEmpty() ? clazz.getSimpleName() : value;
    }

    private String getQualifier (Class<?> clazz) {
        return clazz.isAnnotationPresent(Qualifier.class) ? clazz.getAnnotation(Qualifier.class).value() : null;
    }
    
}
