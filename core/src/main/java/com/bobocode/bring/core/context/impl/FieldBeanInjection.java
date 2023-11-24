package com.bobocode.bring.core.context.impl;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Qualifier;
import com.bobocode.bring.core.anotation.Value;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.bobocode.bring.core.utils.ReflectionUtils.setField;

@AllArgsConstructor
@Slf4j
public class FieldBeanInjection {

    private final AnnotationBringBeanRegistry beanRegistry;
    private final ClassPathScannerFactory classPathScannerFactory;

    public void injectViaFields(Class<?> clazz, Object bean) {
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Autowired.class) || field.isAnnotationPresent(Value.class))
                .forEach(field -> injectDependencyViaField(field, bean));
    }

    @SneakyThrows
    private void injectDependencyViaField(Field field, Object bean) {
        var createdBeanAnnotations = classPathScannerFactory.getCreatedBeanAnnotations();
        Optional<Object> injectViaProperties = beanRegistry.getTypeResolverFactory()
                .getFieldValueTypeInjectors().stream()
                .filter(valueType -> valueType.hasAnnotatedWithValue(field))
                .map(valueType -> {
                    Object dependencyValue = valueType.setValueToField(field, bean, createdBeanAnnotations);
                    if (dependencyValue instanceof List listDependencyValue) {
                        setField(field, bean, beanRegistry.injectListDependency(listDependencyValue));
                    } else {
                        setField(field, bean, dependencyValue);
                    }
                    return dependencyValue;
                })
                .findFirst();

        if (injectViaProperties.isEmpty()) {
            String dependencyBeanName = classPathScannerFactory.resolveBeanName(field.getType());
            String qualifier = field.isAnnotationPresent(Qualifier.class) ? field.getAnnotation(Qualifier.class).value() : null;
            Object dependencyObject = beanRegistry.getOrCreateBean(dependencyBeanName, field.getType(), qualifier);
            setField(field, bean, dependencyObject);
        }
    }
}
