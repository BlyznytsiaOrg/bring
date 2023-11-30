package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.context.BeanDefinitionRegistry;
import com.bobocode.bring.core.context.BeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.CyclicBeanException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.*;

@Slf4j
public class AnnotationBringBeanRegistry extends DefaultBringBeanFactory implements BeanRegistry, BeanDefinitionRegistry {

    @Getter
    protected ClassPathScannerFactory classPathScannerFactory;
    private final BeanCreator beanCreator;
    private final Set<String> currentlyCreatingBeans = new HashSet<>();
    @Getter
    private final Reflections reflections;

    public AnnotationBringBeanRegistry(Reflections reflections) {
        this.reflections = reflections;
        this.classPathScannerFactory = new ClassPathScannerFactory(reflections);
        this.beanCreator = new BeanCreator(this, classPathScannerFactory);
    }

    @Override
    public void registerBean(String beanName, BeanDefinition beanDefinition) {
        log.info("Registering Bean with name \"{}\" into Bring context...", beanName);

        Class<?> clazz = beanDefinition.getBeanClass();

        if (currentlyCreatingBeans.contains(beanName)) {
            log.error("Cyclic dependency detected!");
            throw new CyclicBeanException(currentlyCreatingBeans);
        }

        if (isBeanCreated(beanName)) {
            log.info("Bean with name \"{}\" already created, no need to register it again.", beanName);
            return;
        }

        currentlyCreatingBeans.add(beanName);

        if (beanDefinition.isConfigurationBean()) {
            beanCreator.registerConfigurationBean(beanName, beanDefinition);
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

    public Object getOrCreateBean(String beanName) {
        Object existingBean = getBeanByName(beanName);

        if (Objects.nonNull(existingBean)) {
            return existingBean;
        }

        BeanDefinition beanDefinition = getBeanDefinitionMap().get(beanName);

        if (beanDefinition != null) {
            registerBean(beanName, beanDefinition);
        }

        return getBeanByName(beanName);
    }

}
