package com.bobocode.bring.testdata.di.positive.primary.component;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Component;
import lombok.Getter;

@Component
@Getter
public class C {

  @Autowired
  private I field;

}
