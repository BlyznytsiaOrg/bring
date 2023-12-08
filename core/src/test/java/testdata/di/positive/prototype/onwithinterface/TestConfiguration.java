package testdata.di.positive.prototype.onwithinterface;

import io.github.blyznytsiaorg.bring.core.annotation.Bean;
import io.github.blyznytsiaorg.bring.core.annotation.Configuration;
import io.github.blyznytsiaorg.bring.core.annotation.Scope;
import io.github.blyznytsiaorg.bring.core.domain.BeanScope;
import io.github.blyznytsiaorg.bring.core.domain.ProxyMode;

@Configuration
public class TestConfiguration {

  @Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.ON)
  @Bean
  public SimpleClass simpleClass() {
    return new SimpleClass();
  }
  
}
