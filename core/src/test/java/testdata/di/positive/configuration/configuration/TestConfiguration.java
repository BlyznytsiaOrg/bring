package testdata.di.positive.configuration.configuration;

import io.github.blyznytsiaorg.bring.core.annotation.Bean;
import io.github.blyznytsiaorg.bring.core.annotation.Configuration;
import io.github.blyznytsiaorg.bring.core.annotation.Primary;
import io.github.blyznytsiaorg.bring.core.annotation.Qualifier;
import io.github.blyznytsiaorg.bring.core.annotation.Scope;
import io.github.blyznytsiaorg.bring.core.domain.BeanScope;
import io.github.blyznytsiaorg.bring.core.domain.ProxyMode;
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
    public BringService bringService(RestClient bringRestClient) {
        return new BringService(bringRestClient);
    }

    @Bean
    public BringService bringService2(RestClient bringRestClient) {
        return new BringService(bringRestClient);
    }

    @Bean("urlValue")
    public String urlssssss() {
        return "https://ssssss";
    }
    
    @Bean
    public String url(String urlValue) {
        return urlValue;
    }

    @Bean
    public BringService bringService3(@Qualifier("bringRestClient") RestClient client, String url) {
        RestClient restClient = client.toBuilder().url(url).build();
        
        return new BringService(restClient);
    }
    
}
