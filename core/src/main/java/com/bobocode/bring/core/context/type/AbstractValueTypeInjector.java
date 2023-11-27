package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;

import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import java.util.*;
import java.util.function.BiFunction;

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

    private final static BiFunction<List<BeanDefinition>, String, List<BeanDefinition>> PRIMARY_FILTER_FUNCTION =
        (beanDefinitions, qualifier) -> beanDefinitions.stream()
            .filter(BeanDefinition::isPrimary
            ).toList();

    private final static BiFunction<List<BeanDefinition>, String, List<BeanDefinition>>  QUALIFIER_FILTER_FUNCTION =
        (beanDefinitions, qualifier) -> beanDefinitions.stream()
            .filter(bd -> bd.getFactoryBeanName().equals(qualifier))
            .toList();

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
            Object dependecyObject = beanRegistry.getOrCreateBean(implBeanName);
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
            Object dependecyObject = beanRegistry.getOrCreateBean(implBeanName);
            dependencyObjects.add(dependecyObject);
        }

        return dependencyObjects;
    }

    public Object findImplementationByPrimaryOrQualifier(List<Class<?>> implementationTypes, Class<?> fieldType, String qualifier) {
        List<BeanDefinition> beanDefinitions = implementationTypes.stream()
            .map(impl -> beanRegistry.getBeanDefinitionMap().get(getBeanName(impl)))
            .toList();
        var dependencyObject = findProperBean(beanDefinitions, fieldType, qualifier, PRIMARY_FILTER_FUNCTION);

        if(dependencyObject.isPresent()) {
            return dependencyObject.get();
        } else {
            return findProperBean(beanDefinitions, fieldType, qualifier, QUALIFIER_FILTER_FUNCTION).orElseThrow(() -> new NoSuchBeanException(fieldType));
        }

    }

    private <T> String getBeanName(Class<? extends T> type) {
        List<String> beanNames = Optional.ofNullable(beanRegistry.getTypeToBeanNames().get(type))
            .orElseThrow(() -> new NoSuchBeanException(type));
        if (beanNames.size() != 1) {
            throw new NoUniqueBeanException(type);
        }
        return beanNames.get(0);
    }

    private Optional<?> findProperBean(List<BeanDefinition> beanDefinitions,
        Class<?> interfaceType, String qualifier, BiFunction<List<BeanDefinition>, String, List<BeanDefinition>>filter) {
        if (beanDefinitions.size() == 1) {
            var beanDefinition = beanDefinitions.get(0);
            return Optional.ofNullable(beanRegistry.getOrCreateBean(beanDefinition.getFactoryBeanName()));
        } else {
            var filteredBeanDefinitions = filter.apply(beanDefinitions, qualifier);

            if(filteredBeanDefinitions.size() > 1) {
                throw new NoUniqueBeanException(interfaceType);
            } else if (filteredBeanDefinitions.size() == 1) {
                var beanDefinition = filteredBeanDefinitions.get(0);
                return Optional.ofNullable(beanRegistry.getOrCreateBean(filteredBeanDefinitions.get(0).getFactoryBeanName()));
            }
        }
        return Optional.empty();
    }

}
