package com.bobocode.bring.core.utils;

import com.bobocode.bring.core.anotation.Scope;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class BeanScopeUtils {
    
    public final String SINGLETON = "singleton";
    
    public final String PROTOTYPE = "prototype";
    
    public String findBeanScope(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(Scope.class))
                .map(Scope::value)
                .filter(value -> getAllScopes().contains(value))
                .orElse(BeanScopeUtils.SINGLETON);
    }

    public String findBeanScope(Method method) {
        return Optional.ofNullable(method.getAnnotation(Scope.class))
                .map(Scope::value)
                .filter(value -> getAllScopes().contains(value))
                .orElse(BeanScopeUtils.SINGLETON);
    }
    
    private List<String> getAllScopes() {
        return List.of(SINGLETON, PROTOTYPE);
    }
    
}
