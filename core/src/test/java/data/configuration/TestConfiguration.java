package data.configuration;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;
import data.client.RestClient;
import data.service.BringService;

@Configuration
public class TestConfiguration {

    @Bean
    public RestClient bringRestClient() {
        final RestClient restClient = new RestClient();
        restClient.setUrl("https://");
        restClient.setKey("KEY");
        
        return restClient;
    }

    @Bean
    public BringService bringService(final RestClient bringRestClient) {
        return new BringService(bringRestClient);
    }

    @Bean
    public BringService bringService2(final RestClient bringRestClient) {
        return new BringService(bringRestClient);
    }

//    @Bean
//    public BringService bringServiceInterBean() {
//        return new BringService(bringRestClient());
//    }
    
}
