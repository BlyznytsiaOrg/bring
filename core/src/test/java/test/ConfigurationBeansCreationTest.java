package test;

import static org.assertj.core.api.Assertions.assertThat;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import data.client.RestClient;
import data.configuration.TestConfiguration;
import data.service.BringService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class ConfigurationBeansCreationTest {

    @Disabled("Reason: Unable to create Bean of type=[class [Z], methodName=[$jacocoInit]. Method is not annotated")
    @DisplayName("All beans from configuration class registered in Bring Context")
    @Test
    void testConfigurationBeansRegistration() {
        BringApplicationContext bringApplicationContext = BringApplication.run(TestConfiguration.class);
        
        RestClient restClient = bringApplicationContext.getBean(RestClient.class);
        Map<String, BringService> bringServices = bringApplicationContext.getBeans(BringService.class);
        
        assertThat(restClient).isNotNull();
        assertThat(bringServices).hasSize(2);
        
        List<RestClient> restClients = bringServices.values().stream().map(BringService::getBringRestClient).toList();
        assertThat(restClients.stream().allMatch(rc -> rc.equals(restClient))).isTrue();
    }
    
}
