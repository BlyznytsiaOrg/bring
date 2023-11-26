package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractValueTypeInjector {

    private final AnnotationBringBeanRegistry beanRegistry;
    private final ClassPathScannerFactory classPathScannerFactory;

    protected AbstractValueTypeInjector(AnnotationBringBeanRegistry beanRegistry,
                                        ClassPathScannerFactory classPathScannerFactory) {
        this.beanRegistry = beanRegistry;
        this.classPathScannerFactory = classPathScannerFactory;
    }


    public List<Object> injectListDependency(List<Class<?>> value) {
        List<Object> dependencyObjects = new ArrayList<>();
        for (var impl : value) {
            String implBeanName = classPathScannerFactory.resolveBeanName(impl);
            Object dependecyObject = beanRegistry.getOrCreateBean(implBeanName, impl, null);
            dependencyObjects.add(dependecyObject);
        }

        return dependencyObjects;
    }
}
