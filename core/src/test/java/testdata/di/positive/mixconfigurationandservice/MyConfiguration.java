package testdata.di.positive.mixconfigurationandservice;

import com.bobocode.bring.core.annotation.Bean;
import com.bobocode.bring.core.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    public BeanA beanA() {
        return new BeanA();
    }
}
