package com.bobocode.bring.core.postprocessor;

import com.bobocode.bring.core.context.impl.DefaultBringBeanFactory;

public interface BeanFactoryPostProcessor {
    
    void postProcessBeanFactory(DefaultBringBeanFactory defaultBeanFactory);
    
}
