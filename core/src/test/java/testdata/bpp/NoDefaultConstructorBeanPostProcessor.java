package testdata.bpp;

import io.github.blyznytsiaorg.bring.core.annotation.BeanProcessor;
import io.github.blyznytsiaorg.bring.core.bpp.BeanPostProcessor;

@BeanProcessor
public class NoDefaultConstructorBeanPostProcessor implements BeanPostProcessor {

    public NoDefaultConstructorBeanPostProcessor(String test) {
    }
}
