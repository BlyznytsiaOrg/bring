package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanCreator {
    private final AnnotationBringBeanRegistry beanRegistry;
    private final ConstructorBeanInjection createBeanUsingConstructor;
    private final FieldBeanInjection fieldBeanInjection;
    private final SetterBeanInjection setterBeanInjection;

    public BeanCreator(AnnotationBringBeanRegistry beanRegistry, ClassPathScannerFactory classPathScannerFactory) {
        this.beanRegistry = beanRegistry;
        this.createBeanUsingConstructor = new ConstructorBeanInjection(beanRegistry, classPathScannerFactory);
        this.fieldBeanInjection = new FieldBeanInjection(beanRegistry, classPathScannerFactory);
        this.setterBeanInjection = new SetterBeanInjection(beanRegistry, classPathScannerFactory);
    }

    public void create(Class<?> clazz, String beanName, BeanDefinition beanDefinition) {
        createBeanUsingConstructor.create(clazz, beanName, beanDefinition);
        injectDependencies(clazz, beanName);
    }

    private void injectDependencies(Class<?> clazz, String beanName) {
        Object bean = beanRegistry.getSingletonObjects().get(beanName);
        fieldBeanInjection.injectViaFields(clazz, bean);
        setterBeanInjection.injectViaSetter(clazz, bean);
    }
}
