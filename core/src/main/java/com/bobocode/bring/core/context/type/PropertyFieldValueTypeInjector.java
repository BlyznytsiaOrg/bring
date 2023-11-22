package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.anotation.Value;
import com.bobocode.bring.core.utils.TypeCast;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class PropertyFieldValueTypeInjector implements FieldValueTypeInjector {

    private final Map<String, String> properties;

    @Override
    public boolean hasAnnotatedWithValue(Field field) {
        return field.isAnnotationPresent(Value.class);
    }

    @Override
    public Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations) {
        Value valueAnnotation = field.getAnnotation(Value.class);
        String key = valueAnnotation.value();
        String value = properties.get(key);
        if (Objects.isNull(value)) {
            return null;
        }
        return TypeCast.cast(value, field.getType());
    }
}
