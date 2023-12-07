package testdata.di.positive.primary.bean;

import io.github.blyznytsiaorg.bring.core.annotation.Bean;
import io.github.blyznytsiaorg.bring.core.annotation.Configuration;
import io.github.blyznytsiaorg.bring.core.annotation.Primary;

@Configuration
public class Config {

  @Bean
  public Employee tomEmployee() {
    return new Employee("Tom");
  }

  @Bean
  @Primary
  public Employee jerryEmployee() {
    return new Employee("Jerry");
  }
}
