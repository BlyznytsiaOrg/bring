package com.bobocode.bring.testdata.di.positive.prototype.on;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;
import com.bobocode.bring.core.anotation.Scope;
import com.bobocode.bring.core.domain.BeanScope;
import com.bobocode.bring.core.domain.ProxyMode;

@Configuration
public class TestConfiguration {

  @Scope(name = BeanScope.PROTOTYPE, proxyMode = ProxyMode.ON)
  @Bean
  public SimpleClass simpleClass() {
    return new SimpleClass();
  }
  
}
