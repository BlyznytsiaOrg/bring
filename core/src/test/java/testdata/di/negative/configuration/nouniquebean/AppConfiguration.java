package testdata.di.negative.configuration.nouniquebean;

import io.github.blyznytsiaorg.bring.core.annotation.Bean;
import io.github.blyznytsiaorg.bring.core.annotation.Configuration;
import testdata.di.negative.configuration.A;
import testdata.di.negative.configuration.B;

@Configuration
public class AppConfiguration {
    
    @Bean
    public A a() {
        return A.builder().field("Hello!").build();
    }

    @Bean("a")
    public A aa() {
        return A.builder().field("Hello!").build();
    }
    
    @Bean
    public B b(A a) {
        return B.builder().a(a).build();
    }
    
}
