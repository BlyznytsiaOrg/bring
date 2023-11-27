package testdata.bpp;

import com.bobocode.bring.core.annotation.BeanProcessor;
import com.bobocode.bring.core.bpp.BeanPostProcessor;

@BeanProcessor
public class NoDefaultConstructorBeanPostProcessor implements BeanPostProcessor {

    public NoDefaultConstructorBeanPostProcessor(String test) {
    }
}
