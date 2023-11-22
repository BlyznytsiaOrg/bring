package com.bobocode.bring.testdata.di.positive.prototype.off;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;
import com.bobocode.bring.core.anotation.Scope;
import com.bobocode.bring.core.domain.BeanScope;

@Configuration
public class TestConfiguration {

  @Scope(name = BeanScope.PROTOTYPE)
  @Bean
  public SimpleClass simpleClass() {
    return new SimpleClass();
  }
  
}
