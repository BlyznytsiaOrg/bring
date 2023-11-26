package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import lombok.extern.slf4j.Slf4j;

/**
 * Responsible for creating beans and injecting dependencies into these beans based on provided definitions.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Slf4j
public class BeanCreator {
    private final AnnotationBringBeanRegistry beanRegistry;
    private final ConstructorBeanInjection createBeanUsingConstructor;
    private final FieldBeanInjection fieldBeanInjection;
    private final SetterBeanInjection setterBeanInjection;

    /**
     * Constructs a BeanCreator.
     *
     * @param beanRegistry           The registry storing beans and their definitions.
     * @param classPathScannerFactory The factory for creating class path scanners.
     */
    public BeanCreator(AnnotationBringBeanRegistry beanRegistry, ClassPathScannerFactory classPathScannerFactory) {
        this.beanRegistry = beanRegistry;
        this.createBeanUsingConstructor = new ConstructorBeanInjection(beanRegistry, classPathScannerFactory);
        this.fieldBeanInjection = new FieldBeanInjection(beanRegistry, classPathScannerFactory);
        this.setterBeanInjection = new SetterBeanInjection(beanRegistry, classPathScannerFactory);
    }

    /**
     * Creates a bean of the specified class and injects dependencies into it.
     *
     * @param clazz          The class of the bean to be created.
     * @param beanName       The name of the bean being created.
     * @param beanDefinition The definition of the bean being created.
     */
    public void create(Class<?> clazz, String beanName, BeanDefinition beanDefinition) {
        createBeanUsingConstructor.create(clazz, beanName, beanDefinition);
        injectDependencies(clazz, beanName);
    }

    /**
     * Injects dependencies into the bean based on its class and name.
     *
     * @param clazz    The class of the bean.
     * @param beanName The name of the bean.
     */
    private void injectDependencies(Class<?> clazz, String beanName) {
        Object bean = beanRegistry.getSingletonObjects().get(beanName);
        fieldBeanInjection.injectViaFields(clazz, bean);
        setterBeanInjection.injectViaSetter(clazz, bean);
    }
}
