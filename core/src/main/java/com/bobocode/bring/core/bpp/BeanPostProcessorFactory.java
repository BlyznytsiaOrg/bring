package com.bobocode.bring.core.bpp;

import static com.bobocode.bring.core.utils.ReflectionUtils.getConstructorWithOutParameters;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.bpp.impl.ScheduleBeanPostProcessor;
import com.bobocode.bring.core.bpp.impl.schedule.CustomScheduler;
import com.bobocode.bring.core.context.BringBeanFactory;
import com.bobocode.bring.core.utils.ReflectionUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code BeanPostProcessorFactory} class is responsible for creating and managing a list
 * of {@link BeanPostProcessor} instances. It initializes the list with default post-processors,
 * such as the {@link ScheduleBeanPostProcessor}.
 *
 * <p>This class is typically used to provide additional processing for beans during their
 * initialization phase in the BringApplicationContext.
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
 * @see BeanProcessor
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
     * @param beanFactory  the BringBeanFactory instance for obtaining beans
     * @param reflections  the Reflections instance for scanning classes
     * @see ScheduleBeanPostProcessor
     */
    public BeanPostProcessorFactory(BringBeanFactory beanFactory, Reflections reflections) {
        this.beanPostProcessors = reflections.getSubTypesOf(BeanPostProcessor.class)
                .stream()
                .filter(clazz -> !clazz.equals(ScheduleBeanPostProcessor.class))
                .filter(clazz -> clazz.isAnnotationPresent(BeanProcessor.class))
                .sorted(ReflectionUtils.ORDER_COMPARATOR)
                .map(clazz -> clazz.cast(getConstructorWithOutParameters(clazz)))
                .collect(Collectors.toList());

        CustomScheduler customScheduler = beanFactory.getBean(CustomScheduler.class);
        this.beanPostProcessors.add(new ScheduleBeanPostProcessor(customScheduler));

        log.info("Register BeanPostProcessors {}", Arrays.toString(beanPostProcessors.toArray()));
    }
}
