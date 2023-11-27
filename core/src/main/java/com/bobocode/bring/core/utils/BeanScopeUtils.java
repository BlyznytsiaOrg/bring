package com.bobocode.bring.core.utils;

import com.bobocode.bring.core.annotation.Scope;
import com.bobocode.bring.core.domain.BeanScope;
import com.bobocode.bring.core.domain.ProxyMode;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class BeanScopeUtils {
    
    public BeanScope findBeanScope(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(Scope.class))
                .map(Scope::name)
                .filter(value -> getAllScopes().contains(value))
                .orElse(BeanScope.SINGLETON);
    }

    public BeanScope findBeanScope(Method method) {
        return Optional.ofNullable(method.getAnnotation(Scope.class))
                .map(Scope::name)
                .filter(value -> getAllScopes().contains(value))
                .orElse(BeanScope.SINGLETON);
    }

    public ProxyMode findProxyMode(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(Scope.class))
          .map(Scope::proxyMode)
          .filter(value -> getAllProxyModes().contains(value))
          .orElse(null);
    }

    public ProxyMode findProxyMode(Method method) {
        return Optional.ofNullable(method.getAnnotation(Scope.class))
          .map(Scope::proxyMode)
          .filter(value -> getAllProxyModes().contains(value))
          .orElse(null);
    }
    
    private List<BeanScope> getAllScopes() {
        return List.of(BeanScope.values());
    }

    private List<ProxyMode> getAllProxyModes() {
        return List.of(ProxyMode.values());
    }
    
}
