package testdata.di.negative.configuration.notabean;

import io.github.blyznytsiaorg.bring.core.annotation.Configuration;
import testdata.di.negative.configuration.A;

@Configuration
public class AppConfiguration {
    
    public A a() {
        return A.builder().field("Hello!").build();
    }
    
}
