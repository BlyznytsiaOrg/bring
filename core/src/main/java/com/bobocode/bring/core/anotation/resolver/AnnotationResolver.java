package com.bobocode.bring.core.anotation.resolver;

public interface AnnotationResolver {

    boolean isSupported(Class<?> clazz);
    
    String resolve(Class<?> clazz);
    
}
