package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.anotation.Value;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class PropertyFieldValueTypeInjector implements FieldValueTypeInjector {

    private final Map<String, String> properties;

    @Override
    public boolean hasAnnotatedWithValue(Field field) {
        return field.isAnnotationPresent(Value.class);
    }

    @Override
    public Object setValueToField(Field field, Object bean) {
        Value valueAnnotation = field.getAnnotation(Value.class);
        String key = valueAnnotation.value();
        String value = properties.get(key);
        return field.getType().cast(value);
    }
}
