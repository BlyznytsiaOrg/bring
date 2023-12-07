package io.github.blyznytsiaorg.bring.core.context.type.field;

import io.github.blyznytsiaorg.bring.core.exception.PropertyValueNotFoundException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The {@code FieldValueTypeInjector} interface defines methods for injecting values into fields annotated with the
 * {@code @Value} annotation. Implementations of this interface are responsible for checking if a field has the
 * {@code @Value} annotation and setting the corresponding value to the field from an external source, such as a
 * properties map.
 * <p>
 * Implementations may use utility classes, type casting, and exception handling to handle the injection process
 * efficiently. In case of a property value not being found for a specified key, an exception
 * ({@link PropertyValueNotFoundException}) should be thrown.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public interface FieldValueTypeInjector {

    /**
     * Checks if the provided field is annotated with the {@code @Value} annotation.
     *
     * @param field The field to check for the presence of the {@code @Value} annotation.
     * @return {@code true} if the field is annotated with {@code @Value}, {@code false} otherwise.
     */
    boolean hasAnnotatedWithValue(Field field);

    /**
     * Sets the value to the specified field based on an external source.
     *
     * @param field                   The field to set the value for.
     * @param bean                    The object containing the field.
     * @param createdBeanAnnotations A list of annotations present on the created bean.
     * @return The injected value converted to the type of the field.
     * @throws PropertyValueNotFoundException If the property value is not found for the specified key.
     */
    Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations);
}
