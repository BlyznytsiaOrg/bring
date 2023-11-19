package com.bobocode.bring.testdata.di.positive.prototype;

import com.bobocode.bring.core.anotation.Bean;
import com.bobocode.bring.core.anotation.Configuration;
import com.bobocode.bring.core.anotation.Scope;
import com.bobocode.bring.core.utils.BeanScopeUtils;

@Configuration
public class TestConfiguration {

  @Scope(BeanScopeUtils.PROTOTYPE)
  @Bean
  public SimpleClass simpleClass() {
    return new SimpleClass();
  }
  
}
