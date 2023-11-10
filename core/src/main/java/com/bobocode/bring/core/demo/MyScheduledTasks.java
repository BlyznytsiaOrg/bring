package com.bobocode.bring.core.demo;

import com.bobocode.bring.core.anotation.Component;
import com.bobocode.bring.core.anotation.ScheduledTask;

import java.time.LocalDateTime;

@Component
public class MyScheduledTasks {

    @ScheduledTask(value = "myTask1", period = 10)
    public void scheduledMethod1() {
        System.out.println(Thread.currentThread().getName() + " scheduledMethod1 " + LocalDateTime.now());
    }
}
