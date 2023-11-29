package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;

import java.util.*;

/**
 * The {@code AbstractValueTypeInjector} is an abstract class that serves as a base for value type injectors.
 * It provides methods for injecting dependencies for types like List and Set. This injector collaborates with
 * the AnnotationBringBeanRegistry and ClassPathScannerFactory equivalents to resolve bean names, retrieve or
 * create beans, and inject dependencies based on the provided value types.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public abstract class AbstractValueTypeInjector {

    private final AnnotationBringBeanRegistry beanRegistry;
    private final ClassPathScannerFactory classPathScannerFactory;

    /**
     * Constructs a new instance of {@code AbstractValueTypeInjector}.
     *
     * @param beanRegistry              The bean registry for managing created beans.
     * @param classPathScannerFactory   The factory for creating classpath scanners.
     */
    protected AbstractValueTypeInjector(AnnotationBringBeanRegistry beanRegistry,
                                        ClassPathScannerFactory classPathScannerFactory) {
        this.beanRegistry = beanRegistry;
        this.classPathScannerFactory = classPathScannerFactory;
    }

    /**
     * Injects dependencies for a List of classes into bean objects.
     *
     * @param value The list of classes for which dependencies need to be injected.
     * @return A list of bean objects representing the injected dependencies.
     */
    public List<Object> injectListDependency(List<Class<?>> value) {
        List<Object> dependencyObjects = new ArrayList<>();
        for (var impl : value) {
            String implBeanName = classPathScannerFactory.resolveBeanName(impl);
            Object dependecyObject = beanRegistry.getOrCreateBean(implBeanName, impl, null);
            dependencyObjects.add(dependecyObject);
        }

        return dependencyObjects;
    }

    /**
     * Injects dependencies for a List of classes into bean objects and returns a set of unique dependencies.
     *
     * @param value The list of classes for which dependencies need to be injected.
     * @return A set of unique bean objects representing the injected dependencies.
     */
    public Set<Object> injectSetDependency(List<Class<?>> value) {
        Set<Object> dependencyObjects = new LinkedHashSet<>();
        for (var impl : value) {
            String implBeanName = classPathScannerFactory.resolveBeanName(impl);
            Object dependecyObject = beanRegistry.getOrCreateBean(implBeanName, impl, null);
            dependencyObjects.add(dependecyObject);
        }

        return dependencyObjects;
    }
}
