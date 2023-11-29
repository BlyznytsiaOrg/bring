package com.bobocode.bring.core.bpp.impl;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.annotation.PostConstruct;
import com.bobocode.bring.core.bpp.BeanPostProcessor;
import com.bobocode.bring.core.exception.PostConstructException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static com.bobocode.bring.core.utils.ReflectionUtils.processBeanPostProcessorAnnotation;


@BeanProcessor
public class PostConstructBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessInitialization(Object bean, String beanName) {
        Method[] declaredMethods = bean.getClass().getMethods();
        try {
            processBeanPostProcessorAnnotation(bean, declaredMethods, PostConstruct.class);
        } catch (Exception exception) {
            throw new PostConstructException(exception);
        }

        return BeanPostProcessor.super.postProcessInitialization(bean, beanName);
    }
}
