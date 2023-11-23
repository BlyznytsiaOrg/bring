package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.anotation.*;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;
import com.bobocode.bring.core.anotation.resolver.impl.*;
import com.bobocode.bring.core.context.BeanDefinitionRegistry;
import com.bobocode.bring.core.context.BeanRegistry;
import com.bobocode.bring.core.context.type.ParameterValueTypeInjector;
import com.bobocode.bring.core.domain.BeanDefinition;
import com.bobocode.bring.core.exception.CyclicBeanException;
import com.bobocode.bring.core.exception.NoConstructorWithAutowiredAnnotationBeanException;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import com.bobocode.bring.core.utils.ReflectionUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Supplier;

import static com.bobocode.bring.core.utils.ReflectionUtils.setField;

@RequiredArgsConstructor
@Slf4j
public class AnnotationBringBeanRegistry extends DefaultBringBeanFactory implements BeanRegistry, BeanDefinitionRegistry {

    private static final String SET_METHOD_START_PREFIX = "set";

    private final List<AnnotationResolver> annotationResolvers = List.of(
            new ComponentBeanNameAnnotationResolver(),
            new ServiceBeanNameAnnotationResolver(),
            new InterfaceBeanNameAnnotationResolver(),
            new ConfigurationBeanNameAnnotationResolver(),
            new ControllerBeanNameAnnotationResolver(),
            new RestControllerBeanNameAnnotationResolver()
    );

    private final List<Class<? extends Annotation>> createdBeanAnnotations = List.of(
            Component.class, Service.class, Configuration.class, RestController.class, Controller.class, Bean.class
    );

    private final Set<String> currentlyCreatingBeans = new HashSet<>();

