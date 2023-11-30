package com.bobocode.bring.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Represents the definition of a bean within an application context.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Getter
@Builder
public class BeanDefinition {

    private Class<?> beanClass;

    private BeanTypeEnum beanType;

    private BeanScope scope;

    private ProxyMode proxyMode;

    private Method method;

    private String factoryBeanName;

    private boolean isPrimary;

    /**
     * Checks if the bean is of type Configuration.
     *
     * @return True if the bean is of type Configuration, otherwise false
     */
    public boolean isConfiguration() {
        return this.beanType == BeanTypeEnum.CONFIGURATION;
    }

    /**
     * Checks if the bean is a configuration bean.
     *
     * @return True if the bean is a configuration bean, otherwise false
     */
    public boolean isConfigurationBean() {
        return Objects.nonNull(method);
    }

    /**
     * Checks if the bean's scope is set to Prototype.
     *
     * @return True if the bean's scope is Prototype, otherwise false
     */
    public boolean isPrototype() {
        return scope == BeanScope.PROTOTYPE;
    }

    /**
     * Checks if the bean is configured for proxying.
     *
     * @return True if the bean is configured for proxying, otherwise false
     */
    public boolean isProxy() {
        return proxyMode == ProxyMode.ON;
    }

}
