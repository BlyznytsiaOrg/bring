package com.bobocode.bring.core.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public final class ReflectionUtils {

    @SneakyThrows
    public static void setField(Field field, Object obj, Object value) {
        field.setAccessible(true);
        field.set(obj, value);
    }
}
