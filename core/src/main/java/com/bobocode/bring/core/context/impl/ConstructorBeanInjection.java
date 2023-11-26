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

/**
 * Responsible for creating beans using constructor-based dependency injection.
 * This class assists in instantiating beans based on constructors annotated with @Autowired
 * or if this is one constructor no need to add Autowired.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */

@AllArgsConstructor
@Slf4j
public class ConstructorBeanInjection {

    private final AnnotationBringBeanRegistry beanRegistry;

    private final ClassPathScannerFactory classPathScannerFactory;

    /**
     * Creates a bean using constructor-based dependency injection for the provided class and bean definition.
     *
     * @param clazz          The class for which the bean is to be created.
     * @param beanName       The name of the bean.
     * @param beanDefinition The definition of the bean.
     * @throws NoConstructorWithAutowiredAnnotationBeanException If no constructor is annotated with @Autowired.
     * @throws NoSuchBeanException                               If a required dependency bean is not found.
     */
    public void create(Class<?> clazz, String beanName, BeanDefinition beanDefinition) {
        var constructor = findAutowiredConstructor(clazz);
        createBeanUsingConstructor(constructor, beanName, beanDefinition);
    }

    /**
     * Finds a constructor annotated with @Autowired within the provided class.
     * If multiple constructors exist, the one with the @Autowired annotation is selected or one without.
     *
     * @param clazz The class to search for an @Autowired annotated constructor.
     * @return The @Autowired annotated constructor.
     * @throws NoConstructorWithAutowiredAnnotationBeanException If no constructor is annotated with @Autowired.
     */
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

    /**
     * Creates a bean using the specified constructor and resolves its dependencies.
     * Instantiates the bean within the context based on resolved dependencies and the constructor.
     *
     * @param constructor    The constructor to use for bean instantiation.
     * @param beanName       The name of the bean to be created.
     * @param beanDefinition The definition of the bean being created.
     * @throws NoSuchBeanException If a required dependency bean is not found.
     */
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

    /**
     * Finds the appropriate bean name for an argument in a constructor based on its parameter type.
     * Handles scenarios with multiple candidate beans, qualifiers, and primary beans.
     *
     * @param parameter            The parameter for which the bean name is to be resolved.
     * @param constructorParamNames The names of parameters in the constructor.
     * @return The resolved bean name for the parameter.
     * @throws NoSuchBeanException      If a required bean is not found.
     * @throws NoUniqueBeanException   If multiple primary beans or qualifiers match the parameter type.
     */
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
            if (clazz.isInterface()) {
                throw new NoSuchBeanException(String.format("No such bean that implements this %s ", clazz));
            }

            throw new NoSuchBeanException(clazz);
        } else if (beanNames.size() == 1) {
            return beanNames.get(0);
        } else {
            String paramName = constructorParamNames.get(ReflectionUtils.extractParameterPosition(parameter));

            return findPrimaryBeanNameOrByQualifierOrbBParameter(beanNames, paramName, parameter);
        }
    }

    /**
     * Resolves the primary bean name or selects a bean by qualifier or parameter name.
     *
     * @param beanNames  The candidate bean names for the parameter type.
     * @param paramName  The parameter name in the constructor.
     * @param parameter  The parameter for which the bean name is to be resolved.
     * @return The resolved bean name based on primary, qualifier, or parameter name.
     * @throws NoSuchBeanException      If a required bean is not found.
     * @throws NoUniqueBeanException   If multiple primary beans or qualifiers match the parameter type.
     */
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
                    .findFirst().orElseThrow(() -> {
                        if (parameterType.isInterface()) {
                            return new NoSuchBeanException(String.format("No such bean that implements this %s ", parameterType));
                        }
                        return new NoSuchBeanException(parameterType);
                    });
        }
        else {
            return beanNames.stream()
                    .filter(name -> name.equalsIgnoreCase(paramName))
                    .findFirst()
                    .orElseThrow(() -> new NoUniqueBeanException(parameterType, beanNames));
        }
    }

    /**
     * Retrieves the qualifier value from the parameter's annotation, if present.
     *
     * @param parameter The parameter for which the qualifier value is to be retrieved.
     * @return The qualifier value if present, otherwise null.
     */
    private String getQualifier(Parameter parameter) {
        return parameter.isAnnotationPresent(Qualifier.class) ? parameter.getAnnotation(Qualifier.class).value() : null;
    }
}
