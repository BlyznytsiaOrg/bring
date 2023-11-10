package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledTask {
    String value() default "";
    long initialDelay() default 0;
    long period();
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
