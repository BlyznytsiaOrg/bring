package com.bobocode.bring.web.utils;

import com.thoughtworks.paranamer.AnnotationParanamer;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import lombok.experimental.UtilityClass;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public final class ReflectionUtils {

    private final Paranamer info = new CachingParanamer(new AnnotationParanamer(new BytecodeReadingParanamer()));

    public List<String> getParameterNames(AccessibleObject method) {
        return Arrays.stream(info.lookupParameterNames(method)).toList();
    }
}
