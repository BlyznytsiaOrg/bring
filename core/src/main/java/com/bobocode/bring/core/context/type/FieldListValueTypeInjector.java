package com.bobocode.bring.core.context.type;

import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

public class FieldListValueTypeInjector implements FieldValueTypeInjector {

    private final Reflections reflections;

    public FieldListValueTypeInjector(Reflections reflections) {
        this.reflections = reflections;
    }

    @Override
    public boolean hasAnnotatedWithValue(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }

    @Override
    public Object setValueToField(Field field, Object bean) {
        ParameterizedType genericTypeOfField = (ParameterizedType) field.getGenericType();
        return extractImplClasses(genericTypeOfField, reflections);
    }
}