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

@Slf4j
public class AnnotationBringBeanRegistry extends DefaultBringBeanFactory implements BeanRegistry, BeanDefinitionRegistry {

    private static final String SET_METHOD_START_PREFIX = "set";

    private final Reflections reflections;

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

    public AnnotationBringBeanRegistry(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public void registerBean(String beanName, BeanDefinition beanDefinition) {
        Class<?> clazz = beanDefinition.getBeanClass();

        if (currentlyCreatingBeans.contains(beanName)) {
            throw new CyclicBeanException(currentlyCreatingBeans);
        }

        if (getSingletonObjects().containsKey(beanName)) {
            // Bean with this name already created, no need to create it again.
            return;
        }

        currentlyCreatingBeans.add(beanName);

        if (beanDefinition.isConfigurationBean()) {
            registerConfigurationBean(beanName, beanDefinition);
        } else {
            findAutowiredConstructor(clazz)
                    .map(constructorToUse -> createBeanUsingConstructor(constructorToUse, beanName, beanDefinition))
                    .orElseThrow(() -> new NoConstructorWithAutowiredAnnotationBeanException(clazz));

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
        methodParamNames.forEach(paramName -> {
            Object object = beanDefinition.isPrototype() 
              ? Optional.ofNullable(getPrototypeSuppliers().get(paramName)).map(Supplier::get).orElse(null) 
              : getSingletonObjects().get(paramName);
            
            if (Objects.nonNull(object)) {
                methodObjs.add(object);
            } else {
                BeanDefinition bd = Optional.ofNullable(getBeanDefinitions().get(paramName))
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
        addBeanDefinition(resolveBeanName(beanDefinition.getBeanClass()), beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return getBeanDefinitionByName(beanName);
    }

    @Override
    public List<String> getBeanDefinitionNames() {
        return this.getAllBeanDefinitionNames();
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
                BeanDefinition beanDefinition = getBeanDefinitions().get(implementationBeanName);
                registerBean(beanName, beanDefinition);
            }
        }

    }

    private <T> void registerListImplementations(Class<T> interfaceType) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private Optional<Constructor<?>> findAutowiredConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length == 1) {
            return Optional.of(constructors[0]);
        }

        Optional<Constructor<?>> autowiredConstructor = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .findFirst();

        if (constructors.length > 1 && autowiredConstructor.isEmpty()) {
            throw new NoConstructorWithAutowiredAnnotationBeanException(clazz, Arrays.toString(constructors));
        }

        return autowiredConstructor;
    }

    @SneakyThrows
    private Object createBeanUsingConstructor(Constructor<?> constructor, String beanName, 
                                              BeanDefinition beanDefinition) {
        Parameter[] parameters = constructor.getParameters();
        Object[] dependencies = new Object[parameters.length];

        List<String> names = ReflectionUtils.getParameterNames(constructor);

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
                String dependencyBeanName = findBeanNameForParamInConstructor(parameter, names);
                Object dependencyObject = getOrCreateBean(dependencyBeanName, parameter.getType());
                dependencies[i] = dependencyObject;
            }
        }

        Supplier<Object> supplier = ReflectionUtils.createNewInstance(constructor, dependencies);
        Object bean = supplier.get();

        for (var interfaceClass : bean.getClass().getInterfaces()) {
            addInterfaceNameToImplementations(interfaceClass.getSimpleName(), bean);
        }

        if (beanDefinition.isPrototype()) {
            addPrototypeBean(beanName, supplier);
        } else {
            addSingletonBean(beanName, bean);
        }
        
        return bean;
    }

    private String findBeanNameForParamInConstructor(Parameter parameter, List<String> constructorParamNames) {
        Class<?> clazz = parameter.getType();
        String paramName = constructorParamNames.get(ReflectionUtils.extractParameterPosition(parameter));

        List<String> beanNames = clazz.isInterface()
                ? getTypeToBeanNames().entrySet()
                    .stream()
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
            return beanNames.stream()
                    .filter(name -> name.equalsIgnoreCase(paramName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchBeanException(clazz));
        }
    }

    private Object getOrCreateBean(String beanName, Class<?> beanType) {
        Object existingBean = getSingletonObjects().get(beanName);
        if (existingBean != null) {
            return existingBean;
        }

        BeanDefinition beanDefinition = getBeanDefinitions().get(beanName);

        if (Objects.isNull(beanDefinition)) {
            //TODO this is like workaround need to think how to populate bean definition for interface & dependencies
            if (beanType.isInterface()) {
                registerImplementations(beanName, beanType);
            }
        } else {
            registerBean(beanName, beanDefinition);
        }


        Object bean = Optional.ofNullable(getSingletonObjects().get(beanName))
          .orElse(Optional.ofNullable(getPrototypeSuppliers().get(beanName))
            .map(Supplier::get)
            .orElse(null));
        if (Objects.isNull(bean)) {
            return getOrCreateBean(beanName, beanType);
        }

        return bean;
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
                .orElseThrow(() -> new IllegalStateException("No suitable resolver found for " + clazz.getName()));
    }

    protected Reflections getReflections() {
        return reflections;
    }

}
