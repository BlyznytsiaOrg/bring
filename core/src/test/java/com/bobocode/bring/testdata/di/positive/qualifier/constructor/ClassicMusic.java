package com.bobocode.bring.testdata.di.positive.qualifier.constructor;

import com.bobocode.bring.core.anotation.Component;

@Component
public class ClassicMusic implements Music{

  @Override
  public String getSong() {
    return "Beethoven - Moonlight Sonata";
  }
}
