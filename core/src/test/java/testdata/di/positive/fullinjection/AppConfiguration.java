package testdata.di.positive.fullinjection;

import com.bobocode.bring.core.annotation.Bean;
import com.bobocode.bring.core.annotation.Configuration;

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

    @Bean
    public RestClient restClient3(DefaultRestClient defaultRestClient) {
        return RestClient.builder()
                .url(defaultRestClient.getUrl())
                .username(defaultRestClient.getUsername())
                .build();
    }
    
}
