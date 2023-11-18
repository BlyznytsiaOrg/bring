package com.bobocode.bring.core.context.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;

public interface ParameterValueTypeInjector {

    boolean hasAnnotatedWithValue(Parameter parameter);

    Object setValueToSetter(Parameter parameter, List<Class<? extends Annotation>> createdBeanAnnotations);
}
