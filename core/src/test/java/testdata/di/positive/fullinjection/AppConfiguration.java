package testdata.di.positive.fullinjection;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;

@Configuration
public class AppConfiguration {
    
    @Bean("restClient1")
    public RestClient restClient() {
        return RestClient.builder()
                .url("https://exterl.service")
                .username("user100")
                .build();
    }

    @Bean
    public RestClient restClient2() {
        return RestClient.builder()
                .url("https://exterl.service2")
                .username("user200")
                .build();
    }
    
}
