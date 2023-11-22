package com.bobocode.bring.core.bpp;

import com.bobocode.bring.core.bpp.impl.ScheduleBeanPostProcessor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * The {@code BeanPostProcessorFactory} class is responsible for creating and managing a list
 * of {@link BeanPostProcessor} instances. It initializes the list with default post-processors,
 * such as the {@link ScheduleBeanPostProcessor}.
 *
 * <p>This class is typically used to provide additional processing for beans during their
 * initialization phase in the BringApplicationContext.
 *
 *
 * <p>The default post-processor included in the list is:
 * <ul>
 *     <li>{@link ScheduleBeanPostProcessor}: Processes beans annotated with {@code @Scheduled}.</li>
 * </ul>
 *
 * <p>Usage example:
 * <pre>{@code
 * BeanPostProcessorFactory processorFactory = new BeanPostProcessorFactory();
 * List<BeanPostProcessor> processors = processorFactory.getBeanPostProcessors();
 * }</pre>
 *
 * @see BeanPostProcessor
 * @see ScheduleBeanPostProcessor
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Getter
@Slf4j
public class BeanPostProcessorFactory {

    /**
     * The list of bean post-processors created by this factory.
     */
    private final List<BeanPostProcessor> beanPostProcessors;

    /**
     * Constructs a new BeanPostProcessorFactory and initializes the list of post-processors.
     *
     * <p>The default post-processor included in the list is the {@link ScheduleBeanPostProcessor}.
     *
     * @see ScheduleBeanPostProcessor
     */
    public BeanPostProcessorFactory() {
        this.beanPostProcessors = Arrays.asList(
                new ScheduleBeanPostProcessor()
        );

        log.info("Register BeanPostProcessors {}", Arrays.toString(beanPostProcessors.toArray()));
    }
}
