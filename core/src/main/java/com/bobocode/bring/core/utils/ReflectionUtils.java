package com.bobocode.bring.core.utils;

import com.thoughtworks.paranamer.AnnotationParanamer;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public final class ReflectionUtils {

    private final Paranamer info = new CachingParanamer(new AnnotationParanamer(new BytecodeReadingParanamer()));
    
    @SneakyThrows
    public static void setField(Field field, Object obj, Object value) {
        field.setAccessible(true);
        field.set(obj, value);
    }
    
    public List<String> getMethodParameterNames(Method method) {
        return Arrays.stream(info.lookupParameterNames(method)).toList();
    }
    
}
