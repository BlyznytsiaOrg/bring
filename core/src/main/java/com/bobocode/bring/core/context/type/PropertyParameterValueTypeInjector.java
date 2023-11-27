package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.annotation.Value;
import com.bobocode.bring.core.exception.PropertyValueNotFoundException;
import com.bobocode.bring.core.utils.TypeCast;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * The `PropertyParameterValueTypeInjector` class is responsible for injecting property values into constructor parameters annotated with {@link Value}.
 * It extends the {@link AbstractPropertyValueTypeInjector} class and implements the {@link ParameterValueTypeInjector} interface.
 * <p>
 * This injector is designed to work with a provided {@link Map} of properties.
 * It leverages the {@link Value} annotation to identify constructor parameters that require property value injection.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
public class PropertyParameterValueTypeInjector extends AbstractPropertyValueTypeInjector implements ParameterValueTypeInjector {

    /**
     * Constructs a PropertyParameterValueTypeInjector with a given set of properties.
     *
     * @param properties a Map containing property key-value pairs
     */
    public PropertyParameterValueTypeInjector(Map<String, String> properties) {
        super(properties);
    }

    /**
     * Checks if the provided parameter is annotated with the {@link Value} annotation.
     *
     * @param parameter the parameter to check for the {@link Value} annotation
     * @return true if the parameter is annotated with {@link Value}, false otherwise
     */
    @Override
    public boolean hasAnnotatedWithValue(Parameter parameter) {
        return parameter.isAnnotationPresent(Value.class);
    }

    /**
     * Retrieves the property value based on the {@link Value} annotation and sets it to the setter method.
     *
     * @param parameter              the parameter annotated with {@link Value}
     * @param createdBeanAnnotations a list of annotations associated with the created bean
     * @return the property value converted to the type of the parameter
     * @throws PropertyValueNotFoundException if the property value is not found for the specified key and field
     */
    @SneakyThrows
    @Override
    public Object setValueToSetter(Parameter parameter, List<Class<? extends Annotation>> createdBeanAnnotations) {
        return TypeCast.cast(getValue(parameter.getAnnotation(Value.class), parameter.getName()), parameter.getType());
    }
}
