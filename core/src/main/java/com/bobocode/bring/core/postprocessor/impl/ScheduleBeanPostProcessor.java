package com.bobocode.bring.core.postprocessor.impl;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.ScheduledTask;
import com.bobocode.bring.core.postprocessor.BeanPostProcessor;
import com.bobocode.bring.core.schedule.CustomScheduleConfiguration;
import com.bobocode.bring.core.schedule.CustomScheduler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Component
@Slf4j
public class ScheduleBeanPostProcessor implements BeanPostProcessor {

    private final CustomScheduler customScheduler;

    public ScheduleBeanPostProcessor() {
        //TODO should be created via bring
        this.customScheduler = new CustomScheduleConfiguration().customScheduler();
    }

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

    private void invokeTaskMethod(Object bean, Method method, String taskName) {
        try {
            method.invoke(bean);
        } catch (Exception exe) {
            log.error("Cannot invoke taskName [" + taskName + "] error " + exe.getMessage(), exe);
        }
    }
}
