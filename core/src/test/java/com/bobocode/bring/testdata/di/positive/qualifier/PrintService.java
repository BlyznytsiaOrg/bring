package com.bobocode.bring.testdata.di.positive.qualifier;

import com.bobocode.bring.core.anotation.Autowired;
import com.bobocode.bring.core.anotation.Qualifier;
import com.bobocode.bring.core.anotation.Service;
import lombok.Getter;

@Service
@Getter
public class PrintService {

  @Autowired
  @Qualifier("CanonPrinter")
  private Printer printer;

}
