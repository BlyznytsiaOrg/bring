package com.bobocode.bring.testdata.di.positive.qualifier.constructor;

import com.bobocode.bring.core.anotation.Component;

@Component
public class RockMusic implements Music {

  @Override
  public String getSong() {
    return "Lynyrd Skynyrd - Sweet Home Alabama";
  }
}
