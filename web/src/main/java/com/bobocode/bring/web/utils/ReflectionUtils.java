package com.bobocode.bring.web.utils;

import com.thoughtworks.paranamer.AnnotationParanamer;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import lombok.experimental.UtilityClass;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code ReflectionUtils} class is a utility class providing methods for reflection-related operations.
 * It utilizes the Paranamer library to retrieve parameter names of accessible methods.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@UtilityClass
public final class ReflectionUtils {

    /**
     * An instance of Paranamer for parameter name lookup.
     */
    private final Paranamer info = new CachingParanamer(new AnnotationParanamer(new BytecodeReadingParanamer()));

    /**
     * Retrieves the parameter names of the specified accessible method or constructor.
     *
     * @param method The accessible method or constructor.
     * @return A list of parameter names in the order they appear in the method signature.
     */
    public List<String> getParameterNames(AccessibleObject method) {
        return Arrays.stream(info.lookupParameterNames(method)).toList();
    }
}
