package testdata.di.positive.prototype.off;

import io.github.blyznytsiaorg.bring.core.annotation.Bean;
import io.github.blyznytsiaorg.bring.core.annotation.Configuration;
import io.github.blyznytsiaorg.bring.core.annotation.Scope;
import io.github.blyznytsiaorg.bring.core.domain.BeanScope;

@Configuration
public class TestConfiguration {

  @Scope(name = BeanScope.PROTOTYPE)
  @Bean
  public SimpleClass simpleClass() {
    return new SimpleClass();
  }
  
}
