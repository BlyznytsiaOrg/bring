package com.bobocode.bring.core.context.type;

import java.lang.reflect.Field;

public interface FieldValueTypeInjector {

    boolean hasAnnotatedWithValue(Field field);

    Object setValueToField(Field field, Object bean);
}
