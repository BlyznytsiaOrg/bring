package testdata.di.positive.qualifier.field;

import com.bobocode.bring.core.annotation.Component;

@Component
public class CanonPrinter implements Printer{

  @Override
  public String print() {
    return "Canon";
  }
}
