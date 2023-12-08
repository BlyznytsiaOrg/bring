package io.github.blyznytsiaorg.bring.core.context.type.field;

import io.github.blyznytsiaorg.bring.core.context.impl.AnnotationBringBeanRegistry;
import io.github.blyznytsiaorg.bring.core.context.scaner.ClassPathScannerFactory;
import io.github.blyznytsiaorg.bring.core.context.type.AbstractValueTypeInjector;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * The {@code FieldMapValueTypeInjector} class is responsible for handling field injections for types of Map.
 * It extends the functionality of the AbstractValueTypeInjector and works in conjunction with a framework,
 * utilizing the bean registry and classpath scanner to recognize fields of type Map. However, this injector
 * does not support injecting dependencies for Map types and throws an UnsupportedOperationException if attempted.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
public class FieldMapValueTypeInjector extends AbstractValueTypeInjector implements FieldValueTypeInjector {

    private static final String UN_SUPPORTED_OPERATION_MESSAGE = "Map type of injection is not supported, field: %s.";

    private final Reflections reflections;

    /**
     * Constructs a new instance of {@code FieldMapValueTypeInjector}.
     *
     * @param reflections               The Reflections instance for scanning annotated classes.
     * @param beanRegistry              The bean registry for managing created beans.
     * @param classPathScannerFactory   The factory for creating classpath scanners.
     */
    public FieldMapValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
                                     ClassPathScannerFactory classPathScannerFactory) {
        super(beanRegistry, classPathScannerFactory);
        this.reflections = reflections;
    }

    /**
     * Checks if the specified field is annotated with a value.
     *
     * @param field The field to check.
     * @return {@code true} if the field is of type Map, {@code false} otherwise.
     */
    @Override
    public boolean hasAnnotatedWithValue(Field field) {
        return Map.class.isAssignableFrom(field.getType());
    }

    /**
     * Throws an UnsupportedOperationException since injecting dependencies for Map types is not supported.
     *
     * @param field                      The field for which the injection is attempted.
     * @param bean                       The target bean containing the field.
     * @param createdBeanAnnotations     The annotations used for creating the bean.
     * @return This method always throws an UnsupportedOperationException.
     * @throws UnsupportedOperationException If the injection is attempted for Map types.
     */
    @Override
    public Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations) {
        log.error(String.format(UN_SUPPORTED_OPERATION_MESSAGE, field.getName()));
        throw new UnsupportedOperationException(String.format(UN_SUPPORTED_OPERATION_MESSAGE, field.getName()));
    }
}