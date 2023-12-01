package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;

import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private static final BiFunction<List<BeanDefinition>, String, List<BeanDefinition>> PRIMARY_FILTER_FUNCTION =
        (beanDefinitions, qualifier) -> beanDefinitions.stream()
            .filter(BeanDefinition::isPrimary
            ).toList();

    private final AnnotationBringBeanRegistry beanRegistry;
    private final ClassPathScannerFactory classPathScannerFactory;

    private final BiFunction<List<BeanDefinition>, String, List<BeanDefinition>>  QUALIFIER_FILTER_FUNCTION;

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

        QUALIFIER_FILTER_FUNCTION = (beanDefinitions, qualifier) -> beanDefinitions.stream()
            .filter(bd -> classPathScannerFactory.resolveBeanName(bd.getBeanClass()).equals(qualifier))
            .toList();
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

    public Object findImplementationByPrimaryOrQualifier(List<Class<?>> implementationTypes, Class<?> fieldType, String qualifier, String fieldName) {
        List<BeanDefinition> beanDefinitions = implementationTypes.stream()
            .map(impl -> beanRegistry.getBeanDefinitionMap().get(getBeanName(impl)))
            .toList();
        var dependencyObject = findProperBean(beanDefinitions, fieldType, qualifier, PRIMARY_FILTER_FUNCTION);

        if(dependencyObject.isPresent()) {
            return dependencyObject.get();
        } else {
            dependencyObject = findProperBean(beanDefinitions, fieldType, qualifier, QUALIFIER_FILTER_FUNCTION);
            if(dependencyObject.isPresent()) {
                return dependencyObject.get();
            } else {
                return findProperBeanByName(fieldType, fieldName);
            }
        }
    }

    private Object findProperBeanByName(Class<?> type, String paramName) {
        String beanName = checkByBeanName(getBeanNames(type),  paramName, type);
        return beanRegistry.getOrCreateBean(beanName);
    }

    private String checkByBeanName(List<String> beanNames, String paramName, Class<?> type) {
        if (beanNames.isEmpty()) {
            if (type.isInterface()) {
                throw  new NoSuchBeanException(String.format("No such bean that implements this %s ", type));
            }
            throw new NoSuchBeanException(type);
        }

        return beanNames.stream()
            .filter(name -> name.equalsIgnoreCase(paramName))
            .findFirst()
            .orElseThrow(() -> new NoUniqueBeanException(type, beanNames));
    }

    private List<String> getBeanNames (Class<?> clazz) {
        return clazz.isInterface()
            ? beanRegistry.getTypeToBeanNames().entrySet().stream()
            .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
            .map(Map.Entry::getValue)
            .flatMap(Collection::stream)
            .toList()
            : Optional.ofNullable(beanRegistry.getTypeToBeanNames().get(clazz)).orElse(
                Collections.emptyList());
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
            return Optional.ofNullable(beanRegistry.getOrCreateBean(classPathScannerFactory.resolveBeanName(beanDefinition.getBeanClass())));
        } else {
            var filteredBeanDefinitions = filter.apply(beanDefinitions, qualifier);

            if(filteredBeanDefinitions.size() > 1) {
                throw new NoUniqueBeanException(interfaceType);
            } else if (filteredBeanDefinitions.size() == 1) {
                var beanName = classPathScannerFactory.resolveBeanName(filteredBeanDefinitions.get(0).getBeanClass());
                return Optional.ofNullable(beanRegistry.getOrCreateBean(beanName));
            }
        }
        return Optional.empty();
    }

}
