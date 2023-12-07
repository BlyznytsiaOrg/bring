package io.github.blyznytsiaorg.bring.core.context.type.parameter;

import io.github.blyznytsiaorg.bring.core.context.impl.AnnotationBringBeanRegistry;
import io.github.blyznytsiaorg.bring.core.context.scaner.ClassPathScannerFactory;
import io.github.blyznytsiaorg.bring.core.context.type.AbstractValueTypeInjector;
import io.github.blyznytsiaorg.bring.core.utils.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static io.github.blyznytsiaorg.bring.core.utils.ReflectionUtils.extractImplClasses;

/**
 * The {@code ParameterListValueTypeInjector} class is responsible for handling parameter injections for types of List.
 * It extends the functionality of the AbstractValueTypeInjector and works in conjunction with a framework,
 * utilizing the bean registry and classpath scanner to recognize parameters of type List. The injector extracts
 * implementation classes based on the generic type of the List and injects the corresponding dependencies.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public class ParameterListValueTypeInjector extends AbstractValueTypeInjector implements ParameterValueTypeInjector{

    private final Reflections reflections;

    /**
     * Constructs a new instance of {@code ParameterListValueTypeInjector}.
     *
     * @param reflections               The Reflections instance for scanning annotated classes.
     * @param beanRegistry              The bean registry for managing created beans.
     * @param classPathScannerFactory   The factory for creating classpath scanners.
     */
    public ParameterListValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
                                          ClassPathScannerFactory classPathScannerFactory) {
        super(beanRegistry, classPathScannerFactory);
        this.reflections = reflections;
    }

    /**
     * Checks if the specified parameter is annotated with a value.
     *
     * @param parameter The parameter to check.
     * @return {@code true} if the parameter is of type List, {@code false} otherwise.
     */
    @Override
    public boolean hasAnnotatedWithValue(Parameter parameter) {
        return List.class.isAssignableFrom(parameter.getType());
    }

    /**
     * Sets the value to the specified parameter by injecting dependencies based on the generic type of the List.
     *
     * @param parameter                  The parameter to inject value into.
     * @param createdBeanAnnotations     The annotations used for creating the bean.
     * @return The result of injecting dependencies into the List parameter.
     */
    @Override
    public Object setValueToSetter(Parameter parameter, String parameterName, List<Class<? extends Annotation>> createdBeanAnnotations) {
        ParameterizedType genericTypeOfField = (ParameterizedType) parameter.getParameterizedType();
        List<Class<?>> dependencies = ReflectionUtils.extractImplClasses(genericTypeOfField, reflections, createdBeanAnnotations);
        return injectListDependency(dependencies);
    }
}
