package testdata.di.negative.configuration.notabean;

import com.bobocode.bring.core.anotation.Configuration;
import testdata.di.negative.configuration.A;

@Configuration
public class AppConfiguration {
    
    public A a() {
        return A.builder().field("Hello!").build();
    }
    
}
