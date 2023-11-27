package testdata.di.positive.prototype.off;

import com.bobocode.bring.core.annotation.Bean;
import com.bobocode.bring.core.annotation.Configuration;
import com.bobocode.bring.core.annotation.Scope;
import com.bobocode.bring.core.domain.BeanScope;

@Configuration
public class TestConfiguration {

  @Scope(name = BeanScope.PROTOTYPE)
  @Bean
  public SimpleClass simpleClass() {
    return new SimpleClass();
  }
  
}
