package test;

import com.bobocode.bring.core.BringApplication;
import com.bobocode.bring.core.context.impl.BringApplicationContext;
import data.beans.BringBeansService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentBeansCreationTest {
     
    @Disabled("Reason: No suitable resolver found for data.service.BringService")
    @DisplayName("@Component and @Service beans registered in Bring Context")
    @Test
    void createComponentBeans() {
        BringApplicationContext bringApplicationContext = BringApplication.run(BringBeansService.class);
        BringBeansService bringBeansService = bringApplicationContext.getBean(BringBeansService.class);
        
        assertThat(bringBeansService).isNotNull();
    }
    
}
