package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Value;
import com.bobocode.bring.core.context.type.ParameterValueTypeInjector;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.NoConstructorWithAutowiredAnnotationBeanException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import com.bobocode.bring.core.utils.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Supplier;

import static com.bobocode.bring.core.utils.ReflectionUtils.isAutowiredSetterMethod;
import static com.bobocode.bring.core.utils.ReflectionUtils.setField;

@Slf4j
@AllArgsConstructor
public class BeanCreator {

    private final AnnotationBringBeanRegistry beanRegistry;

    public void create(Class<?> clazz, String beanName, BeanDefinition beanDefinition) {
        var constructor = findAutowiredConstructor(clazz);
        createBeanUsingConstructor(constructor, beanName, beanDefinition);
        injectDependencies(clazz, beanName);
    }

    private Constructor<?> findAutowiredConstructor(Class<?> clazz){
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length == 1) {
            return constructors[0];
        }

        return Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst()
                .orElseThrow(() -> new NoConstructorWithAutowiredAnnotationBeanException(
                        clazz, Arrays.toString(constructors))
                );
    }

    private void createBeanUsingConstructor(Constructor<?> constructor, String beanName, BeanDefinition beanDefinition) {
        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = new Object[parameters.length];

        List<String> parameterNames = ReflectionUtils.getParameterNames(constructor);
        var createdBeanAnnotations = beanRegistry.getClassPathScannerFactory().getCreatedBeanAnnotations();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            int index = i;
            Optional<Object> injectViaProperties = beanRegistry.getTypeResolverFactory()
                    .getParameterValueTypeInjectors().stream()
                    .filter(valueType -> valueType.hasAnnotatedWithValue(parameter))
                    .map(valueType -> {
                        Object dependencyValue = valueType.setValueToSetter(parameter, createdBeanAnnotations);
                        if (dependencyValue instanceof List) {
                            return injectListDependency((List<Class<?>>) dependencyValue);
                        }
                        return dependencyValue;
                    })
                    .map(obj -> dependencies[index] = obj)
                    .findFirst();

            if (injectViaProperties.isEmpty()) {
                String dependencyBeanName = findBeanNameForArgumentInConstructor(parameter, parameterNames);
                Object dependencyObject = getOrCreateBean(dependencyBeanName);
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

            return beanNames.stream()
                    .filter(name -> name.equalsIgnoreCase(paramName))
                    .findFirst()
                    .orElseThrow(() -> new NoUniqueBeanException(clazz, beanNames));
        }
    }

    private Object getOrCreateBean(String beanName) {
        Object existingBean = beanRegistry.getBeanByName(beanName);

        if (Objects.nonNull(existingBean)) {
            return existingBean;
        }

        BeanDefinition beanDefinition = beanRegistry.getBeanDefinitionMap().get(beanName);

        if (Objects.nonNull(beanDefinition)) {
            beanRegistry.registerBean(beanName, beanDefinition);
        }

        return beanRegistry.getBeanByName(beanName);
    }

    private void injectDependencies(Class<?> clazz, String beanName) {
        Object bean = beanRegistry.getSingletonObjects().get(beanName);

        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class) || field.isAnnotationPresent(Value.class))
                .forEach(field -> injectDependencyViaField(field, bean));

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(ReflectionUtils::isAutowiredSetterMethod)
                .forEach(method -> injectDependencyViaMethod(method, bean));
    }

    @SneakyThrows
    private void injectDependencyViaMethod(Method method, Object bean) {
        if (isAutowiredSetterMethod(method)) {
            Parameter parameter = method.getParameters()[0];
            Optional<Object> injectViaProperties = beanRegistry.getTypeResolverFactory().getParameterValueTypeInjectors()
                    .stream()
                    .filter(valueType -> valueType.hasAnnotatedWithValue(parameter))
                    .map(valueType -> injectDependencyViaParameter(method, parameter, bean, valueType))
                    .findFirst();

            if (injectViaProperties.isEmpty()) {
                Class<?> dependencyType = method.getParameterTypes()[0];
                String dependencyBeanName = beanRegistry.getClassPathScannerFactory().resolveBeanName(dependencyType);
                Object dependencyObject = getOrCreateBean(dependencyBeanName);
                method.invoke(bean, dependencyObject);
            }
        }
    }

    @SneakyThrows
    private Object injectDependencyViaParameter(Method method, Parameter parameter, Object bean,
                                                ParameterValueTypeInjector valueType) {
        var createdBeanAnnotations = beanRegistry.getClassPathScannerFactory().getCreatedBeanAnnotations();
        Object dependencyValue = valueType.setValueToSetter(parameter, createdBeanAnnotations);
        if (dependencyValue instanceof List) {
            var dependencyObjects = injectListDependency((List<Class<?>>) dependencyValue);
            method.invoke(bean, dependencyObjects);
        } else {
            method.invoke(bean, dependencyValue);
        }
        return dependencyValue;
    }

    @SneakyThrows
    private void injectDependencyViaField(Field field, Object bean) {
        var createdBeanAnnotations = beanRegistry.getClassPathScannerFactory().getCreatedBeanAnnotations();
        Optional<Object> injectViaProperties = beanRegistry.getTypeResolverFactory().getFieldValueTypeInjectors()
                .stream()
                .filter(valueType -> valueType.hasAnnotatedWithValue(field))
                .map(valueType -> {
                    Object dependencyValue = valueType.setValueToField(field, bean, createdBeanAnnotations);
                    if (dependencyValue instanceof List listDependencyValue) {
                        setField(field, bean, injectListDependency(listDependencyValue));
                    } else {
                        setField(field, bean, dependencyValue);
                    }
                    return dependencyValue;
                })
                .findFirst();

        if (injectViaProperties.isEmpty()) {
            String dependencyBeanName = beanRegistry.getClassPathScannerFactory().resolveBeanName(field.getType());
            Object dependencyObject = getOrCreateBean(dependencyBeanName);
            setField(field, bean, dependencyObject);
        }
    }

    private List<Object> injectListDependency(List<Class<?>> implementations) {
        List<Object> dependencyObjects = new ArrayList<>();
        for (var implementation : implementations) {
            String implBeanName = beanRegistry.getClassPathScannerFactory().resolveBeanName(implementation);
            Object dependecyObject = getOrCreateBean(implBeanName);
            dependencyObjects.add(dependecyObject);
        }

        return dependencyObjects;
    }
}
