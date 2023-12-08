package testdata.bfpp;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.bfpp.BeanFactoryPostProcessor;
import io.github.blyznytsiaorg.bring.core.context.impl.DefaultBringBeanFactory;

@BeanProcessor
public class NoConstructorWithReflectionParameterBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    public NoConstructorWithReflectionParameterBeanFactoryPostProcessor(String test) {
    }

    @Override
    public void postProcessBeanFactory(DefaultBringBeanFactory defaultBeanFactory) {

    }
}
