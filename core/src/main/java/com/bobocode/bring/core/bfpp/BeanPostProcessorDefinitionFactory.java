package com.bobocode.bring.core.bfpp;

import com.bobocode.bring.core.bfpp.impl.ConfigurationClassPostProcessor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.bobocode.bring.core.utils.ReflectionUtils.ORDER_COMPARATOR;
import static com.bobocode.bring.core.utils.ReflectionUtils.getConstructorWithOutParameters;

/**
 * The {@code BeanPostProcessorDefinitionFactory} class is responsible for creating and managing a list
 * of {@link BeanFactoryPostProcessor} instances. It initializes the list with default post-processors,
 * such as the {@link ConfigurationClassPostProcessor}.
 *
 * <p>This class is typically used to provide additional processing during the startup of the
 * BringApplicationContext to handle configuration class annotations and perform necessary tasks.
 *
 * <p>The default post-processor included in the list is:
 * <ul>
 *     <li>{@link ConfigurationClassPostProcessor}: Processes configuration classes and their annotations.</li>
 * </ul>
 *
 * <p>Usage example:
 * <pre>{@code
 * BeanPostProcessorDefinitionFactory processorFactory = new BeanPostProcessorDefinitionFactory();
 * List<BeanFactoryPostProcessor> processors = processorFactory.getBeanFactoryPostProcessors();
 * }</pre>
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 * @see BeanFactoryPostProcessor
 * @see ConfigurationClassPostProcessor
 */
@Getter
@Slf4j
public class BeanPostProcessorDefinitionFactory {

    /**
     * The list of bean factory post-processors created by this factory.
     */
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors;

    /**
     * Constructs a new BeanPostProcessorDefinitionFactory and initializes the list of post-processors.
     *
     * <p>The default post-processor included in the list is the {@link ConfigurationClassPostProcessor}.
     *
     * @see ConfigurationClassPostProcessor
     */
    public BeanPostProcessorDefinitionFactory(Reflections reflections) {
        this.beanFactoryPostProcessors = reflections.getSubTypesOf(BeanFactoryPostProcessor.class)
                .stream()
                .sorted(ORDER_COMPARATOR)
                .map(clazz -> clazz.cast(getConstructorWithOutParameters(clazz)))
                .collect(Collectors.toList());

        log.info("Register BeanFactoryPostProcessors {}", Arrays.toString(beanFactoryPostProcessors.toArray()));
    }
}
