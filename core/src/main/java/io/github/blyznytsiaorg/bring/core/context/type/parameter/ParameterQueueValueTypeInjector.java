package io.github.blyznytsiaorg.bring.core.context.type.parameter;

import io.github.blyznytsiaorg.bring.core.context.impl.AnnotationBringBeanRegistry;
import io.github.blyznytsiaorg.bring.core.context.scaner.ClassPathScannerFactory;
import io.github.blyznytsiaorg.bring.core.context.type.AbstractValueTypeInjector;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Queue;

/**
 * The {@code ParameterQueueValueTypeInjector} class is responsible for handling parameter injections for types of Queue.
 * It extends the functionality of the AbstractValueTypeInjector and works in conjunction with a framework,
 * utilizing the bean registry and classpath scanner to recognize parameters of type Queue. However, this injector
 * does not support injecting dependencies for Queue types and throws an UnsupportedOperationException if attempted.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Slf4j
public class ParameterQueueValueTypeInjector extends AbstractValueTypeInjector implements ParameterValueTypeInjector{

    private static final String UN_SUPPORTED_OPERATION_MESSAGE = "Queue type of injection is not supported, parameter: %s.";

    private final Reflections reflections;

    /**
     * Constructs a new instance of {@code ParameterQueueValueTypeInjector}.
     *
     * @param reflections               The Reflections instance for scanning annotated classes.
     * @param beanRegistry              The bean registry for managing created beans.
     * @param classPathScannerFactory   The factory for creating classpath scanners.
     */
    public ParameterQueueValueTypeInjector(Reflections reflections, AnnotationBringBeanRegistry beanRegistry,
                                           ClassPathScannerFactory classPathScannerFactory) {
        super(beanRegistry, classPathScannerFactory);
        this.reflections = reflections;
    }

    /**
     * Checks if the specified parameter is annotated with a value.
     *
     * @param parameter The parameter to check.
     * @return {@code true} if the parameter is of type Queue, {@code false} otherwise.
     */
    @Override
    public boolean hasAnnotatedWithValue(Parameter parameter) {
        return Queue.class.isAssignableFrom(parameter.getType());
    }

    /**
     * Throws an UnsupportedOperationException since injecting dependencies for Queue types is not supported.
     *
     * @param parameter                  The parameter for which the injection is attempted.
     * @param createdBeanAnnotations     The annotations used for creating the bean.
     * @return This method always throws an UnsupportedOperationException.
     * @throws UnsupportedOperationException If the injection is attempted for Queue types.
     */
    @Override
    public Object setValueToSetter(Parameter parameter, String parameterName, List<Class<? extends Annotation>> createdBeanAnnotations) {
        log.error(String.format(UN_SUPPORTED_OPERATION_MESSAGE, parameter.getName()));
        throw new UnsupportedOperationException(String.format(UN_SUPPORTED_OPERATION_MESSAGE, parameter.getName()));
    }
}
