package com.bobocode.bring.core.domain;

import com.bobocode.bring.core.utils.BeanScopeUtils;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Objects;

@Getter
@Builder
public class BeanDefinition {
    
    private Class<?> beanClass;
    
    private BeanTypeEnum beanType;
    
    private BeanScope scope;
    
    private ProxyMode proxyMode;
    
    private Method method;
    
    private String factoryMethodName;
    
    private String factoryBeanName;

    private boolean isPrimary;
    
    public boolean isConfiguration() {
        return this.beanType == BeanTypeEnum.CONFIGURATION;
    }

    public boolean isProfile() {
        return this.beanType == BeanTypeEnum.PROFILE;
    }

    public boolean isConfigurationBean() {
        return Objects.nonNull(method);
    }
    
    public boolean isPrototype() {
        return scope == BeanScope.PROTOTYPE;
    }
    
    public boolean isProxy() {
        return proxyMode == ProxyMode.ON;
    }
    
}
