package testdata.di.negative.configuration.nosuchbean;

import com.bobocode.bring.core.annotation.Bean;
import com.bobocode.bring.core.annotation.Configuration;
import testdata.di.negative.configuration.A;
import testdata.di.negative.configuration.B;

@Configuration
public class AppConfiguration {
    
    @Bean
    public A a() {
        return A.builder().field("Hello!").build();
    }
    
    @Bean
    public B b(A aaaa) {
        return B.builder().a(aaaa).build();
    }
    
}
