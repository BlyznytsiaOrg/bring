package com.bobocode.bring.testdata.di.positive.prototype.on;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SimpleClass {
  
  private final UUID uuid = UUID.randomUUID();

}
