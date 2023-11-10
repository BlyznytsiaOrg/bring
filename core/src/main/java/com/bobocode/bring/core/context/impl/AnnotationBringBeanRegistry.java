package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.Service;
import com.bobocode.bring.core.anotation.resolver.AnnotationResolver;
import com.bobocode.bring.core.anotation.resolver.impl.ComponentBeanNameAnnotationResolver;
import com.bobocode.bring.core.anotation.resolver.impl.InterfaceBeanNameAnnotationResolver;
import com.bobocode.bring.core.anotation.resolver.impl.ServiceBeanNameAnnotationResolver;
import com.bobocode.bring.core.context.BeanRegistry;
import com.bobocode.bring.core.exception.CyclicBeanException;
import com.bobocode.bring.core.exception.NoConstructorWithAutowiredAnnotationBeanException;
import com.bobocode.bring.core.exception.NoUniqueBeanException;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static com.bobocode.bring.core.utils.ReflectionUtils.setField;

public class AnnotationBringBeanRegistry extends AbstractBringBeanFactory implements BeanRegistry {

    private static final String SET_METHOD_START_PREFIX = "set";

    private final Reflections reflections;

    private final List<AnnotationResolver> annotationResolvers = List.of(
            new ComponentBeanNameAnnotationResolver(),
            new ServiceBeanNameAnnotationResolver(),
            new InterfaceBeanNameAnnotationResolver()
    );

    private final List<Class<? extends Annotation>> createdBeanAnnotations = List.of(
            Component.class, Service.class
    );

    private final Set<String> currentlyCreatingBeans = new HashSet<>();

    public AnnotationBringBeanRegistry(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public <T> void registerBean(Class<T> clazz) {
        String beanName = resolveBeanName(clazz);

        if (currentlyCreatingBeans.contains(beanName)) {
            throw new CyclicBeanException(currentlyCreatingBeans);
        }

        if (getBeansNameToObject().containsKey(beanName)) {
            return; // Bean with this name already created, no need to create it again.
        }

        currentlyCreatingBeans.add(beanName);

        if (clazz.isInterface()) {
            registerImplementations(clazz);
        } else {
            findAutowiredConstructor(clazz)
                    .map(constructorToUse -> createBeanUsingConstructor(constructorToUse, beanName))
                    .orElseThrow(() -> new NoConstructorWithAutowiredAnnotationBeanException(clazz));

            injectDependencies(clazz, beanName);
        }

        currentlyCreatingBeans.clear();
    }

    private <T> void registerImplementations(Class<T> interfaceType) {
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
                registerBean(implementation);
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
    private Object createBeanUsingConstructor(Constructor<?> constructor, String beanName) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            String dependencyBeanName = resolveBeanName(parameterTypes[i]);
            Object dependencyObject = getOrCreateBean(dependencyBeanName, parameterTypes[i]);
            dependencies[i] = dependencyObject;
        }

        Object bean = constructor.newInstance(dependencies);

        for (var interfaceClass : bean.getClass().getInterfaces()) {
            addInterfaceNameToImplementations(interfaceClass.getSimpleName(), bean);
        }

        addBean(beanName, bean);

        return constructor;
    }

    private Object getOrCreateBean(String beanName, Class<?> beanType) {
        Object existingBean = getBeansNameToObject().get(beanName);
        if (existingBean != null) {
            return existingBean;
        }

        List<Object> implementations = getInterfaceNameToImplementations(beanName);
        if (!implementations.isEmpty()) {
            if (implementations.size() == 1) {
                return implementations.get(0);
            }

            throw new NoUniqueBeanException(beanType.getPackageName() + "." + beanName, implementations);
        }

        registerBean(beanType);

        Object object = getBeansNameToObject().get(beanName);
        if (Objects.isNull(object)){
            return getOrCreateBean(beanName, beanType);
        }

        return object;
    }

    private void injectDependencies(Class<?> clazz, String beanName) {
        Object bean = getBeansNameToObject().get(beanName);

        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .forEach(field -> injectDependencyViaField(field, bean));

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(this::isAutowiredSetterMethod)
                .forEach(method -> injectDependencyViaMethod(method, bean));
    }

    @SneakyThrows
    private void injectDependencyViaMethod(Method method, Object bean) {
        if (isAutowiredSetterMethod(method)) {
            Class<?> dependencyType = method.getParameterTypes()[0];
            String dependencyBeanName = resolveBeanName(dependencyType);
            Object dependencyObject = getOrCreateBean(dependencyBeanName, dependencyType);
            method.invoke(bean, dependencyObject);
        }
    }

    @SneakyThrows
    private void injectDependencyViaField(Field field, Object bean) {
        String dependencyBeanName = resolveBeanName(field.getType());
        Object dependencyObject = getOrCreateBean(dependencyBeanName, field.getType());
        setField(field, bean, dependencyObject);
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

    protected List<Class<? extends Annotation>> getCreatedBeanAnnotations() {
        return createdBeanAnnotations;
    }
}
