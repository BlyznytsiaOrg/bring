package testdata.di.positive.mixconfigurationandservice;

import com.bobocode.bring.core.annotation.Configuration;

@Configuration
public class MyConfiguration {

    public BeanA beanA() {
        return new BeanA();
    }
}
