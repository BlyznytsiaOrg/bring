package com.bobocode.bring.core.context.type.parameter;

import com.bobocode.bring.core.context.impl.AnnotationBringBeanRegistry;
import com.bobocode.bring.core.context.scaner.ClassPathScannerFactory;
import com.bobocode.bring.core.context.type.AbstractValueTypeInjector;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

/**
 * The {@code ParameterMapValueTypeInjector} class is responsible for handling parameter injections for types of Map.
 * It extends the functionality of the AbstractValueTypeInjector and works in conjunction with a framework,
 * utilizing the bean registry and classpath scanner to recognize parameters of type Map. However, this injector
 * does not support injecting dependencies for Map types and throws an UnsupportedOperationException if attempted.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
public class ParameterMapValueTypeInjector extends AbstractValueTypeInjector implements ParameterValueTypeInjector{

    private static final String UN_SUPPORTED_OPERATION_MESSAGE = "Map type of injection is not supported, parameter: %s.";

    private final Reflections reflections;

    /**
     * Constructs a new instance of {@code ParameterMapValueTypeInjector}.
     *
     * @param reflections               The Reflections instance for scanning annotated classes.
     * @param beanRegistry              The bean registry for managing created beans.
     * @param classPathScannerFactory   The factory for creating classpath scanners.
     */
    public ParameterMapValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
                                         ClassPathScannerFactory classPathScannerFactory) {
        super(beanRegistry, classPathScannerFactory);
        this.reflections = reflections;
    }

    /**
     * Checks if the specified parameter is annotated with a value.
     *
     * @param parameter The parameter to check.
     * @return {@code true} if the parameter is of type Map, {@code false} otherwise.
     */
    @Override
    public boolean hasAnnotatedWithValue(Parameter parameter) {
        return Map.class.isAssignableFrom(parameter.getType());
    }

    /**
     * Throws an UnsupportedOperationException since injecting dependencies for Map types is not supported.
     *
     * @param parameter                  The parameter for which the injection is attempted.
     * @param createdBeanAnnotations     The annotations used for creating the bean.
     * @return This method always throws an UnsupportedOperationException.
     * @throws UnsupportedOperationException If the injection is attempted for Map types.
     */
    @Override
    public Object setValueToSetter(Parameter parameter, String parameterName, List<Class<? extends Annotation>> createdBeanAnnotations) {
        log.error(String.format(UN_SUPPORTED_OPERATION_MESSAGE, parameter.getName()));
        throw new UnsupportedOperationException(String.format(UN_SUPPORTED_OPERATION_MESSAGE, parameter.getName()));
    }
}
