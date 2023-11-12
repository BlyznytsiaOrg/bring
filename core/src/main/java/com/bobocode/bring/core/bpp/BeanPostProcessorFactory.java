package com.bobocode.bring.core.bpp;

import com.bobocode.bring.core.bpp.impl.ScheduleBeanPostProcessor;

import java.util.Arrays;
import java.util.List;

public class BeanPostProcessorFactory {

    private final List<BeanPostProcessor> beanPostProcessors;

    public BeanPostProcessorFactory() {
        this.beanPostProcessors = Arrays.asList(
                new ScheduleBeanPostProcessor()
        );
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }
}