    @Getter
    private final Reflections reflections;

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
            registerConfigurationBean(beanName, beanDefinition);
        } else {
            Constructor<?> constructor = findAutowiredConstructor(clazz);
            createBeanUsingConstructor(constructor, beanName, beanDefinition);

            injectDependencies(clazz, beanName);
        }

        currentlyCreatingBeans.clear();
    }

    @SneakyThrows
    private Object registerConfigurationBean(String beanName, BeanDefinition beanDefinition) {
        Object configObj = Optional.ofNullable(getSingletonObjects().get(beanDefinition.getFactoryBeanName()))
                .orElseThrow(() -> {
                    log.info("Unable to register Bean from Configuration class [{}]: " +
                            "Configuration class not annotated or is not of Singleton scope.", beanName);
                    return new NoSuchBeanException(beanDefinition.getBeanClass());
                });
        
        List<String> methodParamNames = ReflectionUtils.getParameterNames(beanDefinition.getMethod());

        List<Object> methodObjs = new ArrayList<>();
        methodParamNames.forEach(
                paramName -> {
                    Object object = getBeanByName(paramName);

                    if (Objects.nonNull(object)) {
                        methodObjs.add(object);
                    } else {
                        BeanDefinition bd = Optional.ofNullable(getBeanDefinitionMap().get(paramName))
                                .orElseThrow(() -> new NoSuchBeanException(beanDefinition.getBeanClass()));
                        Object newObj = registerConfigurationBean(bd.getFactoryMethodName(), bd);
                        methodObjs.add(newObj);
                    }
                });

        Supplier<Object> supplier = ReflectionUtils.invokeBeanMethod(beanDefinition.getMethod(), 
                configObj, methodObjs.toArray());
        Object bean = supplier.get();
        
        if (beanDefinition.isPrototype()) {
            addPrototypeBean(beanName, supplier);
        } else {
            addSingletonBean(beanName, bean);
        }

        return bean;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        String beanName = resolveBeanName(beanDefinition.getBeanClass());
        addBeanDefinition(beanName, beanDefinition);
    }

    private <T> void registerImplementations(String beanName, Class<T> interfaceType) {
        if (interfaceType.isAssignableFrom(List.class)) {
            registerListImplementations(interfaceType);
            return;
        }

        Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaceType);

        for (Class<? extends T> implementation : implementations) {
            boolean hasRequiredAnnotation = Arrays.stream(implementation.getAnnotations())
                    .map(Annotation::annotationType)
                    .anyMatch(createdBeanAnnotations::contains);

            if (hasRequiredAnnotation) {
                List<String> beanNames = Optional.ofNullable(getTypeToBeanNames().get(implementation))
                        .orElseThrow(() -> new NoSuchBeanException(implementation));

                if (beanNames.size() != 1) {
                    throw new NoUniqueBeanException(implementation);
                }

                String implementationBeanName = beanNames.get(0);
                BeanDefinition beanDefinition = getBeanDefinitionMap().get(implementationBeanName);
                registerBean(beanName, beanDefinition);
            }
        }

    }

    private <T> void registerListImplementations(Class<T> interfaceType) {
        throw new UnsupportedOperationException("Not implemented yet");
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

        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = new Object[parameters.length];

        List<String> parameterNames = ReflectionUtils.getParameterNames(constructor);

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            int index = i;
            Optional<Object> injectViaProperties = getTypeResolverFactory().getParameterValueTypeInjectors().stream()
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
                Object dependencyObject = getOrCreateBean(dependencyBeanName, parameter.getType());
                dependencies[i] = dependencyObject;
            }
        }

        Supplier<Object> supplier = ReflectionUtils.createNewInstance(constructor, dependencies, 
                beanDefinition.getBeanClass(), beanDefinition.isProxy());
        Object bean = supplier.get();

        for (var interfaceClass : bean.getClass().getInterfaces()) {
            addInterfaceNameToImplementations(interfaceClass.getSimpleName(), bean);
        }

        if (beanDefinition.isPrototype()) {
            addPrototypeBean(beanName, supplier);
        } else {
            addSingletonBean(beanName, bean);
        }
    }

    private String findBeanNameForArgumentInConstructor(Parameter parameter, List<String> constructorParamNames) {
        Class<?> clazz = parameter.getType();

        List<String> beanNames = clazz.isInterface()
                ? getTypeToBeanNames().entrySet().stream()
                    .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .flatMap(Collection::stream)
                    .toList()
                : Optional.ofNullable(getTypeToBeanNames().get(clazz)).orElse(Collections.emptyList());

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

    private Object getOrCreateBean(String beanName, Class<?> beanType) {
        Object existingBean = getBeanByName(beanName);

        if (Objects.nonNull(existingBean)) {
            return existingBean;
        }

        BeanDefinition beanDefinition = getBeanDefinitionMap().get(beanName);

        if (Objects.isNull(beanDefinition)) {
            //TODO this is like workaround need to think how to populate bean definition for interface & dependencies
            if (beanType.isInterface()) {
                registerImplementations(beanName, beanType);
            }
        } else {
            registerBean(beanName, beanDefinition);
        }

        return getBeanByName(beanName);
    }

    private void injectDependencies(Class<?> clazz, String beanName) {
        Object bean = getSingletonObjects().get(beanName);

        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class) || field.isAnnotationPresent(Value.class))
                .forEach(field -> injectDependencyViaField(field, bean));

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::isAutowiredSetterMethod)
                .forEach(method -> injectDependencyViaMethod(method, bean));
    }

    @SneakyThrows
    private void injectDependencyViaMethod(Method method, Object bean) {
        if (isAutowiredSetterMethod(method)) {
            Parameter parameter = method.getParameters()[0];
            Optional<Object> injectViaProperties = getTypeResolverFactory().getParameterValueTypeInjectors().stream()
                    .filter(valueType -> valueType.hasAnnotatedWithValue(parameter))
                    .map(valueType -> injectDependencyViaParameter(method, parameter, bean, valueType))
                    .findFirst();

            if (injectViaProperties.isEmpty()) {
                Class<?> dependencyType = method.getParameterTypes()[0];
                String dependencyBeanName = resolveBeanName(dependencyType);
                Object dependencyObject = getOrCreateBean(dependencyBeanName, dependencyType);
                method.invoke(bean, dependencyObject);
            }
        }
    }

    @SneakyThrows
    private Object injectDependencyViaParameter(Method method, Parameter parameter, Object bean,
                                                ParameterValueTypeInjector valueType) {
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
        Optional<Object> injectViaProperties = getTypeResolverFactory().getFieldValueTypeInjectors().stream()
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
            String dependencyBeanName = resolveBeanName(field.getType());
            Object dependencyObject = getOrCreateBean(dependencyBeanName, field.getType());
            setField(field, bean, dependencyObject);
        }
    }

    private List<Object> injectListDependency(List<Class<?>> value) {
        List<Object> dependencyObjects = new ArrayList<>();
        for (var impl : value) {
            String implBeanName = resolveBeanName(impl);
            Object dependecyObject = getOrCreateBean(implBeanName, impl);
            dependencyObjects.add(dependecyObject);
        }

        return dependencyObjects;
    }

    private boolean isAutowiredSetterMethod(Method method) {
        return method.isAnnotationPresent(Autowired.class) && method.getName().startsWith(SET_METHOD_START_PREFIX);
    }

    private String resolveBeanName(Class<?> clazz) {
        return annotationResolvers.stream()
                .filter(resolver -> resolver.isSupported(clazz))
                .findFirst()
                .map(annotationResolver -> annotationResolver.resolve(clazz))
                .orElseThrow(
                        () -> new IllegalStateException("No suitable resolver found for " + clazz.getName()));
    }

}
