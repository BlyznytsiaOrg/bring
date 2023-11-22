package com.bobocode.bring.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;

@Getter
@Builder
public class BeanDefinition {
    
    private Class<?> beanClass;
    
    private BeanTypeEnum beanType;
    
    private boolean isSingleton;
    
    private Method method;
    
    private String factoryMethodName;
    
    private String factoryBeanName;

    private boolean isPrimary;
    
    public boolean isConfiguration() {
        return this.beanType == BeanTypeEnum.CONFIGURATION;
    }
    
}
