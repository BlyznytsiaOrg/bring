package com.bobocode.bring.web.servlet.annotation;

import com.bobocode.bring.web.servlet.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ResponseStatus {
    HttpStatus value() default HttpStatus.INTERNAL_SERVER_ERROR;

    String reason() default "";
}
