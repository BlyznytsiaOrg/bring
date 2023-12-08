package testdata.di.positive.mixconfigurationandservice;

import io.github.blyznytsiaorg.bring.core.annotation.Bean;
import io.github.blyznytsiaorg.bring.core.annotation.Configuration;

@Configuration
public class MyConfiguration {

    @Bean
    public BeanA beanA() {
        return new BeanA();
    }
}
