package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.BeanDefinitionRegistry;
import com.bobocode.bring.core.context.BeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.CyclicBeanException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AnnotationBringBeanRegistry extends DefaultBringBeanFactory implements BeanRegistry, BeanDefinitionRegistry {
    @Getter
    private final ClassPathScannerFactory classPathScannerFactory;
    protected final Set<Class<?>> beansToCreate;
    private final Set<String> currentlyCreatingBeans = new HashSet<>();

    @Getter
    private final Reflections reflections;
    private final ConfigurationBeanRegistrar configurationBeanRegistrar;
    private final BeanCreator beanCreator;

    public AnnotationBringBeanRegistry(Reflections reflections) {
        this.reflections = reflections;
        this.classPathScannerFactory = new ClassPathScannerFactory(reflections);
        this.beansToCreate = classPathScannerFactory.getBeansToCreate();
        this.configurationBeanRegistrar = new ConfigurationBeanRegistrar(this);
        this.beanCreator = new BeanCreator(this);
    }

    @Override
    public void registerBean(String beanName, BeanDefinition beanDefinition) {
        log.info("Registering Bean with name [{}] into Bring context...", beanName);

        Class<?> clazz = beanDefinition.getBeanClass();

        if (currentlyCreatingBeans.contains(beanName)) {
            throw new CyclicBeanException(currentlyCreatingBeans);
        }

        if (getSingletonObjects().containsKey(beanName) || getPrototypeSuppliers().containsKey(beanName)) {
            log.info("Bean with name [{}] already created, no need to register it again.", beanName);
            return;
        }

        currentlyCreatingBeans.add(beanName);

        if (beanDefinition.isConfigurationBean()) {
            configurationBeanRegistrar.registerConfigurationBean(beanName, beanDefinition);
        } else {
            beanCreator.create(clazz, beanName, beanDefinition);
        }

        currentlyCreatingBeans.clear();
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        String beanName = classPathScannerFactory.resolveBeanName(beanDefinition.getBeanClass());
        addBeanDefinition(beanName, beanDefinition);
    }
}
