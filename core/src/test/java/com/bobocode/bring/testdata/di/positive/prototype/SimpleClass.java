package com.bobocode.bring.testdata.di.positive.prototype;

import java.util.UUID;

import lombok.Getter;

@Getter
public class SimpleClass {
  
  private final UUID uuid = UUID.randomUUID();

}
