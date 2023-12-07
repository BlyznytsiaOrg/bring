package io.github.blyznytsiaorg.bring.core.context.type.parameter;

import io.github.blyznytsiaorg.bring.core.exception.PropertyValueNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * {@code ParameterValueTypeInjector} is an interface that defines methods for injecting values into parameters
 * annotated with the {@code @Value} annotation. Implementations of this interface are responsible for checking if
 * a parameter has the {@code @Value} annotation and setting the corresponding value to the parameter from an
 * external source, such as a properties map.
 * <p>
 * Implementations may use utility classes, type casting, and exception handling to handle the injection process
 * efficiently. In case of a property value not being found for a specified key, an exception
 * ({@link PropertyValueNotFoundException}) should be thrown.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public interface ParameterValueTypeInjector {

    /**
     * Checks if the provided parameter is annotated with the {@code @Value} annotation.
     *
     * @param parameter The parameter to check for the presence of the {@code @Value} annotation.
     * @return {@code true} if the parameter is annotated with {@code @Value}, {@code false} otherwise.
     */
    boolean hasAnnotatedWithValue(Parameter parameter);

    /**
     * Sets the value to the specified parameter based on an external source.
     *
     * @param parameter               The parameter to set the value for.
     * @param createdBeanAnnotations A list of annotations present on the created bean.
     * @return The injected value converted to the type of the parameter.
     * @throws PropertyValueNotFoundException If the property value is not found for the specified key.
     */
    Object setValueToSetter(Parameter parameter, String parameterName, List<Class<? extends Annotation>> createdBeanAnnotations);
}
