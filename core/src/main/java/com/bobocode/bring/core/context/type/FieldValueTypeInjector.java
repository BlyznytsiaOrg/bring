package com.bobocode.bring.core.context.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public interface FieldValueTypeInjector {

    boolean hasAnnotatedWithValue(Field field);

    Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations);
}
