package testdata.di.negative.configuration.noconfigbean;

import com.bobocode.bring.core.anotation.Bean;
import testdata.di.negative.configuration.A;

public class AppConfiguration {
    
    @Bean
    public A a() {
        return A.builder().field("Hello!").build();
    }
    
}