package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.anotation.Value;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.util.Map;

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
    public Object setValueToSetter(Parameter parameter) {
        Value valueAnnotation = parameter.getAnnotation(Value.class);
        Class<?> type = parameter.getType();
        String key = valueAnnotation.value();
        String value = properties.get(key);
        return type.cast(value);
    }
}
