package com.bobocode.bring.core.utils;

import com.bobocode.bring.core.annotation.Scope;
import com.bobocode.bring.core.domain.BeanScope;
import com.bobocode.bring.core.domain.ProxyMode;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * Utility class providing methods for resolving bean scopes and proxy modes based on annotations.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@UtilityClass
public class BeanScopeUtils {

    /**
     * Finds the bean scope specified by the {@code Scope} annotation on the given class.
     *
     * @param clazz The class to inspect for the scope annotation
     * @return The resolved bean scope or defaulting to {@code BeanScope.SINGLETON}
     */
    public BeanScope findBeanScope(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(Scope.class))
                .map(Scope::name)
                .filter(value -> getAllScopes().contains(value))
                .orElse(BeanScope.SINGLETON);
    }

    /**
     * Finds the bean scope specified by the {@code Scope} annotation on the given method.
     *
     * @param method The method to inspect for the scope annotation
     * @return The resolved bean scope or defaulting to {@code BeanScope.SINGLETON}
     */
    public BeanScope findBeanScope(Method method) {
        return Optional.ofNullable(method.getAnnotation(Scope.class))
                .map(Scope::name)
                .filter(value -> getAllScopes().contains(value))
                .orElse(BeanScope.SINGLETON);
    }

    /**
     * Finds the proxy mode specified by the {@code Scope} annotation on the given class.
     *
     * @param clazz The class to inspect for the proxy mode annotation
     * @return The resolved proxy mode or {@code null} if not found
     */
    public ProxyMode findProxyMode(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(Scope.class))
          .map(Scope::proxyMode)
          .filter(value -> getAllProxyModes().contains(value))
          .orElse(null);
    }

    /**
     * Finds the proxy mode specified by the {@code Scope} annotation on the given method.
     *
     * @param method The method to inspect for the proxy mode annotation
     * @return The resolved proxy mode or {@code null} if not found
     */
    public ProxyMode findProxyMode(Method method) {
        return Optional.ofNullable(method.getAnnotation(Scope.class))
          .map(Scope::proxyMode)
          .filter(value -> getAllProxyModes().contains(value))
          .orElse(null);
    }

    /**
     * Retrieves all available bean scopes.
     *
     * @return A list containing all available bean scopes
     */
    private List<BeanScope> getAllScopes() {
        return List.of(BeanScope.values());
    }

    /**
     * Retrieves all available proxy modes.
     *
     * @return A list containing all available proxy modes
     */
    private List<ProxyMode> getAllProxyModes() {
        return List.of(ProxyMode.values());
    }
    
}
