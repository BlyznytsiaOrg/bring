package com.bobocode.bring.core.bpp.impl;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.ScheduledTask;
import com.bobocode.bring.core.bpp.BeanPostProcessor;
import com.bobocode.bring.core.bpp.impl.schedule.CustomScheduler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * A BeanProcessor responsible for processing beans and registering methods annotated with @ScheduledTask
 * for scheduling via a CustomScheduler.
 * <p>
 * This class scans for methods annotated with @ScheduledTask in Spring-managed beans and schedules them for execution
 * using the provided CustomScheduler.
 * <p>
 * Example usage:
 * This processor can be registered in a Spring context to automatically schedule methods annotated with @ScheduledTask
 * at specified intervals.
 *
 * @see com.bobocode.bring.core.bpp.BeanPostProcessor
 * @see ScheduledTask
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
@Component
@Slf4j
public class ScheduleBeanPostProcessor implements BeanPostProcessor {

    /** The CustomScheduler used for scheduling tasks. */
    private final CustomScheduler customScheduler;

    public ScheduleBeanPostProcessor(CustomScheduler customScheduler) {
        this.customScheduler = customScheduler;
    }

    /**
     * Processes bean initialization and schedules methods annotated with @ScheduledTask.
     *
     * @param bean     the bean instance
     * @param beanName the name of the bean
     * @return the processed bean
     */
    @Override
    public Object postProcessInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();

        for (Method method : beanClass.getMethods()) {
            ScheduledTask annotation = method.getAnnotation(ScheduledTask.class);
            if (annotation != null) {
                var taskName = annotation.value();
                log.info("Register task with name " + taskName + " in class " +
                        beanClass.getPackageName() + "." + beanClass.getSimpleName());
                customScheduler.scheduleTask(
                        () -> invokeTaskMethod(bean, method, taskName),
                        annotation.initialDelay(), annotation.period(), annotation.timeUnit()
                );
            }
        }

        return BeanPostProcessor.super.postProcessInitialization(bean, beanName);
    }

    /**
     * Invokes the method annotated with @ScheduledTask on the provided bean instance.
     *
     * @param bean     the bean instance
     * @param method   the method to be invoked
     * @param taskName the name of the scheduled task
     */
    private void invokeTaskMethod(Object bean, Method method, String taskName) {
        try {
            method.invoke(bean);
        } catch (Exception exe) {
            log.error("Cannot invoke taskName [" + taskName + "] error " + exe.getMessage(), exe);
        }
    }
}
