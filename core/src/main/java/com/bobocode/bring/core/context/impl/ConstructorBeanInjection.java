package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Qualifier;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.NoConstructorWithAutowiredAnnotationBeanException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import com.bobocode.bring.core.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Supplier;

@AllArgsConstructor
@Slf4j
public class ConstructorBeanInjection {

    private final AnnotationBringBeanRegistry beanRegistry;

    private final ClassPathScannerFactory classPathScannerFactory;

    public void create(Class<?> clazz, String beanName, BeanDefinition beanDefinition) {
        var constructor = findAutowiredConstructor(clazz);
        createBeanUsingConstructor(constructor, beanName, beanDefinition);
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseThrow(() -> new NoConstructorWithAutowiredAnnotationBeanException(
                        clazz, Arrays.toString(constructors)));
    }

    private void createBeanUsingConstructor(Constructor<?> constructor, String beanName,
                                            BeanDefinition beanDefinition) {
        var createdBeanAnnotations = classPathScannerFactory.getCreatedBeanAnnotations();
        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = new Object[parameters.length];

        List<String> parameterNames = ReflectionUtils.getParameterNames(constructor);

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            int index = i;
            Optional<Object> injectViaProperties = beanRegistry.getTypeResolverFactory()
                    .getParameterValueTypeInjectors().stream()
                    .filter(valueType -> valueType.hasAnnotatedWithValue(parameter))
                    .map(valueType -> valueType.setValueToSetter(parameter, createdBeanAnnotations))
                    .map(obj -> dependencies[index] = obj)
                    .findFirst();

            if (injectViaProperties.isEmpty()) {
                String dependencyBeanName = findBeanNameForArgumentInConstructor(parameter, parameterNames);
                Object dependencyObject = beanRegistry.getOrCreateBean(dependencyBeanName, parameter.getType(), null);
                dependencies[i] = dependencyObject;
            }
        }

        Supplier<Object> supplier = ReflectionUtils.createNewInstance(constructor, dependencies,
                beanDefinition.getBeanClass(), beanDefinition.isProxy());
        Object bean = supplier.get();

        if (beanDefinition.isPrototype()) {
            beanRegistry.addPrototypeBean(beanName, supplier);
        } else {
            beanRegistry.addSingletonBean(beanName, bean);
        }
    }

    private String findBeanNameForArgumentInConstructor(Parameter parameter, List<String> constructorParamNames) {
        Class<?> clazz = parameter.getType();

        List<String> beanNames = clazz.isInterface()
                ? beanRegistry.getTypeToBeanNames().entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .toList()
                : Optional.ofNullable(beanRegistry.getTypeToBeanNames().get(clazz)).orElse(Collections.emptyList());

        if (beanNames.isEmpty()) {
            throw new NoSuchBeanException(clazz);
        } else if (beanNames.size() == 1) {
            return beanNames.get(0);
        } else {
            String paramName = constructorParamNames.get(ReflectionUtils.extractParameterPosition(parameter));

            return findPrimaryBeanNameOrByQualifierOrbBParameter(beanNames, paramName, parameter);
        }
    }

    private String findPrimaryBeanNameOrByQualifierOrbBParameter(List<String> beanNames, String paramName,
                                                                 Parameter  parameter) {
        Class<?>  parameterType = parameter.getType();
        String qualifier = getQualifier(parameter);
        var primaryNames = beanNames.stream()
                .filter(beanName -> beanRegistry.getBeanDefinitionByName(beanName).isPrimary())
                .toList();
        if(primaryNames.size() == 1) {
            return primaryNames.get(0);
        } else if (primaryNames.size() > 1) {
            throw new NoUniqueBeanException(parameterType);
        } else if (qualifier != null) {
            return beanNames.stream().filter(name -> name.equals(qualifier))
                    .findFirst().orElseThrow(() -> new NoSuchBeanException(parameterType));
        }
        else {
            return beanNames.stream()
                    .filter(name -> name.equalsIgnoreCase(paramName))
                    .findFirst()
                    .orElseThrow(() -> new NoUniqueBeanException(parameterType, beanNames));
        }
    }

    private String getQualifier(Parameter  parameter) {
        return parameter.isAnnotationPresent(Qualifier.class) ? parameter.getAnnotation(Qualifier.class).value() : null;
    }
}
