package testdata.bfpp;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.bfpp.BeanFactoryPostProcessor;
import com.bobocode.bring.core.context.impl.DefaultBringBeanFactory;

@BeanProcessor
public class NoConstructorWithReflectionParameterBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    public NoConstructorWithReflectionParameterBeanFactoryPostProcessor(String test) {
    }

    @Override
    public void postProcessBeanFactory(DefaultBringBeanFactory defaultBeanFactory) {

    }
}
