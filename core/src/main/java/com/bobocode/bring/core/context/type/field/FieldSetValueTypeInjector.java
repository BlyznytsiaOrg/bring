package com.bobocode.bring.core.context.type.field;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.AbstractValueTypeInjector;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import static com.bobocode.bring.core.utils.ReflectionUtils.extractImplClasses;

/**
 * The {@code FieldSetValueTypeInjector} class is responsible for handling field injections for types of Set.
 * It extends the functionality of the AbstractValueTypeInjector and works in conjunction with a framework,
 * utilizing the bean registry and classpath scanner to recognize fields of type Set. The injector extracts
 * implementation classes based on the generic type of the Set and injects the corresponding dependencies.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class FieldSetValueTypeInjector extends AbstractValueTypeInjector implements FieldValueTypeInjector{

    private final Reflections reflections;

    /**
     * Constructs a new instance of {@code FieldSetValueTypeInjector}.
     *
     * @param reflections               The Reflections instance for scanning annotated classes.
     * @param beanRegistry              The bean registry for managing created beans.
     * @param classPathScannerFactory   The factory for creating classpath scanners.
     */
    public FieldSetValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
                                     ClassPathScannerFactory classPathScannerFactory) {
        super(beanRegistry, classPathScannerFactory);
        this.reflections = reflections;
    }

    /**
     * Checks if the specified field is annotated with a value.
     *
     * @param field The field to check.
     * @return {@code true} if the field is of type Set, {@code false} otherwise.
     */
    @Override
    public boolean hasAnnotatedWithValue(Field field) {
        return Set.class.isAssignableFrom(field.getType());
    }

    /**
     * Sets the value to the specified field by injecting dependencies based on the generic type of the Set.
     *
     * @param field                      The field to inject value into.
     * @param bean                       The target bean containing the field.
     * @param createdBeanAnnotations     The annotations used for creating the bean.
     * @return The result of injecting dependencies into the Set field.
     */
    @Override
    public Object setValueToField(Field field, Object bean, List<Class<? extends Annotation>> createdBeanAnnotations) {
        ParameterizedType genericTypeOfField = (ParameterizedType) field.getGenericType();
        List<Class<?>> dependencies = extractImplClasses(genericTypeOfField, reflections, createdBeanAnnotations);
        return injectSetDependency(dependencies);
    }
}