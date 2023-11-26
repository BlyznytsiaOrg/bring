package testdata.di.positive.configuration.configuration;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;
import com.bobocode.bring.core.anotation.Primary;
import com.bobocode.bring.core.anotation.Scope;
import com.bobocode.bring.core.domain.BeanScope;
import com.bobocode.bring.core.domain.ProxyMode;
import testdata.di.positive.configuration.client.RestClient;
import testdata.di.positive.configuration.service.BringService;

@Configuration
public class TestConfiguration {

    @Primary
    @Bean
    public RestClient bringRestClient() {
        return RestClient.builder()
                .url("https://")
                .key("KEY")
                .build();
    }
    
    @Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.ON)
    @Bean("bringRestClient22")
    public RestClient bringRestClient2() {
        return RestClient.builder()
                .url("https://2")
                .key("KEY2")
                .build();
    }

    @Bean
    public BringService bringService(final RestClient bringRestClient) {
        return new BringService(bringRestClient);
    }

    @Bean
    public BringService bringService2(final RestClient bringRestClient) {
        return new BringService(bringRestClient);
    }

    @Bean("urlValue")
    public String urlssssss() {
        return "https://ssssss";
    }
    
    @Bean
    public String url(final String urlValue) {
        return urlValue;
    }

    @Bean
    public BringService bringService3(final RestClient bringRestClient, final String url) {
        RestClient restClient = bringRestClient.toBuilder().url(url).build();
        
        return new BringService(restClient);
    }
    
}
