package com.bobocode.bring.testdata.di.positive.qualifier;

import com.bobocode.bring.core.anotation.Component;

@Component
public class LexmarkPrinter implements Printer{

  @Override
  public String print() {
    return "Lexmark";
  }
}
