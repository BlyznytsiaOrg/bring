package testdata.di.negative.configuration.noconfigbean;

import io.github.blyznytsiaorg.bring.core.annotation.Bean;
import testdata.di.negative.configuration.A;

public class AppConfiguration {
    
    @Bean
    public A a() {
        return A.builder().field("Hello!").build();
    }
    
}
