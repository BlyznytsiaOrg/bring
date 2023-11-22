package com.bobocode.bring.testdata.di.positive.primary.bean;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;
import com.bobocode.bring.core.anotation.Primary;

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
