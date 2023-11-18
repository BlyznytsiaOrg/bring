package com.bobocode.bring.core.anotation.resolver.impl;

import com.bobocode.bring.core.anotation.RequestMapping;
import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;

public class ReqestMappingBeanNameAnnotationResolver implements AnnotationResolver {
    @Override
    public boolean isSupported(Class<?> clazz) {
        return  clazz.getAnnotation(RequestMapping.class) != null;
    }

    @Override
    public String resolve(Class<?> clazz) {
        return clazz.getSimpleName();
    }
    
}
