package com.bobocode.bring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bobocode.bring.core.domain.BeanScope;
import com.bobocode.bring.core.domain.ProxyMode;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Scope {

    BeanScope name();
    
    ProxyMode proxyMode() default ProxyMode.OFF;
    
}
