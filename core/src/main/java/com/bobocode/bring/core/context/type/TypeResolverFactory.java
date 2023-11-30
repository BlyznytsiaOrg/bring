package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.field.FieldValueTypeInjector;
import com.bobocode.bring.core.context.type.field.PropertyFieldValueTypeInjector;
import com.bobocode.bring.core.context.type.parameter.ParameterValueTypeInjector;
import com.bobocode.bring.core.context.type.parameter.PropertyParameterValueTypeInjector;
import com.bobocode.bring.core.utils.ReflectionUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@code TypeResolverFactory} class is responsible for creating instances of {@link FieldValueTypeInjector}
 * and {@link ParameterValueTypeInjector} implementations based on the provided configuration and classpath scanning.
 * It scans the classpath for all available injectors, instantiates them, and adds additional injectors for handling
 * properties. The factory is configured with a set of properties, a Reflections instance for classpath scanning,
 * and an AnnotationBringBeanRegistry for managing created beans.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Getter
@Slf4j
public class TypeResolverFactory {

    /**
     * The list of field value type injectors used for resolving field dependencies.
     */
    private List<FieldValueTypeInjector> fieldValueTypeInjectors;
    /**
     * The list of parameter value type injectors used for resolving method parameter dependencies.
     */
    private List<ParameterValueTypeInjector> parameterValueTypeInjectors;
    /**
     * The properties used for configuration.
     */
    private final Map<String, String> properties;

    /**
     * Constructs a new instance of {@code TypeResolverFactory} with the provided configuration.
     *
     * @param properties   The configuration properties.
     * @param reflections  The Reflections instance for classpath scanning.
     * @param beanRegistry The AnnotationBringBeanRegistry for managing created beans.
     */
    public TypeResolverFactory(Map<String, String> properties,
                               Reflections reflections,
                               AnnotationBringBeanRegistry beanRegistry) {
        Map<Class<?>, Object> parameterTypesToInstance = new LinkedHashMap<>();
        parameterTypesToInstance.put(Reflections.class, reflections);
        parameterTypesToInstance.put(AnnotationBringBeanRegistry.class, beanRegistry);
        parameterTypesToInstance.put(ClassPathScannerFactory.class, beanRegistry.getClassPathScannerFactory());

        setFieldValueTypeInjectors(properties, reflections, parameterTypesToInstance);
        setParameterValueTypeInjectors(properties, reflections, parameterTypesToInstance);
        this.properties = properties;
    }

    /**
     * Initializes the list of field value type injectors by scanning the classpath for implementations
     * of {@link FieldValueTypeInjector}. Additional injectors are added to handle property-based dependencies.
     *
     * @param properties               The configuration properties.
     * @param reflections              The Reflections instance for classpath scanning.
     * @param parameterTypesToInstance Map of parameter types to their corresponding instances for dependency injection.
     */
    private void setFieldValueTypeInjectors(Map<String, String> properties,
                                            Reflections reflections,
                                            Map<Class<?>, Object> parameterTypesToInstance) {
        this.fieldValueTypeInjectors = reflections.getSubTypesOf(FieldValueTypeInjector.class).stream()
                .filter(clazz -> !clazz.equals(PropertyFieldValueTypeInjector.class))
                .map(clazz -> clazz.cast(ReflectionUtils.getConstructorWithParameters(clazz, parameterTypesToInstance)))
                .collect(Collectors.toList());
        this.fieldValueTypeInjectors.add(new PropertyFieldValueTypeInjector(properties));
    }


    /**
     * Initializes the list of parameter value type injectors by scanning the classpath for implementations
     * of {@link ParameterValueTypeInjector}. Additional injectors are added to handle property-based dependencies.
     *
     * @param properties               The configuration properties.
     * @param reflections              The Reflections instance for classpath scanning.
     * @param parameterTypesToInstance Map of parameter types to their corresponding instances for dependency injection.
     */
    private void setParameterValueTypeInjectors(Map<String, String> properties,
                                                Reflections reflections,
                                                Map<Class<?>, Object> parameterTypesToInstance) {
        this.parameterValueTypeInjectors = reflections.getSubTypesOf(ParameterValueTypeInjector.class).stream()
                .filter(clazz -> !clazz.equals(PropertyParameterValueTypeInjector.class))
                .map(clazz -> clazz.cast(ReflectionUtils.getConstructorWithParameters(clazz, parameterTypesToInstance)))
                .collect(Collectors.toList());
        this.parameterValueTypeInjectors.add(new PropertyParameterValueTypeInjector(properties));
    }

}
