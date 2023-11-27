package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Value;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.field.FieldValueTypeInjector;
import com.bobocode.bring.core.exception.NoSuchBeanException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.bobocode.bring.core.utils.ReflectionUtils.setField;

/**
 * Responsible for injecting dependencies into fields of a bean using annotations like @Autowired or @Value.
 * Handles field-based dependency injection for annotated fields within a class.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@AllArgsConstructor
@Slf4j
public class FieldBeanInjection {

    private final AnnotationBringBeanRegistry beanRegistry;
    private final ClassPathScannerFactory classPathScannerFactory;

    /**
     * Injects dependencies into fields of the provided class instance based on annotations.
     *
     * @param clazz The class whose fields are to be injected with dependencies.
     * @param bean  The instance of the class whose fields are being injected.
     */
    public void injectViaFields(Class<?> clazz, Object bean) {
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class) || field.isAnnotationPresent(Value.class))
                .forEach(field -> {
                    log.trace("Injecting dependency into field {} of class [{}]", field.getName(), clazz.getName());
                    injectDependencyViaField(field, bean);
                });
    }

    /**
     * Injects a dependency into a specific field of the provided bean instance.
     *
     * @param field The field to be injected with a dependency.
     * @param bean  The instance of the class whose field is being injected.
     */
    @SneakyThrows
    private void injectDependencyViaField(Field field, Object bean) {
        var createdBeanAnnotations = classPathScannerFactory.getCreatedBeanAnnotations();
        Optional<Object> injectViaProperties = beanRegistry.getTypeResolverFactory()
                .getFieldValueTypeInjectors().stream()
                .filter(valueType -> valueType.hasAnnotatedWithValue(field))
                .map(valueType -> setFieldDependency(field, bean, valueType, createdBeanAnnotations))
                .findFirst();

        if (injectViaProperties.isEmpty()) {
            String dependencyBeanName = classPathScannerFactory.resolveBeanName(field.getType());
            if (Objects.isNull(dependencyBeanName)) {
                if (field.getType().isInterface()) {
                    log.trace("Field {} is Interface {}", field.getName(), field.getType());
                    throw new NoSuchBeanException(String.format("No such bean that implements this %s ", field.getType()));
                }
                log.trace("Cannot find any beans for field {}", field.getName());
                throw new NoSuchBeanException(field.getType());
            }
            Object dependencyObject = beanRegistry.getOrCreateBean(dependencyBeanName);
            setField(field, bean, dependencyObject);
        }
    }

    /**
     * Sets the value of a field in the provided bean instance with the resolved dependency.
     *
     * @param field       The field to be set with the resolved dependency.
     * @param bean        The instance of the class whose field is being set.
     * @param valueType   The ValueTypeInjector responsible for setting the field value.
     * @param createdBeanAnnotations List of created bean annotations used for dependency resolution.
     * @return The resolved dependency object.
     */
    private static Object setFieldDependency(Field field, Object bean, FieldValueTypeInjector valueType,
                                             List<Class<? extends Annotation>> createdBeanAnnotations) {
        Object dependency = valueType.setValueToField(field, bean, createdBeanAnnotations);
        setField(field, bean, dependency);
        return dependency;
    }
}
