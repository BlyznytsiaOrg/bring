package testdata.di.positive.primary.bean;

import com.bobocode.bring.core.annotation.Bean;
import com.bobocode.bring.core.annotation.Configuration;
import com.bobocode.bring.core.annotation.Primary;

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
