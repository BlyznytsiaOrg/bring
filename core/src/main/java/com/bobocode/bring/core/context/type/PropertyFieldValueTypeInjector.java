package com.bobocode.bring.core.context.type;

import com.bobocode.bring.core.annotation.Value;
import com.bobocode.bring.core.exception.PropertyValueNotFoundException;
import com.bobocode.bring.core.utils.TypeCast;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * The `PropertyFieldValueTypeInjector` class is responsible for injecting property values into fields annotated with {@link Value}.
 * It extends the {@link AbstractPropertyValueTypeInjector} class and implements the {@link FieldValueTypeInjector} interface.
 * <p>
 * This injector is designed to work with a provided {@link Map} of properties.
 * It leverages the {@link Value} annotation to identify fields that require property value injection.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
public class PropertyFieldValueTypeInjector extends AbstractPropertyValueTypeInjector implements FieldValueTypeInjector {

    /**
     * Constructs a new instance of the injector with the specified properties.
     *
     * @param properties A {@link Map} containing key-value pairs of configuration properties.
     */
    public PropertyFieldValueTypeInjector(Map<String, String> properties) {
        super(properties);
    }

    /**
     * Checks if the given field is annotated with {@link Value}.
     *
     * @param field The field to be inspected.
     * @return {@code true} if the field is annotated with {@link Value}, otherwise {@code false}.
     */
    @Override
    public boolean hasAnnotatedWithValue(Field field) {
        return field.isAnnotationPresent(Value.class);
    }

    /**
     * Sets the value to the specified field based on the {@link Value} annotation.
     *
     * @param field                   The field to which the value will be set.
     * @param bean                    The target object instance.
     * @param createdBeanAnnotations A list of annotations present on the created bean.
     * @return The value after type casting.
     * @throws PropertyValueNotFoundException If the property value is not found.
     */
    @Override
    public Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations) {
        return TypeCast.cast(getValue(field.getAnnotation(Value.class), field.getName()), field.getType());
    }
}
