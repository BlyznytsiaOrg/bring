package com.bobocode.bring.core.context.type;

import java.lang.reflect.Parameter;

public interface ParameterValueTypeInjector {

    boolean hasAnnotatedWithValue(Parameter parameter);

    Object setValueToSetter(Parameter parameter);
}
