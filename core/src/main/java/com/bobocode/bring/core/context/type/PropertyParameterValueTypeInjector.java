package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.anotation.Value;
import com.bobocode.bring.core.utils.TypeCast;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class PropertyParameterValueTypeInjector implements ParameterValueTypeInjector {

    private final Map<String, String> properties;

    @Override
    public boolean hasAnnotatedWithValue(Parameter parameter) {
        return parameter.isAnnotationPresent(Value.class);
    }

    @SneakyThrows
    @Override
    public Object setValueToSetter(Parameter parameter, List<Class<? extends Annotation>> createdBeanAnnotations) {
        Value valueAnnotation = parameter.getAnnotation(Value.class);
        Class<?> type = parameter.getType();
        String key = valueAnnotation.value();
        String value = properties.get(key);
        if (Objects.isNull(value)) {
            return null;
        }
        return TypeCast.cast(value, parameter.getType());
    }
}
